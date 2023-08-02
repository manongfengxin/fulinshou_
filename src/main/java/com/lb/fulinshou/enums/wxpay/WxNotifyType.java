package com.lb.fulinshou.enums.wxpay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信通知的地址枚举，都是我们自己编写的地址
 */
@AllArgsConstructor
@Getter
public enum WxNotifyType {

	/**
	 * 支付通知
	 * 微信向我们发送支付通知的地址，该地址是我们自己编写的
	 */
	WXPAY_NOTIFY("/wx-pay/native/notify"),


	/**
	 * 退款结果通知
	 */
	REFUND_NOTIFY("/api/wx-pay/refunds/notify");

	/**
	 * 类型
	 */
	private final String type;
}
