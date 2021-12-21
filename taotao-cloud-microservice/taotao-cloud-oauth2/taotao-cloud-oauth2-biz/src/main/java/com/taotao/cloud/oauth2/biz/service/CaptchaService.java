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
package com.taotao.cloud.oauth2.biz.service;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.CaptchaUtil;
import com.taotao.cloud.common.utils.RequestUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.wf.captcha.ArithmeticCaptcha;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CaptchaService
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/12/21 20:33
 */
@Service
public class CaptchaService {

	public static final String NOT_CODE_NULL = "验证码不能为空";
	public static final String NOT_LEGAL = "验证码不合法";
	public static final String INVALID = "验证码已失效";
	public static final String ERROR = "验证码错误";

	public static final String PARAM_T = "t";
	public static final String PARAM_CODE = "code";

	@Autowired
	private RedisRepository redisRepository;

	public ArithmeticCaptcha getCaptcha(HttpServletRequest request) {
		ArithmeticCaptcha captcha = CaptchaUtil.getArithmeticCaptcha();
		String text = captcha.text();

		Map<String, String> params = RequestUtil.getAllRequestParam(request);
		String t = params.get(PARAM_T);

		redisRepository
			.setExpire(RedisConstant.CAPTCHA_KEY_PREFIX + t, text.toLowerCase(), 120);

		return captcha;
	}

	public Boolean checkCaptcha(HttpServletRequest request) {
		Map<String, String> params = RequestUtil.getAllRequestParam(request);
		String code = params.get(PARAM_CODE);
		String t = params.get(PARAM_T);

		if (StrUtil.isBlank(code)) {
			throw new BaseException(NOT_CODE_NULL);
		}
		String key = RedisConstant.CAPTCHA_KEY_PREFIX + t;
		if (!redisRepository.exists(key)) {
			throw new BaseException(NOT_LEGAL);
		}

		Object captcha = redisRepository.get(key);
		if (captcha == null) {
			throw new BaseException(INVALID);
		}
		if (!code.toLowerCase().equals(captcha)) {
			throw new BaseException(ERROR);
		}

		return true;
	}

}
