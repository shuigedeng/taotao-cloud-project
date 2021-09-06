/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.captcha.dto;

/**
 * CheckCptchaVO 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:36:54
 */
public class CheckCptchaVO {

	/**
	 * 点坐标(base64加密传输)
	 */
	private String pointJson;

	/**
	 * UUID(每次请求的验证码唯一标识)
	 */
	private String token;

	/**
	 * 校验结果
	 */
	private boolean result;

	public String getPointJson() {
		return pointJson;
	}

	public void setPointJson(String pointJson) {
		this.pointJson = pointJson;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}
