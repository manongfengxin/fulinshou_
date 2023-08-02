package com.lb.fulinshou.model;

import lombok.Data;

/**
 * 用户登录的实体类
 */
@Data
public class WXAuth {
    private String encryptedData;
    private String iv;
    private String sessionId;
}