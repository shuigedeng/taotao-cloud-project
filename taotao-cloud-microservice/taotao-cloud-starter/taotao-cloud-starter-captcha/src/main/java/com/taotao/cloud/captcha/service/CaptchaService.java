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
package com.taotao.cloud.captcha.service;


import com.taotao.cloud.captcha.model.CaptchaVO;
import com.taotao.cloud.captcha.model.ResponseModel;
import java.util.Properties;

/**
 * 验证码服务接口
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:49
 */
public interface CaptchaService {

	/**
	 * 配置初始化
	 */
	void init(Properties config);

	/**
	 * 获取验证码
	 *
	 * @param captchaVO
	 */
	ResponseModel get(CaptchaVO captchaVO);

	/**
	 * 核对验证码(前端)
	 *
	 * @param captchaVO
	 */
	ResponseModel check(CaptchaVO captchaVO);

	/**
	 * 二次校验验证码(后端)
	 *
	 * @param captchaVO
	 */
	ResponseModel verification(CaptchaVO captchaVO);

	/***
	 * 验证码类型
	 * 通过java SPI机制，接入方可自定义实现类，实现新的验证类型
	 */
	String captchaType();

	/**
	 * 历史资源清除(过期的图片文件，生成的临时图片...)
	 *
	 * @param config 配置项 控制资源清理的粒度
	 */
	void destroy(Properties config);
}
