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
package com.taotao.cloud.sms.common.model;


/**
 * 验证信息
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:49:07
 */
public class VerifyInfo {

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 验证码
	 */
	private String code;

	/**
	 * 识别码
	 */
	private String identificationCode;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIdentificationCode() {
		return identificationCode;
	}

	public void setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode;
	}
}
