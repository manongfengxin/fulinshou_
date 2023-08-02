package com.lb.fulinshou.service;

public interface WxService {
    String wxDecrypt(String encryptedData, String sessionId, String vi)throws Exception;

//    String getStringRandom(int length);
}
