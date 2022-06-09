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

import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * 验证码类型生成
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:49:05
 */
@FunctionalInterface
public interface VerificationCodeTypeGenerate {

	/**
	 * 根据电话号码和参数列表生成验证码类型
	 *
	 * @param phone  电话号码
	 * @param params 参数列表
	 * @return 验证码类型
	 */
	@Nullable
	String getType(String phone, Map<String, String> params);
}
