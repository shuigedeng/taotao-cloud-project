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
 * @version 1.0.0
 * @since 2021/08/31 22:10
 */
public class GetCptchaVO {
	/**
	 * 原生图片base64
	 */
	private String originalImageBase64;

	/**
	 * 滑块图片base64
	 */
	private String jigsawImageBase64;

	/**
	 * UUID(每次请求的验证码唯一标识)
	 */
	private String token;

	private String secretKey;

	public String getOriginalImageBase64() {
		return originalImageBase64;
	}

	public void setOriginalImageBase64(String originalImageBase64) {
		this.originalImageBase64 = originalImageBase64;
	}

	public String getJigsawImageBase64() {
		return jigsawImageBase64;
	}

	public void setJigsawImageBase64(String jigsawImageBase64) {
		this.jigsawImageBase64 = jigsawImageBase64;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
