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
package com.taotao.cloud.sms.channel.chinamobile;

import com.taotao.cloud.sms.model.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 移动云短信配置
 *
 * @author shuigedeng
 */
@ConfigurationProperties(prefix = ChinaMobileProperties.PREFIX)
public class ChinaMobileProperties extends AbstractHandlerProperties<String> {

	public static final String PREFIX = "taotao.cloud.sms.chinamobile";
	private boolean enabled = false;
	/**
	 * 请求地址
	 */
	private String uri = "http://112.35.1.155:1992/sms/tmpsubmit";

	/**
	 * 企业名称
	 */
	private String ecName;

	/**
	 * 接口账号用户名
	 */
	private String apId;

	/**
	 * 接口账号密码
	 */
	private String secretKey;

	/**
	 * 签名编码。在模板短信控制台概览页获取。
	 */
	private String sign;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getEcName() {
		return ecName;
	}

	public void setEcName(String ecName) {
		this.ecName = ecName;
	}

	public String getApId() {
		return apId;
	}

	public void setApId(String apId) {
		this.apId = apId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
