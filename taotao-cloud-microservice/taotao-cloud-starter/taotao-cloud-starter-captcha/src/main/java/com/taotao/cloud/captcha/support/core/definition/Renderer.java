/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.captcha.support.core.definition;

import com.taotao.cloud.captcha.support.core.definition.domain.Metadata;
import com.taotao.cloud.captcha.support.core.dto.Captcha;
import com.taotao.cloud.captcha.support.core.dto.Verification;

/**
 * <p>Description: 基础绘制器定义 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:57:42
 */
public interface Renderer {

	/**
	 * 验证码绘制
	 *
	 * @return 绘制的验证码和校验信息 {@link Metadata}
	 */
	Metadata draw();

	/**
	 * 创建验证码
	 *
	 * @param key 验证码标识，用于标记在缓存中的存储
	 * @return 验证码数据 {@link Captcha}
	 */
	Captcha getCapcha(String key);

	/**
	 * 验证码校验
	 *
	 * @param verification 前端传入的验证值
	 * @return true 验证成功，返回错误信息
	 */
	boolean verify(Verification verification);

	/**
	 * 获取验证码类别
	 *
	 * @return 验证码类别
	 */
	String getCategory();
}
