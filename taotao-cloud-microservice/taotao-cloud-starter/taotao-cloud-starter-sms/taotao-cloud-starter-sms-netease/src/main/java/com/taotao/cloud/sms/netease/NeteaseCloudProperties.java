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
package com.taotao.cloud.sms.netease;

import com.taotao.cloud.sms.common.model.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 网易云信短信配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:51:30
 */
@RefreshScope
@ConfigurationProperties(prefix = NeteaseCloudProperties.PREFIX)
public class NeteaseCloudProperties extends AbstractHandlerProperties<String> {

	public static final String PREFIX = "taotao.cloud.sms.netease";
	private boolean enabled = false;
	/**
	 * appkey
	 */
	private String appKey;

	/**
	 * appSecret
	 */
	private String appSecret;

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

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
}
