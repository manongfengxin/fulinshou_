package com.lb.fulinshou.controller;

import com.google.gson.Gson;
import com.lb.fulinshou.service.impl.PayServiceImpl;
import com.lb.fulinshou.utils.HttpUtils;
import com.lb.fulinshou.utils.WechatPay2ValidatorForRequest;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付界面的接口
 */
@RestController
@RequestMapping("/wx-pay")
@Slf4j
public class PayController {
    //在WxPayConfig中的签名验证器
    @Resource
    private Verifier verifier;

    @Resource
    private PayServiceImpl payServiceImpl;

    /**
     * 微信支付通过支付通知接口将用户支付成功的消息通知给商户
     * 本地通知接口的地址是在下单的时候作为请求参数传递给微信的
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/native/notify")
    public String nativeNotify(HttpServletRequest request, HttpServletResponse response){
        log.info("微信发给我们的请求:{}" + request);

        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();//应答对象

        try{
            //处理通知参数
            String body = HttpUtils.readData(request);//根据自定义的工具类处理通知参数
            Map<String, Object> bodyMap = gson.fromJson(body, HashMap.class);
            String requestId = (String) bodyMap.get("id");//获取微信支付通知的唯一id
            log.info("支付通知的id ==> {}", requestId);

            //签名验证，对微信支付通知结果进行签名验证，这里是通过改造微信提供的sdk中的WechatPay2Validator类来对签名验证的，0.4.2以上版本好像有新方法，但我没去尝试
            WechatPay2ValidatorForRequest wechatPay2ValidatorForRequest = new WechatPay2ValidatorForRequest(verifier, requestId, body);

            //通过改造的validate方法进行验签
            if(!wechatPay2ValidatorForRequest.validate(request)){
                log.error("通知验签失败");

                //如果验签不通过
                //失败应答
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "通知验签失败");
                return gson.toJson(map);
            }

            log.info("通知验签成功");

            //处理订单，同时处理重复的通知
            payServiceImpl.processOrder(bodyMap);

            //成功应答，返回给微信的应答——成功应答必须为200或204，否则就是失败应答
            response.setStatus(200);
            map.put("code", "SUCCESS");//设置通知应答返回状态码，SUCCESS代表商户平台接收成功，其他代表商户平台接收失败
            map.put("message", "成功");//设置通知应答的返回信息

            return gson.toJson(map);

        } catch (Exception e){
            e.printStackTrace();

            //失败应答
            response.setStatus(500);
            map.put("code", "ERROR");//设置应答返回状态码为非SUCCESS，代表应答失败
            map.put("message", "失败");
            return gson.toJson(map);
        }
    }
}
