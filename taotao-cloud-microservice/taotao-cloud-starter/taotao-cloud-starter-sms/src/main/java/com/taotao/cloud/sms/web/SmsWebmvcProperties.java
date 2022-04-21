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
package com.taotao.cloud.sms.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 短信Web配置
 *
 * @author shuigedeng
 */
@ConfigurationProperties(prefix = SmsWebmvcProperties.PREFIX)
public class SmsWebmvcProperties {

    public static final String PREFIX = "taotao.cloud.sms.web";

	/**
	 * 默认基础路径
	 */
	public static final String DEFAULT_BASE_PATH = "/sms";

	/**
	 * 是否启用web端点
	 */
	private boolean enable = false;

	/**
	 * 基础路径
	 */
	private String basePath = DEFAULT_BASE_PATH;

	/**
	 * 是否启用验证码发送web端点
	 */
	private boolean enableSend = true;

	/**
	 * 是否启用验证码查询web端点
	 */
	private boolean enableGet = true;

	/**
	 * 是否启用验证码验证web端点
	 */
	private boolean enableVerify = true;

	/**
	 * 是否启用通知发送web端点
	 */
	private boolean enableNotice = true;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public boolean isEnableSend() {
		return enableSend;
	}

	public void setEnableSend(boolean enableSend) {
		this.enableSend = enableSend;
	}

	public boolean isEnableGet() {
		return enableGet;
	}

	public void setEnableGet(boolean enableGet) {
		this.enableGet = enableGet;
	}

	public boolean isEnableVerify() {
		return enableVerify;
	}

	public void setEnableVerify(boolean enableVerify) {
		this.enableVerify = enableVerify;
	}

	public boolean isEnableNotice() {
		return enableNotice;
	}

	public void setEnableNotice(boolean enableNotice) {
		this.enableNotice = enableNotice;
	}
}
