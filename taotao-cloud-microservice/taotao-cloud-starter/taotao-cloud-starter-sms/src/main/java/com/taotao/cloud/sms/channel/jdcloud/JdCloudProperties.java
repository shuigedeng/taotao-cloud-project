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
package com.taotao.cloud.sms.channel.jdcloud;

import com.taotao.cloud.sms.model.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 京东云短信配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:50:58
 */
@ConfigurationProperties(prefix = JdCloudProperties.PREFIX)
public class JdCloudProperties extends AbstractHandlerProperties<String> {
	public static final String PREFIX = "taotao.cloud.sms.jdcloud";
	private boolean enabled = false;
	/**
	 * AccessKey ID
	 */
	private String accessKeyId;

	/**
	 * AccessKey Secret
	 */
	private String secretAccessKey;

	/**
	 * 地域
	 */
	private String region = "cn-north-1";

	/**
	 * 签名ID
	 */
	private String signId;


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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
	}
}
