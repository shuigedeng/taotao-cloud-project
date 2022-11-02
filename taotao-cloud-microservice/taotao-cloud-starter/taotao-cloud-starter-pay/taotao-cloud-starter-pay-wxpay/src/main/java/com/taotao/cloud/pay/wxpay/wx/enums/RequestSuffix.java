package com.taotao.cloud.pay.wxpay.wx.enums;


/**
 * 请求后缀
 *
 * @author lingting 2021/1/29 18:01
 */
public enum RequestSuffix {

	/**
	 * 下单请求后缀
	 */
	UNIFIEDORDER("unifiedorder"),
	/**
	 * 获取沙箱密钥
	 */
	GETSIGNKEY("getsignkey"),
	/**
	 * 查询订单
	 */
	ORDERQUERY("orderquery"),

	;

	/**
	 * 后缀
	 */
	private final String suffix;

	RequestSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		return suffix;
	}
}
