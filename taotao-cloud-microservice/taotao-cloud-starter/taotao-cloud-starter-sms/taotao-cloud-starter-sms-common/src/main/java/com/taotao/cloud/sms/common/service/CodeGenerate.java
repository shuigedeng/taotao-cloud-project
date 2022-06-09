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
package com.taotao.cloud.sms.common.service;

/**
 * 验证码生成
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:49:26
 */
@FunctionalInterface
public interface CodeGenerate {

	/**
	 * 生成验证码
	 *
	 * @return {@link String }
	 * @since 2022-04-27 17:49:26
	 */
	String generate();
}
