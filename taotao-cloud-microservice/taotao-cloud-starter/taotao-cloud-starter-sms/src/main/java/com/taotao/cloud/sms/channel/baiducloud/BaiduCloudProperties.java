/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.channel.baiducloud;

import com.taotao.cloud.sms.model.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 百度云短信配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:50:28
 */
@ConfigurationProperties(prefix = BaiduCloudProperties.PREFIX)
public class BaiduCloudProperties extends AbstractHandlerProperties<String> {

	public static final String PREFIX = "taotao.cloud.sms.baiducloud";
	private boolean enabled = false;
	/**
	 * ACCESS_KEY_ID
	 */
	private String accessKeyId;

	/**
	 * SECRET_ACCESS_KEY
	 */
	private String secretAccessKey;

	/**
	 * endpoint
	 */
	private String endpoint;

	/**
	 * 短信签名ID
	 */
	private String signatureId;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getSignatureId() {
		return signatureId;
	}

	public void setSignatureId(String signatureId) {
		this.signatureId = signatureId;
	}
}
