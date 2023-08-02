package com.lb.fulinshou.config;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

/**
 * 读取wxpay.properties文件中的信息
 */

@Configuration
@PropertySource("classpath:wxpay.properties") //读取配置文件，该注解读取不了.yml的文件
@ConfigurationProperties(prefix="wxpay") //读取wxpay节点，该注解对.yml和.properties都有效
@Data //使用set方法将wxpay节点中的值填充到当前类的属性中
@Slf4j
public class WxPayConfig {

    //驼峰与下划线自动映射

    // 商户号
    private String mchId;

    // 商户API证书序列号
    private String mchSerialNo;

    // 商户私钥文件路径
    private String privateKeyPath;

    // APIv3密钥
    private String apiV3Key;

    // APPID
    private String appid;

    // 微信服务器地址
    private String domain;

    // 接收结果通知地址
    private String notifyDomain;

    /**
     * 获取商户私钥文件
     * @param filename 商户私钥文件路径
     * @return
     */
    public PrivateKey getPrivateKey(String filename){
        try {
            // 私钥存储在文件
            log.info("私钥文件路径 == > {}", filename);
            return PemUtil.loadPrivateKey(
                    new FileInputStream(filename)); //使用微信提供的PemUtil.loadPrivateKey()方法来获取商户私钥文件
        } catch (FileNotFoundException e) {
            throw new RuntimeException("私钥文件不存在", e);
        }
    }

    /**
     * 获取签名验证器
     * @return
     */
    @Bean(name = "verifier") //在启动时创建
    public Verifier getVerifier(){

        log.info("获取签名言验证器");

        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();

        // 获取商户私钥
        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        log.info("商户私钥对象 ==> {}", privateKey);

        // 私钥签名对象
        PrivateKeySigner privateKeySigner = new PrivateKeySigner(mchSerialNo, privateKey);

        // 身份认证对象
        WechatPay2Credentials wechatPay2Credentials = new WechatPay2Credentials(mchId, privateKeySigner);

        // 向证书管理器增加需要自动更新平台证书的商户信息
        try {
            log.info("1.证书管理器certificatesManager ==> {}", certificatesManager);
            certificatesManager.putMerchant(mchId,
                    wechatPay2Credentials,
                    // 对称加密密钥
                    apiV3Key.getBytes(StandardCharsets.UTF_8));
            log.info("2.证书管理器certificatesManager ==> {}", certificatesManager);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (HttpCodeException e) {
            e.printStackTrace();
        }
        // ... 若有多个商户号，可继续调用putMerchant添加商户信息

        // 从证书管理器中获取verifier
        Verifier verifier = null;
        try {
            verifier = certificatesManager.getVerifier(mchId);
        } catch (NotFoundException e) {
            log.info("verifier 有问题");
            e.printStackTrace();
        }

        log.info("Verifier == > {}", verifier);
        return verifier;
    }


    /**
     * 获取http请求对象HttpClient
     * @param verifier
     * @return
     */
    @Bean(name = "wxPayClient") //在启动时创建
    public CloseableHttpClient getWxPayClient(Verifier verifier){

        log.info("获取httpClient");

        // 获取商户私钥
        PrivateKey privateKey = getPrivateKey(privateKeyPath);

        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, privateKey)
                .withValidator(new WechatPay2Validator(verifier));
        // ... 接下来，你仍然可以通过builder设置各种参数，来配置你的HttpClient

        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签，并进行证书自动更新
        CloseableHttpClient httpClient = builder.build();

        return httpClient;

        /*请求发送这里不做，之后调用接口做
        // 后面跟使用Apache HttpClient一样
        CloseableHttpResponse response = httpClient.execute(...);*/
    }

    /**
     * 获取HttpClient，无需进行应答签名验证，跳过验签的流程
     */
    @Bean(name = "wxPayNoSignClient")
    public CloseableHttpClient getWxPayNoSignClient(){

        //获取商户私钥
        PrivateKey privateKey = getPrivateKey(privateKeyPath);

        //用于构造HttpClient
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                //设置商户信息
                .withMerchant(mchId, mchSerialNo, privateKey)
                //无需进行签名验证、通过withValidator((response) -> true)实现
                .withValidator((response) -> true);

        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签，并进行证书自动更新
        CloseableHttpClient httpClient = builder.build();

        log.info("== getWxPayNoSignClient END ==");

        return httpClient;
    }

}
