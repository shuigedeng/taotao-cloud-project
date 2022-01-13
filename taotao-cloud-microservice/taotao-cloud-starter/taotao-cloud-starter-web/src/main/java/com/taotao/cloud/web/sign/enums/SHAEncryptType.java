package com.taotao.cloud.web.sign.enums;

/**
 * <p>SHA加密类型</p>
 *
 * @since 2019年4月16日14:10:18
 */
public enum SHAEncryptType {

	/**
	 * SHA224
	 */
	SHA224("sha-224"),
	/**
	 * SHA256
	 */
	SHA256("sha-256"),
	/**
	 * SHA384
	 */
	SHA384("sha-384"),
	/**
	 * SHA512
	 */
	SHA512("sha-512");

	String value;


	SHAEncryptType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
