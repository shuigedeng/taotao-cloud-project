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
package com.taotao.cloud.sms.channel.jpush;

import com.taotao.cloud.sms.model.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 极光短信配置
 *
 * @author shuigedeng
 */
@ConfigurationProperties(prefix = JPushProperties.PREFIX)
public class JPushProperties extends AbstractHandlerProperties<Integer> {

	public static final String PREFIX = "taotao.cloud.sms.jpush";
	private boolean enabled = false;

	/**
	 * appKey
	 */
	private String appKey;

	/**
	 * masterSecret
	 */
	private String masterSecret;

	/**
	 * 签名ID
	 */
	private Integer signId;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getMasterSecret() {
		return masterSecret;
	}

	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}

	public Integer getSignId() {
		return signId;
	}

	public void setSignId(Integer signId) {
		this.signId = signId;
	}
}
