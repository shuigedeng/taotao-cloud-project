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
package com.taotao.cloud.sms.service;

import org.springframework.lang.NonNull;

/**
 * SmsService
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:43:37
 */
public interface SmsService {

	/**
	 * 发送短消息
	 *
	 * @param phoneNumber   手机号
	 * @param signName      　签名
	 * @param templateCode  　模板Id
	 * @param templateParam JSON模板参数字符串
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-07 20:43:46
	 */
	String sendSms(@NonNull String phoneNumber, @NonNull String signName,
		@NonNull String templateCode, @NonNull String templateParam);

	/**
	 * 发送验证码
	 *
	 * @param digits 位数
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-07 20:43:58
	 */
	String sendRandCode(int digits);
}
