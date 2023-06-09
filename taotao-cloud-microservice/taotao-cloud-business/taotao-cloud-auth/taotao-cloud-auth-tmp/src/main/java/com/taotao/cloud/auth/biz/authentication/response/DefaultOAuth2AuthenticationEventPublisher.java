/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
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
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.response;

import com.taotao.cloud.security.springsecurity.core.constants.OAuth2ErrorKeys;
import com.taotao.cloud.security.springsecurity.core.exception.AccountEndpointLimitedException;
import com.taotao.cloud.security.springsecurity.core.exception.SessionExpiredException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * <p>Description: 扩展的 DefaultAuthenticationEventPublisher </p>
 * <p>
 * 支持 OAuth2AuthenticationException 解析
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
        if (exception instanceof OAuth2AuthenticationException authenticationException) {
            OAuth2Error error = authenticationException.getError();

            return switch (error.getErrorCode()) {
                case OAuth2ErrorKeys.ACCOUNT_EXPIRED ->
                        new AccountExpiredException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.CREDENTIALS_EXPIRED ->
                        new CredentialsExpiredException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.ACCOUNT_DISABLED ->
                        new DisabledException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.ACCOUNT_LOCKED ->
                        new LockedException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.ACCOUNT_ENDPOINT_LIMITED ->
                        new AccountEndpointLimitedException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.USERNAME_NOT_FOUND ->
                        new UsernameNotFoundException(exception.getMessage(), exception.getCause());
                case OAuth2ErrorKeys.SESSION_EXPIRED ->
                        new SessionExpiredException(exception.getMessage(), exception.getCause());
                default -> new BadCredentialsException(exception.getMessage(), exception.getCause());
            };
        }

        return exception;
    }
}
