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

package com.taotao.cloud.auth.application.login.extension.captcha;

import com.taotao.cloud.auth.application.login.extension.AbstractExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.application.login.extension.ExtensionLoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.application.login.extension.JsonExtensionLoginAuthenticationFailureHandler;
import com.taotao.cloud.auth.application.login.extension.JsonExtensionLoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.application.login.extension.captcha.service.CaptchaCheckService;
import com.taotao.cloud.auth.application.login.extension.captcha.service.CaptchaUserDetailsService;
import com.taotao.cloud.auth.infrastructure.token.JwtTokenGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class CaptchaExtensionLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractExtensionLoginFilterConfigurer<
                H,
                CaptchaExtensionLoginFilterConfigurer<H>,
                CaptchaAuthenticationFilter,
                ExtensionLoginFilterSecurityConfigurer<H>> {

    private CaptchaUserDetailsService captchaUserDetailsService;

    private CaptchaCheckService captchaCheckService;

    private JwtTokenGenerator jwtTokenGenerator;

    public CaptchaExtensionLoginFilterConfigurer(ExtensionLoginFilterSecurityConfigurer<H> securityConfigurer) {
        super(securityConfigurer, new CaptchaAuthenticationFilter(), "/login/captcha");
    }

    public CaptchaExtensionLoginFilterConfigurer<H> captchaUserDetailsService(
            CaptchaUserDetailsService captchaUserDetailsService) {
        this.captchaUserDetailsService = captchaUserDetailsService;
        return this;
    }

    public CaptchaExtensionLoginFilterConfigurer<H> captchaCheckService(CaptchaCheckService captchaCheckService) {
        this.captchaCheckService = captchaCheckService;
        return this;
    }

    public CaptchaExtensionLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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

        CaptchaUserDetailsService captchaUserDetailsService = this.captchaUserDetailsService != null
                ? this.captchaUserDetailsService
                : getBeanOrNull(applicationContext, CaptchaUserDetailsService.class);
        Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService is required");

        CaptchaCheckService captchaCheckService = this.captchaCheckService != null
                ? this.captchaCheckService
                : getBeanOrNull(applicationContext, CaptchaCheckService.class);
        Assert.notNull(captchaCheckService, "captchaService is required");

        return new CaptchaAuthenticationProvider(captchaUserDetailsService, captchaCheckService);
    }

    @Override
    protected AuthenticationSuccessHandler defaultSuccessHandler(H http) {
        if (this.jwtTokenGenerator == null) {
            ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
            jwtTokenGenerator = getBeanOrNull(applicationContext, JwtTokenGenerator.class);
        }
        Assert.notNull(jwtTokenGenerator, "jwtTokenGenerator is required");
        return new JsonExtensionLoginAuthenticationSuccessHandler(jwtTokenGenerator);
    }

    @Override
    protected AuthenticationFailureHandler defaultFailureHandler(H http) {
        return new JsonExtensionLoginAuthenticationFailureHandler();
    }
}
