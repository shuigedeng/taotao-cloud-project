package com.taotao.cloud.web.sign.enums;

/**
 * <p>SHA加密类型</p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:42:11
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

	final String value;

	SHAEncryptType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
