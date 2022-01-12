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
package com.taotao.cloud.sms.service;

/**
 * 验证码生成
 *
 * @author shuigedeng
 */
@FunctionalInterface
public interface CodeGenerate {

	/**
	 * 生成验证码
	 *
	 * @return 验证码
	 */
	String generate();
}
