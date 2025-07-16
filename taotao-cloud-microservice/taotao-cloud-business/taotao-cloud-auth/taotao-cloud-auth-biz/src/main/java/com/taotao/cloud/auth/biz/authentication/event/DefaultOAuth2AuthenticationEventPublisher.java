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

package com.taotao.cloud.auth.biz.authentication.event;

import com.taotao.boot.security.spring.constants.OAuth2ErrorKeys;
import com.taotao.boot.security.spring.exception.AccountEndpointLimitedException;
import com.taotao.boot.security.spring.exception.SessionExpiredException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * <p>扩展的 DefaultAuthenticationEventPublisher </p>
 * <p>
 * 支持 OAuth2AuthenticationException 解析
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:24:34
 */
public class DefaultOAuth2AuthenticationEventPublisher extends DefaultAuthenticationEventPublisher {

    /**
     * 默认oauth2身份验证事件发布者
     *
     * @param applicationEventPublisher 应用程序事件发布者
     * @return
     * @since 2023-07-10 17:24:35
     */
    public DefaultOAuth2AuthenticationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }

    /**
     * 发布身份验证失败
     *
     * @param exception      异常
     * @param authentication 身份验证
     * @since 2023-07-10 17:24:35
     */
    @Override
    public void publishAuthenticationFailure(
            AuthenticationException exception, Authentication authentication) {
        super.publishAuthenticationFailure(convert(exception), authentication);
    }

    /**
     * 转换
     *
     * @param exception 异常
     * @return {@link AuthenticationException }
     * @since 2023-07-10 17:24:35
     */
    private AuthenticationException convert(AuthenticationException exception) {
        if (exception instanceof OAuth2AuthenticationException authenticationException) {
            OAuth2Error error = authenticationException.getError();

            return switch (error.getErrorCode()) {
                case OAuth2ErrorKeys.ACCOUNT_EXPIRED ->
                        new AccountExpiredException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.CREDENTIALS_EXPIRED ->
                        new CredentialsExpiredException(
                                exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.ACCOUNT_DISABLED ->
                        new DisabledException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.ACCOUNT_LOCKED ->
                        new LockedException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.ACCOUNT_ENDPOINT_LIMITED ->
                        new AccountEndpointLimitedException(
                                exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.USERNAME_NOT_FOUND ->
                        new UsernameNotFoundException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.SESSION_EXPIRED ->
                        new SessionExpiredException(exception.getMessage(), exception.getCause());
                default ->
                        new BadCredentialsException(exception.getMessage(), exception.getCause());
            };
        }

        return exception;
    }
}
