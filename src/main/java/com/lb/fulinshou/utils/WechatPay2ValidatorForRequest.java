package com.lb.fulinshou.utils;


import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;

import static com.wechat.pay.contrib.apache.httpclient.constant.WechatPayHttpHeaders.*;

/**
 * 改造微信提供的sdk中的WechatPay2Validator类
 */
public class WechatPay2ValidatorForRequest {

    protected static final Logger log = LoggerFactory.getLogger(WechatPay2ValidatorForRequest.class);
    /**
     * 应答超时时间，单位为分钟
     */
    protected static final long RESPONSE_EXPIRED_MINUTES = 5;
    protected final Verifier verifier;
    //添加的参数，请求id
    protected final String requestId;
    //添加的参数，请求体
    protected final String body;


    public WechatPay2ValidatorForRequest(Verifier verifier, String requestId, String body) {
        this.verifier = verifier;
        this.requestId = requestId;
        this.body = body;
    }

    protected static IllegalArgumentException parameterError(String message, Object... args) {
        message = String.format(message, args);
        return new IllegalArgumentException("parameter error: " + message);
    }

    protected static IllegalArgumentException verifyFail(String message, Object... args) {
        message = String.format(message, args);
        return new IllegalArgumentException("signature verify fail: " + message);
    }

    public final boolean validate(HttpServletRequest request) throws IOException {
        try {
            //处理请求参数
            validateParameters(request);

            //构造验签名串
            String message = buildMessage(request);

            //从请求头当中拿到平台证书序列号
            String serial = request.getHeader(WECHAT_PAY_SERIAL);

            //从请求头当中拿到请求中携带的签名
            String signature = request.getHeader(WECHAT_PAY_SIGNATURE);

            //验签
            if (!verifier.verify(serial, message.getBytes(StandardCharsets.UTF_8), signature)) {
                throw verifyFail("serial=[%s] message=[%s] sign=[%s], request-id=[%s]",
                        serial, message, signature, requestId);
            }
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());
            return false;
        }

        return true;
    }

    //处理请求参数
    protected final void validateParameters(HttpServletRequest request) {

        // NOTE: ensure HEADER_WECHAT_PAY_TIMESTAMP at last
        String[] headers = {WECHAT_PAY_SERIAL, WECHAT_PAY_SIGNATURE, WECHAT_PAY_NONCE, WECHAT_PAY_TIMESTAMP};

        String header = null;
        //遍历请求头的键值，从请求头当中拿到headers中的值再进行非空判断
        for (String headerName : headers) {
            header = request.getHeader(headerName);
            if (header == null) {
                throw parameterError("empty [%s], request-id=[%s]", headerName, requestId);
            }
        }

        //判断请求是否过期
        String timestampStr = header;//时间戳
        try {
            Instant responseTime = Instant.ofEpochSecond(Long.parseLong(timestampStr));
            // 拒绝过期请求
            if (Duration.between(responseTime, Instant.now()).abs().toMinutes() >= RESPONSE_EXPIRED_MINUTES) {
                throw parameterError("timestamp=[%s] expires, request-id=[%s]", timestampStr, requestId);
            }
        } catch (DateTimeException | NumberFormatException e) {
            throw parameterError("invalid timestamp=[%s], request-id=[%s]", timestampStr, requestId);
        }
    }

    //构造验签名串
    protected final String buildMessage(HttpServletRequest request) throws IOException {
        //时间戳
        String timestamp = request.getHeader(WECHAT_PAY_TIMESTAMP);
        //随机数字符串
        String nonce = request.getHeader(WECHAT_PAY_NONCE);
        return timestamp + "\n"
                + nonce + "\n"
                //报文主体使用传递过来的参数设置
                + body + "\n";
    }

    protected final String getResponseBody(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        return (entity != null && entity.isRepeatable()) ? EntityUtils.toString(entity) : "";
    }

}
