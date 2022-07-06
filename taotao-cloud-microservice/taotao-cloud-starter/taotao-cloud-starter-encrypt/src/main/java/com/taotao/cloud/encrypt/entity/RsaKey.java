package com.taotao.cloud.encrypt.entity;


/**
 * RSA公私钥实体类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:09:37
 */
public class RsaKey {

	private String publicKey;
	private String privateKey;

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
}
