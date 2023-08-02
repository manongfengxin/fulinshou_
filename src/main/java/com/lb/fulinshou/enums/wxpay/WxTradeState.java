package com.lb.fulinshou.enums.wxpay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商户平台和微信之间的状态
 */
@AllArgsConstructor
@Getter
public enum WxTradeState {

    /**
     * 支付成功
     */
    SUCCESS("SUCCESS"),

    /**
     * 未支付
     */
    NOTPAY("NOTPAY"),

    /**
     * 已关闭
     */
    CLOSED("CLOSED"),

    /**
     * 转入退款
     */
    REFUND("REFUND");

    /**
     * 类型
     */
    private final String type;
}
