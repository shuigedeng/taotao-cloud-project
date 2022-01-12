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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 发送请求
 *
 * @author shuigedeng
 */
public class UpyunSendRequest {

	/**
	 * 手机号
	 */
	@JsonProperty("mobile")
	private String mobile;

	/**
	 * 模板编号
	 */
	@JsonProperty("template_id")
	private String templateId;

	/**
	 * 短信参数
	 */
	@JsonProperty("vars")
	private String vars;

	/**
	 * 拓展码
	 */
	@JsonProperty("extend_code")
	private String extendCode;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getVars() {
		return vars;
	}

	public void setVars(String vars) {
		this.vars = vars;
	}

	public String getExtendCode() {
		return extendCode;
	}

	public void setExtendCode(String extendCode) {
		this.extendCode = extendCode;
	}
}
