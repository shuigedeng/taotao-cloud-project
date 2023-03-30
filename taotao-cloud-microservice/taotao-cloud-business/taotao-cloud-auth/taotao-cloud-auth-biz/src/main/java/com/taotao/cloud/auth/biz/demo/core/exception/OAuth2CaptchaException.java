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

package com.taotao.cloud.auth.biz.demo.core.exception;

import cn.herodotus.engine.assistant.core.definition.exception.HerodotusException;
import cn.herodotus.engine.assistant.core.domain.Feedback;
import cn.herodotus.engine.assistant.core.domain.Result;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.Authentication;

/**
 * Description: OAuth2 验证码基础 Exception
 *
 * <p>这里没有用基础定义的 PlatformAuthorizationException。主要问题是在自定义表单登录时，如果使用基础的 {@link
 * org.springframework.security.core.AuthenticationException}， 在 Spring Security
 * 标准代码中该Exception将不会抛出，而是进行二次的用户验证，这将导致在验证过程中直接跳过验证码的校验。
 *
 * @author : gengwei.zheng
 * @date : 2022/4/12 22:33
 * @see org.springframework.security.authentication.ProviderManager#authenticate(Authentication)
 */
public class OAuth2CaptchaException extends AccountStatusException implements HerodotusException {

    public OAuth2CaptchaException(String msg) {
        super(msg);
    }

    public OAuth2CaptchaException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public Feedback getFeedback() {
        return Feedback.ERROR;
    }

    @Override
    public Result<String> getResult() {
        Result<String> result = Result.failure();
        result.code(getFeedback().getCode());
        result.message(getFeedback().getMessage());
        result.status(getFeedback().getStatus());
        result.stackTrace(super.getStackTrace());
        result.detail(super.getMessage());
        return result;
    }
}
