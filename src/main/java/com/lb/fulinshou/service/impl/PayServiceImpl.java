package com.lb.fulinshou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.lb.fulinshou.config.WxPayConfig;
import com.lb.fulinshou.enums.OrderStatus;
import com.lb.fulinshou.enums.wxpay.WxApiType;
import com.lb.fulinshou.enums.wxpay.WxTradeState;
import com.lb.fulinshou.mapper.OrderMapper;
import com.lb.fulinshou.mapper.PaymentMapper;
import com.lb.fulinshou.pojo.Order;
import com.lb.fulinshou.pojo.Payment;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class PayServiceImpl {
    @Resource
    private WxPayConfig wxPayConfig;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private PaymentMapper paymentMapper;

    //锁
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired//因为在WxPayConfig中，对获得HttpClient的方法标注了@Bean，所以这里可以直接注入
    private CloseableHttpClient wxPayClient;

    /**
     * 处理订单
     * @param bodyMap
     */
    public void processOrder(Map<String, Object> bodyMap) throws GeneralSecurityException {
        log.info("处理订单");

        //解密报文，得到明文
        String plainText = decryptFromResource(bodyMap);

        //将明文转变成Map，解密后才能得到微信支付通知的结果
        Gson gson = new Gson();
        HashMap plainTextMap = gson.fromJson(plainText, HashMap.class);

        //获取支付的商户订单号——下单的时候作为请求参数传递过去的
        String orderNo = (String)plainTextMap.get("out_trade_no");

        /*在对业务数据进行状态检查和处理前，
         * 要采用数据锁进行并发控制
         * 以避免函数重入造成的数据混乱*/
        //尝试获取锁：成功获取则立即返回true，获取失败则立即返回false，不必一直等待锁的释放
        if (lock.tryLock()) {
            try {
                //处理重复的通知，通知可能会重复，所以接收到通知首先判断该通知是否已经被处理了
                //接口调用的幂等性：无论接口被调用多少次，产生的结果都是一致的
                String orderStatus = orderMapper.selectOrderStatusByOrderNo(orderNo);//根据订单号获取订单的当前状态
                if (!OrderStatus.NOTPAY.getType().equals(orderStatus)) {
                    log.info("不是未支付订单");
                    //如果不是未支付的状态，那么就直接返回，不继续往下执行了
                    return;
                }

                //如果是未支付状态，就继续往下执行

                //更新订单状态，从未支付变为支付成功
                this.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);

                //记录支付日志，在交易单新增一条记录
                this.createPayment(plainText);

            } finally {
                //要主动释放锁
                lock.unlock();
            }
        }
    }

    /**
     * 根据微信支付通知参数明文来添加一条交易信息
     * @param plainText
     */
    private void createPayment(String plainText) {
        log.info("记录支付日志");

        Gson gson = new Gson();
        HashMap plainTextMap = gson.fromJson(plainText, HashMap.class);

        //获取商户订单号
        String orderNo = (String) plainTextMap.get("out_trade_no");
        //获取微信支付订单号——微信生成的订单号
        String transactionId = (String) plainTextMap.get("transaction_id");
        //获取支付类型——NATIE:扫码支付，APP:APP支付，JSAPI:公众号支付 ...
        String tradeType = (String)plainTextMap.get("trade_type");
        //获取交易状态——支付成功，转入退款。。。  这是微信提供的交易状态
        String tradeState = (String) plainTextMap.get("trade_state");
        //先获取订单金额信息
        Map<String, Object> amountMap = (Map<String, Object>) plainTextMap.get("amount");
        //再获取用户实际支付金额，单位分，int类型，但这里直接转int会报错，需要先转double再转int
        Integer payerTotal =  ((Double)amountMap.get("payer_total")).intValue();
        //先获取支付者信息
        Map<String, Object> payerMap = (Map<String, Object>) plainTextMap.get("payer");
        //再获取用户标识openId
        String openid = (String) payerMap.get("openid");

        //组装Payment
        Payment payment = new Payment();
        payment.setOrderNo(orderNo);//订单编号，商户后台的
        payment.setTransactionId(transactionId);//微信支付订单号
        payment.setTradeType(tradeType);//交易类型
        payment.setTradeStatus(tradeState);//交易状态
        payment.setOpenId(openid);//支付者标识
        payment.setPayerTotal(payerTotal);//用户支付金额
        payment.setContent(plainText);//微信返回的全部支付信息

        //插入数据库中
        paymentMapper.insert(payment);
    }

    /**
     * 根据订单编号更新订单状态
     * @param orderNo
     * @param orderStatus
     */
    private void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus) {
        log.info("更新订单状态 ==> {}", orderStatus.getType());

        //构造条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);

        //设置更新
        Order order = new Order();
        order.setOrderStatus(orderStatus.getType());

        //更新
        orderMapper.update(order, queryWrapper);
    }

    /**
     * 对称解密
     * @param bodyMap
     * @return
     */
    private String decryptFromResource(Map<String, Object> bodyMap) throws GeneralSecurityException {
        log.info("密文解密");

        //获取通知数据
        Map<String, String> resourceMap = (Map<String, String>) bodyMap.get("resource");//获取通知参数中的通知数据
        //获取通知数据中的数据密文
        String ciphertext = resourceMap.get("ciphertext");
        //获取通知数据中的随机串
        String nonce = resourceMap.get("nonce");
        //获取通知数据中的附加数据
        String associated_data = resourceMap.get("associated_data");

        log.info("密文 ==> {}", ciphertext);

        //根据微信提供的AesUtil进行解密
        AesUtil aesUtil = new AesUtil(wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));

        //解密后获取明文
        String plainText = aesUtil.decryptToString(associated_data.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                ciphertext);

        log.info("明文 ==> {}", plainText);

        return plainText;
    }

    /**
     * 查询创建超过minutes分钟，并且状态为未支付的订单
     * @param minutes
     * @return
     */
    public List<Order> getNoPayOrderByDuration(int minutes) {
        //用当前时间减去minutes分钟得到的时间，就是minutes分钟之前的时间
        Instant instant = Instant.now().minus(Duration.ofMinutes(minutes));

        //创建查询对象
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_status", OrderStatus.NOTPAY.getType());//订单需要是未支付状态
        queryWrapper.le("create_time", instant);//创建时间早于当前minutes分钟之前的时间，代表这笔订单创建时间已经超过minutes分钟了

        List<Order> orderList = orderMapper.selectList(queryWrapper);

        return orderList;
    }

    /**
     * 核实订单状态
     * 已支付，则更新订单状态为支付成功
     * 未支付，则调用关单接口关闭订单，并且更新订单状态为已关闭
     * @param orderNo
     */
    public void checkOrderStatus(String orderNo) throws IOException {
        log.warn("根据订单号核实订单状态 ==> {}", orderNo);

        //调用微信支付查单接口
        String result = this.queryOrder(orderNo);

        Gson gson = new Gson();
        Map resultMap = gson.fromJson(result, HashMap.class);

        //获取微信支付端的订单状态
        Object tradeState = resultMap.get("trade_state");

        //判断订单状态

        if(WxTradeState.SUCCESS.getType().equals(tradeState)){
            //订单已支付
            log.warn("核实订单已支付 == > {}", orderNo);

            //订单已支付，则更新本地订单状态为已支付
            this.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);

            //记录支付日志
            this.createPayment(result);//查单返回的结果和支付通知返回的结果中的密文是一致的，所以这里直接传递result
        }
        if(WxTradeState.NOTPAY.getType().equals(tradeState)){
            //订单未支付
            log.warn("核实订单未支付 ==> {}", orderNo);

            //订单未支付，调用关单接口
            this.closeOrder(orderNo);

            //更新本地订单状态为已关闭
            this.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
        }

    }

    /**
     * 调用微信关单接口
     * @param orderNo
     */
    private void closeOrder(String orderNo) throws IOException {
        log.info("关单接口的调用，订单号：==> {}", orderNo);

        //创建远程请求对象
        String url = String.format(WxApiType.CLOSE_ORDER_BY_NO.getType(), orderNo);//因为WxApiType.CLOSE_ORDER_BY_NO中有一个占位符，所以用format方法替换
        url = wxPayConfig.getDomain().concat(url);
        HttpPost httpPost = new HttpPost(url);

        //组装json请求体
        Gson gson = new Gson();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mchid", wxPayConfig.getMchId());
        String jsonParams = gson.toJson(paramsMap);

        log.info("请求参数 ==> {}", jsonParams);

        //将请求参数设置到请求对象中
        StringEntity entity = new StringEntity(jsonParams,"utf-8");//使用utf-8编码
        entity.setContentType("application/json");//设置内容类型为application/json
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");//设置接收的数据格式也是application/json

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpPost);

        try {
            int statusCode = response.getStatusLine().getStatusCode();//从响应对象中解析出响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功200");
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功204");
            } else {
                log.info("Native关单失败,响应码 = " + statusCode);
                throw new IOException("request failed");
            }

        } finally {
            response.close();
        }
    }

    /**
     * 调用微信查单接口，并返回结果
     * @param orderNo
     * @return
     */
    private String queryOrder(String orderNo) throws IOException {
        log.info("查单接口调用 ==> {}", orderNo);

        //组装url
        String url = String.format(WxApiType.ORDER_QUERY_BY_NO.getType(), orderNo);
        url = wxPayConfig.getDomain().concat(url).concat("?mchid=").concat(wxPayConfig.getMchId());

        //得到请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");//设置接收的数据格式是application/json

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpGet);

        //处理结果
        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());//从响应对象中解析出响应体
            int statusCode = response.getStatusLine().getStatusCode();//从响应对象中解析出响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功，返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                log.info("Native下单失败,响应码 = " + statusCode+ ",返回结果 = " + bodyAsString);
                throw new IOException("request failed");
            }

            return bodyAsString;

        } finally {
            response.close();
        }
    }
}
