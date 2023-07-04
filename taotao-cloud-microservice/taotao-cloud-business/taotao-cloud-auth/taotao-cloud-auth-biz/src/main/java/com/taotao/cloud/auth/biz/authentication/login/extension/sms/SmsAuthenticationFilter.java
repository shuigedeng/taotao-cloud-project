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

package com.taotao.cloud.auth.biz.authentication.login.extension.sms;

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

public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SPRING_SECURITY_FORM_PHONE_KEY = "phone";

    public static final String SPRING_SECURITY_FORM_CAPTCHA_KEY = "captcha";

    public static final String SPRING_SECURITY_FORM_TYPE_KEY = "type";

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/login/phone", "POST");

    private String phoneParameter = SPRING_SECURITY_FORM_PHONE_KEY;
    private String captchaParameter = SPRING_SECURITY_FORM_CAPTCHA_KEY;
    private String typeParameter = SPRING_SECURITY_FORM_TYPE_KEY;

    private Converter<HttpServletRequest, SmsAuthenticationToken> captchaAuthenticationTokenConverter;

    private boolean postOnly = true;

    public SmsAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.captchaAuthenticationTokenConverter = defaultConverter();
    }

    public SmsAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.captchaAuthenticationTokenConverter = defaultConverter();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (this.postOnly && !HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        SmsAuthenticationToken authRequest = captchaAuthenticationTokenConverter.convert(request);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private Converter<HttpServletRequest, SmsAuthenticationToken> defaultConverter() {
        return request -> {
            String phone = request.getParameter(this.phoneParameter);
            phone = (phone != null) ? phone.trim() : "";

            String captcha = request.getParameter(this.captchaParameter);
            captcha = (captcha != null) ? captcha.trim() : "";

            String type = request.getParameter(this.typeParameter);
            type = (type != null) ? type.trim() : "";

            return new SmsAuthenticationToken(phone, captcha, type);
        };
    }

    protected void setDetails(HttpServletRequest request, SmsAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setPhoneParameter(String phoneParameter) {
        Assert.hasText(phoneParameter, "phoneParameter must not be empty or null");
        this.phoneParameter = phoneParameter;
    }

    public void setCaptchaParameter(String captchaParameter) {
        Assert.hasText(captchaParameter, "Password parameter must not be empty or null");
        this.captchaParameter = captchaParameter;
    }

    public void setConverter(Converter<HttpServletRequest, SmsAuthenticationToken> converter) {
        Assert.notNull(converter, "Converter must not be null");
        this.captchaAuthenticationTokenConverter = converter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getPhoneParameter() {
        return this.phoneParameter;
    }

    public final String getCaptchaParameter() {
        return this.captchaParameter;
    }
}
