package com.taotao.cloud.open.openapi.common.model;


/**
 * 密钥对
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:10:13
 */
public class KeyPair {

    /**
     * 私钥（Base64字符串表示）
     */
    private String privateKey;

    /**
     * 公钥（Base64字符串表示）
     */
    private String publicKey;

    /**
     * 构造密钥对
     */
    public KeyPair() {
    }

    /**
     * 构造密钥对
     *
     * @param privateKey 私钥
     * @param publicKey  公钥
     */
    public KeyPair(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
}
