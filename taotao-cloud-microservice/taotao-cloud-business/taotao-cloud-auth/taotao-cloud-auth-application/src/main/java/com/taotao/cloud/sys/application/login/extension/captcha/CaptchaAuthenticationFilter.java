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

package com.taotao.cloud.auth.biz.authentication.login.extension.captcha;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

public class CaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/login/captcha", "POST");

    private Converter<HttpServletRequest, CaptchaAuthenticationToken> accountVerificationAuthenticationTokenConverter;

    private boolean postOnly = true;

    public CaptchaAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.accountVerificationAuthenticationTokenConverter = new CaptchaAuthenticationConverter();
    }

    public CaptchaAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.accountVerificationAuthenticationTokenConverter = new CaptchaAuthenticationConverter();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (this.postOnly && !HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        CaptchaAuthenticationToken captchaAuthenticationToken =
                accountVerificationAuthenticationTokenConverter.convert(request);
        // Allow subclasses to set the "details" property
        setDetails(request, captchaAuthenticationToken);
        return this.getAuthenticationManager().authenticate(captchaAuthenticationToken);
    }

    protected void setDetails(HttpServletRequest request, CaptchaAuthenticationToken captchaAuthenticationToken) {
        captchaAuthenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setConverter(Converter<HttpServletRequest, CaptchaAuthenticationToken> converter) {
        Assert.notNull(converter, "Converter must not be null");
        this.accountVerificationAuthenticationTokenConverter = converter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }
}
