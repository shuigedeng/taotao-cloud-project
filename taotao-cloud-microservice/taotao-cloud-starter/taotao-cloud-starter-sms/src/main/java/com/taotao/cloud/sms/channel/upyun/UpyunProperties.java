/*
 * Copyright 2018-2022 the original author or authors.
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
package com.taotao.cloud.sms.channel.upyun;

import com.taotao.cloud.sms.model.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 又拍云短信配置
 *
 * @author shuigedeng
 */
@ConfigurationProperties(prefix = UpyunProperties.PREFIX)
public class UpyunProperties extends AbstractHandlerProperties<String> {
	public static final String PREFIX = "taotao.cloud.sms.upyun";
	private boolean enabled = false;
	/**
	 * token
	 */
	private String token;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
