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

package com.taotao.cloud.auth.biz.service;

import org.dromara.hutoolcore.util.StrUtil;
import com.taotao.cloud.auth.biz.exception.CloudAuthenticationException;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.captcha.util.CaptchaUtils;
import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import com.wf.captcha.ArithmeticCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CaptchaService
 *
 * @author shuigedeng
 * @version 2022.03
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
        ArithmeticCaptcha captcha = CaptchaUtils.getArithmeticCaptcha();
        String text = captcha.text();

        Map<String, String> params = RequestUtils.getAllRequestParam(request);
        String t = params.get(PARAM_T);

        redisRepository.setExpire(RedisConstant.CAPTCHA_KEY_PREFIX + t, text.toLowerCase(), 120);

        return captcha;
    }

    public void checkCaptcha(String code, String t) {
        if (StrUtil.isBlank(code)) {
            throw CloudAuthenticationException.throwError(NOT_CODE_NULL);
        }
        String key = RedisConstant.CAPTCHA_KEY_PREFIX + t;
        if (!redisRepository.exists(key)) {
            throw CloudAuthenticationException.throwError(NOT_LEGAL);
        }

        Object captcha = redisRepository.get(key);
        if (captcha == null) {
            throw CloudAuthenticationException.throwError(INVALID);
        }
        if (!code.toLowerCase().equals(captcha)) {
            throw CloudAuthenticationException.throwError(ERROR);
        }
    }
}
