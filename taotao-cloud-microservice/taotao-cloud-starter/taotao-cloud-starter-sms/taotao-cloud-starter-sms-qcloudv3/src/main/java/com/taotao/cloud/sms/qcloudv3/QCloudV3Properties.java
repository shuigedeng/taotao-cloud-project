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
package com.taotao.cloud.sms.qcloudv3;

import com.taotao.cloud.sms.common.model.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 腾讯云短信配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:51:58
 */
@RefreshScope
@ConfigurationProperties(prefix = QCloudV3Properties.PREFIX)
public class QCloudV3Properties extends AbstractHandlerProperties<String> {
	public static final String PREFIX = "taotao.cloud.sms.qcloud-v3";
	private boolean enabled = false;
	/**
	 * 腾讯云 SecretID
	 */
	private String secretId;

	/**
	 * 腾讯云 SecretKey
	 */
	private String secretKey;

	/**
	 * 短信签名
	 */
	private String region;

	/**
	 * 短信应用ID
	 */
	private String smsAppId;

	/**
	 * 短信签名
	 */
	private String smsSign;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSmsAppId() {
		return smsAppId;
	}

	public void setSmsAppId(String smsAppId) {
		this.smsAppId = smsAppId;
	}

	public String getSmsSign() {
		return smsSign;
	}

	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}
}
