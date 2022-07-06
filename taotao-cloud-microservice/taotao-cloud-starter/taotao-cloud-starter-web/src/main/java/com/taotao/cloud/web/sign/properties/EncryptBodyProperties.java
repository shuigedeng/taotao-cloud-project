package com.taotao.cloud.web.sign.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * <p>加密数据配置读取类</p>
 * <p>在SpringBoot项目中的application.yml中添加配置信息即可</p>
 * <pre>
 *     encrypt:
 *      body:
 *       aes-key: xiaoFuLoveXiaoQi # AES加密秘钥
 *       des-key: xiaoFuLoveXiaoQiu # DES加密秘钥
 * </pre>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:42:59
 */
@RefreshScope
@ConfigurationProperties(prefix = EncryptBodyProperties.PREFIX)
public class EncryptBodyProperties {

	public static final String PREFIX = "taotao.cloud.web.sign.encrypt.body";

	private String aesKey;

	private String desKey;

	private String rsaKey;

	private String encoding = "UTF-8";

	private String rsaPirKey;

	private String rsaPubKey;

	private String sm2PirKey;

	private String sm2PubKey;

	private String sm4Key;

	/**
	 * Aes密码算法及填充方式
	 */
	private String aesCipherAlgorithm = "AES/GCM/NoPadding";

	/**
	 * Aes密码算法及填充方式
	 */
	private String desCipherAlgorithm = "DES/ECB/PKCS5Padding";


	public String getAesKey() {
		return aesKey;
	}

	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}

	public String getDesKey() {
		return desKey;
	}

	public void setDesKey(String desKey) {
		this.desKey = desKey;
	}

	public String getRsaKey() {
		return rsaKey;
	}

	public void setRsaKey(String rsaKey) {
		this.rsaKey = rsaKey;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getRsaPirKey() {
		return rsaPirKey;
	}

	public void setRsaPirKey(String rsaPirKey) {
		this.rsaPirKey = rsaPirKey;
	}

	public String getRsaPubKey() {
		return rsaPubKey;
	}

	public void setRsaPubKey(String rsaPubKey) {
		this.rsaPubKey = rsaPubKey;
	}

	public String getSm2PirKey() {
		return sm2PirKey;
	}

	public void setSm2PirKey(String sm2PirKey) {
		this.sm2PirKey = sm2PirKey;
	}

	public String getSm2PubKey() {
		return sm2PubKey;
	}

	public void setSm2PubKey(String sm2PubKey) {
		this.sm2PubKey = sm2PubKey;
	}

	public String getSm4Key() {
		return sm4Key;
	}

	public void setSm4Key(String sm4Key) {
		this.sm4Key = sm4Key;
	}

	public String getAesCipherAlgorithm() {
		return aesCipherAlgorithm;
	}

	public void setAesCipherAlgorithm(String aesCipherAlgorithm) {
		this.aesCipherAlgorithm = aesCipherAlgorithm;
	}

	public String getDesCipherAlgorithm() {
		return desCipherAlgorithm;
	}

	public void setDesCipherAlgorithm(String desCipherAlgorithm) {
		this.desCipherAlgorithm = desCipherAlgorithm;
	}
}
