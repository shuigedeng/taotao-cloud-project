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
package com.taotao.cloud.captcha.service;

import com.taotao.cloud.captcha.model.Captcha;
import java.util.Properties;

/**
 * 验证码服务接口
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:55:54
 */
public interface CaptchaService {

	/**
	 * 配置初始化
	 *
	 * @param config config
	 * @since 2021-09-03 20:56:00
	 */
	void init(Properties config);

	/**
	 * 获取验证码
	 *
	 * @param captcha captcha
	 * @return {@link com.taotao.cloud.captcha.model.Captcha }
	 * @since 2021-09-03 20:56:03
	 */
	Captcha get(Captcha captcha);

	/**
	 * 核对验证码(前端)
	 *
	 * @param captcha captcha
	 * @return {@link com.taotao.cloud.captcha.model.Captcha }
	 * @since 2021-09-03 20:56:09
	 */
	Captcha check(Captcha captcha);

	/**
	 * 二次校验验证码(后端)
	 *
	 * @param captcha captcha
	 * @return {@link com.taotao.cloud.captcha.model.Captcha }
	 * @since 2021-09-03 20:56:14
	 */
	Captcha verification(Captcha captcha);

	/**
	 * 验证码类型 通过java SPI机制，接入方可自定义实现类，实现新的验证类型
	 *
	 * @return {@link java.lang.String }
	 * @since 2021-09-03 20:56:21
	 */
	String captchaType();

	/**
	 * 历史资源清除(过期的图片文件，生成的临时图片...)
	 *
	 * @param config config
	 * @since 2021-09-03 20:56:33
	 */
	void destroy(Properties config);
}
