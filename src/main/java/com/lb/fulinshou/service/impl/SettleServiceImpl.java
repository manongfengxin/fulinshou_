package com.lb.fulinshou.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.lb.fulinshou.config.WxPayConfig;
import com.lb.fulinshou.enums.OrderStatus;
import com.lb.fulinshou.enums.wxpay.WxApiType;
import com.lb.fulinshou.enums.wxpay.WxNotifyType;
import com.lb.fulinshou.mapper.*;
import com.lb.fulinshou.pojo.Goods;
import com.lb.fulinshou.pojo.Order;
import com.lb.fulinshou.pojo.Receipt;
import com.lb.fulinshou.pojo.dto.SettleGoods;
import com.lb.fulinshou.pojo.dto.SettleOrder;
import com.lb.fulinshou.service.ShoppingCarService;
import com.lb.fulinshou.utils.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结算界面的服务
 */
@Service
@Slf4j
public class SettleServiceImpl {
    @Resource
    private ReceiptMapper receiptMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private PhotoMapper photoMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private WxPayConfig wxPayConfig;

    @Resource//因为在WxPayConfig中，对获得HttpClient的方法标注了@Bean，所以这里可以直接注入
    private CloseableHttpClient wxPayClient;

    @Autowired
    private ShoppingCarService shoppingCarService;


    /**
     * 获取用户名下所有收货地址信息
     */
    public List<Receipt> getReceipt(Long receiptUserId){
        List<Receipt> receiptList = receiptMapper.selectReceiptByReceiptUserId(receiptUserId);
        return receiptList;
    }

    /**
     * 根据商品id获取商品的部分信息
     */
    public SettleGoods getSettleGoods(Long goodsId){
        //先获取商品的其余信息，id title price
        Goods goods = goodsMapper.selectOtherGoodsById(goodsId);

        SettleGoods settleGoods = new SettleGoods(goods);

        //再从商品图片中取第一个图片
        String photoUrl = photoMapper.selectPhotoByPhotoGoodsId(goods.getGoodsId());

        //然后将商品的图片列表注入TGoods中
        settleGoods.setSettleGoodsPhoto(photoUrl);

        //返回商品总信息TGoods
        return settleGoods;
    }

    /**
     * 根据前端打包汇总的订单信息，添加到订单表中，并且返回支付参数供前端发起支付请求
     */
    public JSONObject addOrder(SettleOrder settleOrder) throws IOException {
        log.info("生成order");
        //生成订单并且插入到数据库中
        Order order = this.createOrderBySettleOrder(settleOrder);

        //调用下单API
        log.info("调用下单API");

        HttpPost httpPost = new HttpPost(wxPayConfig.getDomain().concat(WxApiType.JSAPI_PAY.getType()));//服务器地址+JSAPI下单地址
        //组装请求参数
        Gson gson = new Gson();
        Map paramsMap = new HashMap();
        paramsMap.put("appid", wxPayConfig.getAppid());//appid
        paramsMap.put("mchid", wxPayConfig.getMchId());//商户号
        paramsMap.put("description", order.getOrderDescribe());//商品描述
        paramsMap.put("out_trade_no", order.getOrderNo());//商户订单号——后台生成的
        paramsMap.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.WXPAY_NOTIFY.getType()));//通知地址，支付结果通知地址

        Map amountMap = new HashMap();
        amountMap.put("total", order.getOrderPrice());//订单总金额
        amountMap.put("currency", "CNY");//货币类型

        paramsMap.put("amount", amountMap);//订单金额信息

        Map payerMap = new HashMap();
        payerMap.put("openid", userMapper.selectOpenIdById(order.getOrderUserId()));//用户的openid

        paramsMap.put("payer", payerMap);//支付者信息

        //将请求参数转换成JSON字符串
        String jsonParams = gson.toJson(paramsMap);

        log.info("请求参数 ==> {}", jsonParams);

        //发送请求所必须要设置的格式
        StringEntity entity = new StringEntity(jsonParams,"utf-8");//使用utf-8编码
        entity.setContentType("application/json");//设置内容类型为application/json
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");//设置接收的数据格式也是application/json

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpPost);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());//从响应对象中解析出响应体
            int statusCode = response.getStatusLine().getStatusCode();//从响应对象中解析出响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功，返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                log.info("小程序JSAPI下单失败,响应码 = " + statusCode+ ",返回结果 = " + bodyAsString);
                throw new IOException("request failed");
            }

            //成功后，处理响应结果
            Map<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
            //从响应结果中得到prepay_id-预支付交易会话标识
            String prepayId = resultMap.get("prepay_id");

            //保存prepayId，新生的订单不会有这个值
            this.savePrepayId(order.getOrderNo(), prepayId);

            //生成带签名的支付信息
            Map<String, String> requestParams = WxPayKit.miniAppPrepayIdCreateSign(wxPayConfig.getAppid(), prepayId, wxPayConfig.getApiV3Key(), SignType.RSA);

            //组装成Object参数
            JSONObject jsonObject = new JSONObject(requestParams);

            return jsonObject;

        } finally {
            response.close();
        }
    }

    /**
     * 保存订单的预支付交易会话标识
     * @param prepayId
     */
    private void savePrepayId(String orderNo, String prepayId) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo);

        Order order = new Order();
        order.setOrderPrepayId(prepayId);

        orderMapper.update(order, orderQueryWrapper);
    }

    /**
     * 创建订单
     * @param settleOrder
     * @return
     */
    private Order createOrderBySettleOrder(SettleOrder settleOrder) {
        //商品id列表
        List<Long> goodsIdList = settleOrder.getGoodsIdList();
        //商品数量列表
        List<Integer> goodsAmountList = settleOrder.getGoodsAmountList();


        //判断两个列表是否长度一样
        if(goodsIdList.size() == goodsAmountList.size()){
            //订单描述
            String orderDescribe = "";

            //订单总价，单位分
            int orderPrice = 0;

            //长度一样才插入订单
            for (int i = 0; i < goodsIdList.size(); i++) {
                //组建订单描述：商品id*商品数量，中间用#分割
                orderDescribe = orderDescribe.concat(goodsIdList.get(i) + "&" + goodsAmountList.get(i) + "#");

                //根据商品id获取到商品单价，再乘以数量，得到总价
                orderPrice  = orderPrice + (goodsMapper.selectPriceById(goodsIdList.get(i)) * goodsAmountList.get(i));
            }

            //生成订单
            Order order = new Order();
            order.setOrderNo(OrderNoUtils.getOrderNo());//订单编号
            order.setOrderDescribe(orderDescribe);//订单描述
            order.setOrderPrice(orderPrice);//订单总价，单位分
            order.setOrderReceiptId(settleOrder.getReceiptId());//地址id
            order.setOrderRemarks(settleOrder.getRemarks());//订单备注
            order.setOrderUserId(settleOrder.getUserId());//订单所属用户id
            order.setOrderStatus(OrderStatus.NOTPAY.getType());//订单状态，此时为未支付

            log.info("order ==> {}", order);

            //插入订单到数据库中
            orderMapper.insert(order);

            // 清空购物车对应商品
            for (Long goodsId : goodsIdList) {
                shoppingCarService.deleteShoppingCar(settleOrder.getUserId(),goodsId);
            }

            //返回订单数据
            return order;
        }

        return null;
    }


}
