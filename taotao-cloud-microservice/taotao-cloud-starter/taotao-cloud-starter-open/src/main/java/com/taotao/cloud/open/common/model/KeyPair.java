package com.taotao.cloud.open.common.model;


/**
 * 密钥对
 *
 * @author wanghuidong
 * 时间： 2022/5/29 10:17
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
