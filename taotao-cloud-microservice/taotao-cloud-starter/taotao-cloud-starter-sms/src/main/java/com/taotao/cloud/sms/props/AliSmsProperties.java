package com.taotao.cloud.sms.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = AliSmsProperties.PREFIX)
public class AliSmsProperties {

	public static final String PREFIX = "taotao.cloud.sms.ali";

	/**
	 * 短信API产品名称
	 */
	private String product = "Dysmsapi";

	/**
	 * 短信API产品域名
	 */
	private String domain = "dysmsapi.aliyuncs.com";

	/**
	 * 区域标识
	 */
	private String regionId = "cn-hangzhou";

	/**
	 * accessKeyId
	 */
	private String accessKey;

	/**
	 * accessSecret
	 */
	private String secretKey;

	/**
	 * 短信模板ID
	 */
	private String templateId;

	/**
	 * 短信签名
	 */
	private String signName;

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}
}
