/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.oauth2.core.exception;

import cn.herodotus.engine.assistant.core.definition.exception.HerodotusException;
import cn.herodotus.engine.assistant.core.domain.Feedback;
import cn.herodotus.engine.assistant.core.domain.Result;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.Authentication;

/**
 * <p>Description: OAuth2 验证码基础 Exception </p>
 * <p>
 * 这里没有用基础定义的 PlatformAuthorizationException。主要问题是在自定义表单登录时，如果使用基础的 {@link org.springframework.security.core.AuthenticationException}，
 * 在 Spring Security 标准代码中该Exception将不会抛出，而是进行二次的用户验证，这将导致在验证过程中直接跳过验证码的校验。
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
