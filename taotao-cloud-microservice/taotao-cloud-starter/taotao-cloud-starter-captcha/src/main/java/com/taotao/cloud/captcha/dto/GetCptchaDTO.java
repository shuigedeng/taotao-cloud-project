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
 * GetCptchaDTO 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:37:43
 */
public class GetCptchaDTO {
	/**
	 * 验证码类型:(clickWord,blockPuzzle)
	 */
	private String captchaType;

	/**
	 * 客户端UI组件id,组件初始化时设置一次，UUID
	 */
	private String clientUid;


	public String getCaptchaType() {
		return captchaType;
	}

	public void setCaptchaType(String captchaType) {
		this.captchaType = captchaType;
	}

	public String getClientUid() {
		return clientUid;
	}

	public void setClientUid(String clientUid) {
		this.clientUid = clientUid;
	}
}
