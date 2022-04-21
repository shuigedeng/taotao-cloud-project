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
package com.taotao.cloud.sms.service;

import org.springframework.lang.Nullable;

/**
 * 手机验证码服务
 *
 * @author shuigedeng
 */
public interface VerificationCodeService {

	/**
	 * 验证码短信中验证码对应的key
	 */
	String MSG_KEY_CODE = "code";

	/**
	 * 验证码短信中识别码对应的key
	 */
	String MSG_KEY_IDENTIFICATION_CODE = "identificationCode";

	/**
	 * 验证码短信中有效期(秒)对应的key
	 */
	String MSG_KEY_EXPIRATION_TIME_OF_SECONDS = "expirationTimeOfSeconds";

	/**
	 * 验证码短信中有效期(分)对应的key
	 */
	String MSG_KEY_EXPIRATION_TIME_OF_MINUTES = "expirationTimeOfMinutes";

	/**
	 * 查询手机验证码
	 *
	 * @param phone              手机号
	 * @param identificationCode 识别码
	 * @return 手机验证码
	 */
	@Nullable
	String find(String phone, String identificationCode);

	/**
	 * 发送验证码
	 *
	 * @param phone 手机号码
	 */
	default void send(String phone) {
		send(phone, null);
	}

	/**
	 * 发送验证码
	 *
	 * @param phone 手机号码
	 * @param type  类型
	 */
	void send(String phone, @Nullable String type);

	/**
	 * 验证
	 *
	 * @param phone              手机号码
	 * @param code               验证码
	 * @param identificationCode 识别码
	 * @return 验证通过
	 */
	boolean verify(String phone, String code, @Nullable String identificationCode);
}
