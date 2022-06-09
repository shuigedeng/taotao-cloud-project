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
package com.taotao.cloud.sms.common.repository;

import com.taotao.cloud.sms.common.model.VerificationCode;
import org.springframework.lang.Nullable;

/**
 * 验证码储存接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:49:20
 */
public interface VerificationCodeRepository {

	/**
	 * 根据手机号码查询验证码
	 *
	 * @param phone              手机号码
	 * @param identificationCode 识别码
	 * @return {@link VerificationCode }
	 * @since 2022-04-27 17:49:20
	 */
	@Nullable
	VerificationCode findOne(String phone, @Nullable String identificationCode);

	/**
	 * 保存验证码
	 *
	 * @param verificationCode 验证码
	 * @since 2022-04-27 17:49:20
	 */
	void save(VerificationCode verificationCode);

	/**
	 * 删除验证码
	 *
	 * @param phone              手机号码
	 * @param identificationCode 识别码
	 * @since 2022-04-27 17:49:20
	 */
	void delete(String phone, @Nullable String identificationCode);

}
