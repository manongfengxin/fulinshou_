package com.lb.fulinshou.service.impl;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lb.fulinshou.common.RedisKey;
import com.lb.fulinshou.service.WxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

@Component
@Slf4j
public class WxServiceImpl implements WxService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 
     * @param encryptedData   微信传递的加密数据，后端解密
     * @param sessionId       微信传递，解密算法初始向量
     * @param vi             第一步传递前端的sessionId
     * @return
     * @throws Exception
     */
    public String wxDecrypt(String encryptedData, String sessionId, String vi)throws Exception{
//        log.info("encryptedData ==> {}", encryptedData);
//        log.info("sessionId ==> {}", sessionId);
//        log.info("vi ==> {}",vi);
        
        encryptedData.replaceAll(" ", "+");
        sessionId.replaceAll(" ", "+");
        vi.replaceAll(" ", "+");

//        log.info("encryptedData2 ==> {}", encryptedData);
//        log.info("sessionId2 ==> {}", sessionId);
//        log.info("vi2 ==> {}",vi);

        // 开始解密
        
        //从redis中取出登录返回的json字符串
        String json =  redisTemplate.opsForValue().get(RedisKey.WX_SESSION_ID + sessionId);
        //解析JSON格式数据
        JSONObject jsonObject = JSON.parseObject(json);
        log.info("从redis中取出的微信返回的结果的json格式 ==> {}", jsonObject);

        String sessionKey = (String) jsonObject.get("session_key");
        log.info("从redis中取出的sessionKey ==> {}", sessionKey);
        
        byte[] encData = Base64.decode(encryptedData);
        byte[] iv = Base64.decode(vi);
        byte[] key = Base64.decode(sessionKey);
        
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return new String(cipher.doFinal(encData), "UTF-8");
    }



    //生成随机用户名，数字和字母组成
    /*public String getStringRandom(int length) {

        StringBuilder val = new StringBuilder();
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }*/
}
