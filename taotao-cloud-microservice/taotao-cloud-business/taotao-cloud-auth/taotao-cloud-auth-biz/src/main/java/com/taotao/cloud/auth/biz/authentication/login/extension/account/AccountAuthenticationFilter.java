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

package com.taotao.cloud.auth.biz.authentication.login.extension.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

/**
 * 帐户验证过滤器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-06-29 13:07:20
 */
public class AccountAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/login/account", "POST");

    private Converter<HttpServletRequest, AccountAuthenticationToken> accountVerificationAuthenticationTokenConverter;

    private boolean postOnly = true;

    public AccountAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.accountVerificationAuthenticationTokenConverter = new AccountAuthenticationConverter();
    }

    public AccountAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.accountVerificationAuthenticationTokenConverter = new AccountAuthenticationConverter();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (this.postOnly && !HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        AccountAuthenticationToken accountAuthenticationToken =
                accountVerificationAuthenticationTokenConverter.convert(request);
        // Allow subclasses to set the "details" property
        setDetails(request, accountAuthenticationToken);

        return this.getAuthenticationManager().authenticate(accountAuthenticationToken);
    }

    protected void setDetails(HttpServletRequest request, AccountAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setConverter(Converter<HttpServletRequest, AccountAuthenticationToken> converter) {
        Assert.notNull(converter, "Converter must not be null");
        this.accountVerificationAuthenticationTokenConverter = converter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }
    /**
     * 重写该方法，避免在日志Debug级别会输出错误信息的问题。
     *
     * @param request  请求
     * @param response 响应
     * @param failed   失败内容
     * @throws IOException      IOException
     * @throws ServletException ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        getRememberMeServices().loginFail(request, response);
        getFailureHandler().onAuthenticationFailure(request, response, failed);
    }
}
