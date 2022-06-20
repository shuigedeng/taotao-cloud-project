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
package com.taotao.cloud.sms.aliyun;

import com.taotao.cloud.sms.common.model.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 阿里云短信配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:50:16
 */
@RefreshScope
@ConfigurationProperties(prefix = AliyunProperties.PREFIX)
public class AliyunProperties extends AbstractHandlerProperties<String> {

	public static final String PREFIX = "taotao.cloud.sms.aliyun";

	/**
	 * Endpoint
	 */
	private String endpoint = "cn-hangzhou";

	/**
	 * accessKeyId
	 */
	private String accessKeyId;

	/**
	 * accessKeySecret
	 */
	private String accessKeySecret;

	/**
	 * 短信签名
	 */
	private String signName;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}
}
