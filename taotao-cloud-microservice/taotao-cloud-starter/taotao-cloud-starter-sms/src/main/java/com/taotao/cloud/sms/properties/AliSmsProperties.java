/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * AliSmsProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:54:47
 */
@RefreshScope
@ConfigurationProperties(prefix = AliSmsProperties.PREFIX)
public class AliSmsProperties {

	public static final String PREFIX = "taotao.cloud.sms.aliyun";

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
