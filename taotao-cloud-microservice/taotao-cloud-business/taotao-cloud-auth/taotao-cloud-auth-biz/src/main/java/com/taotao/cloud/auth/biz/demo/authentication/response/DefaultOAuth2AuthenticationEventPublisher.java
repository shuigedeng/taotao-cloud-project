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

package com.taotao.cloud.auth.biz.demo.authentication.response;

import cn.herodotus.engine.oauth2.core.constants.OAuth2ErrorCodes;
import cn.herodotus.engine.oauth2.core.exception.AccountEndpointLimitedException;
import cn.herodotus.engine.oauth2.core.exception.SessionExpiredException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * Description: 扩展的 DefaultAuthenticationEventPublisher 支持 OAuth2AuthenticationException 解析
 *
 * @author : gengwei.zheng
 * @date : 2022/7/9 13:47
 */
public class DefaultOAuth2AuthenticationEventPublisher extends DefaultAuthenticationEventPublisher {

    public DefaultOAuth2AuthenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        super.publishAuthenticationFailure(convert(exception), authentication);
    }

    private AuthenticationException convert(AuthenticationException exception) {
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException authenticationException = (OAuth2AuthenticationException) exception;
            OAuth2Error error = authenticationException.getError();

            switch (error.getErrorCode()) {
                case OAuth2ErrorCodes.ACCOUNT_EXPIRED:
                    return new AccountExpiredException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorCodes.CREDENTIALS_EXPIRED:
                    return new CredentialsExpiredException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorCodes.ACCOUNT_DISABLED:
                    return new DisabledException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorCodes.ACCOUNT_LOCKED:
                    return new LockedException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorCodes.ACCOUNT_ENDPOINT_LIMITED:
                    return new AccountEndpointLimitedException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorCodes.USERNAME_NOT_FOUND:
                    return new UsernameNotFoundException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorCodes.SESSION_EXPIRED:
                    return new SessionExpiredException(exception.getMessage(), exception.getCause());
                default:
                    return new BadCredentialsException(exception.getMessage(), exception.getCause());
            }
        }

        return exception;
    }
}
