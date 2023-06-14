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

package com.taotao.cloud.auth.biz.authentication.authentication.accountVerification;

import com.taotao.cloud.auth.biz.authentication.authentication.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.accountVerification.service.AccountVerificationService;
import com.taotao.cloud.auth.biz.authentication.authentication.accountVerification.service.AccountVerificationUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.authentication.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.authentication.LoginAuthenticationSuccessHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class AccountVerificationLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractLoginFilterConfigurer<
                H,
                AccountVerificationLoginFilterConfigurer<H>,
        AccountVerificationAuthenticationFilter,
                LoginFilterSecurityConfigurer<H>> {

    private AccountVerificationUserDetailsService accountVerificationUserDetailsService;

    private AccountVerificationService accountVerificationService;

    private JwtTokenGenerator jwtTokenGenerator;

    public AccountVerificationLoginFilterConfigurer(LoginFilterSecurityConfigurer<H> securityConfigurer) {
        super(securityConfigurer, new AccountVerificationAuthenticationFilter(), "/login/account/verification");
    }

    public AccountVerificationLoginFilterConfigurer<H> accountVerificationUserDetailsService(
            AccountVerificationUserDetailsService accountVerificationUserDetailsService) {
        this.accountVerificationUserDetailsService = accountVerificationUserDetailsService;
        return this;
    }

    public AccountVerificationLoginFilterConfigurer<H> accountVerificationService(
            AccountVerificationService accountVerificationService) {
        this.accountVerificationService = accountVerificationService;
        return this;
    }

    public AccountVerificationLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        return this;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    @Override
    protected AuthenticationProvider authenticationProvider(H http) {
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        AccountVerificationUserDetailsService captchaUserDetailsService =
                this.accountVerificationUserDetailsService != null
                        ? this.accountVerificationUserDetailsService
                        : getBeanOrNull(applicationContext, AccountVerificationUserDetailsService.class);
        Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService is required");
        AccountVerificationService captchaService = this.accountVerificationService != null
                ? this.accountVerificationService
                : getBeanOrNull(applicationContext, AccountVerificationService.class);
        Assert.notNull(captchaService, "captchaService is required");
        return new AccountVerificationAuthenticationProvider(captchaUserDetailsService, captchaService);
    }

    @Override
    protected AuthenticationSuccessHandler defaultSuccessHandler(H http) {
        if (this.jwtTokenGenerator == null) {
            ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
            jwtTokenGenerator = getBeanOrNull(applicationContext, JwtTokenGenerator.class);
        }
        Assert.notNull(jwtTokenGenerator, "jwtTokenGenerator is required");
        return new LoginAuthenticationSuccessHandler(jwtTokenGenerator);
    }
}
