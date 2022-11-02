package com.taotao.cloud.pay.wxpay.wx.enums;


/**
 * @author lingting 2021/1/29 18:14
 */
public enum SignType {

	/**
	 * 一般用于沙箱环境
	 */
	MD5("MD5"),
	/**
	 * 一般用于正式环境
	 */
	HMAC_SHA256("HMAC-SHA256"),

	;

	private final String str;

	SignType(String str) {
		this.str = str;
	}


	public String getStr() {
		return str;
	}

	public static SignType of(String str) {
		for (SignType e : values()) {
			if (e.str.equals(str)) {
				return e;
			}
		}
		return null;
	}

}
