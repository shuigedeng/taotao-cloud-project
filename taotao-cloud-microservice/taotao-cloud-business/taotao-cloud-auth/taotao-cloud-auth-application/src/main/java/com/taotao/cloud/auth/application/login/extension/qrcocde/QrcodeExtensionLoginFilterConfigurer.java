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

package com.taotao.cloud.auth.application.login.extension.qrcocde;

import com.taotao.cloud.auth.application.login.extension.AbstractExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.application.login.extension.ExtensionLoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.application.login.extension.JsonExtensionLoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.application.login.extension.qrcocde.service.QrcodeService;
import com.taotao.cloud.auth.application.login.extension.qrcocde.service.QrcodeUserDetailsService;
import com.taotao.cloud.auth.infrastructure.token.JwtTokenGenerator;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class QrcodeExtensionLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractExtensionLoginFilterConfigurer<
                H,
                QrcodeExtensionLoginFilterConfigurer<H>,
                QrcodeAuthenticationFilter,
                ExtensionLoginFilterSecurityConfigurer<H>> {

    private QrcodeUserDetailsService qrcodeUserDetailsService;
    private QrcodeService qrcodeService;
    private JwtTokenGenerator jwtTokenGenerator;

    public QrcodeExtensionLoginFilterConfigurer(ExtensionLoginFilterSecurityConfigurer<H> securityConfigurer) {
        super(securityConfigurer, new QrcodeAuthenticationFilter(), "/login/qrcode");
    }

    public QrcodeExtensionLoginFilterConfigurer<H> accountUserDetailsService(
            QrcodeUserDetailsService qrcodeUserDetailsService) {
        this.qrcodeUserDetailsService = qrcodeUserDetailsService;
        return this;
    }

    public QrcodeExtensionLoginFilterConfigurer<H> qrcodeService(QrcodeService qrcodeService) {
        this.qrcodeService = qrcodeService;
        return this;
    }

    public QrcodeExtensionLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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
        QrcodeUserDetailsService qrcodeUserDetailsService = this.qrcodeUserDetailsService != null
                ? this.qrcodeUserDetailsService
                : getBeanOrNull(applicationContext, QrcodeUserDetailsService.class);
        Assert.notNull(qrcodeUserDetailsService, "qrcodeUserDetailsService is required");

        QrcodeService qrcodeService = this.qrcodeService != null
                ? this.qrcodeService
                : getBeanOrNull(applicationContext, QrcodeService.class);
        Assert.notNull(qrcodeService, "captchaUserDetailsService is required");

        return new QrcodeAuthenticationProvider(qrcodeService, qrcodeUserDetailsService);
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
        return (request, response, authException) -> {
            LogUtils.error("用户认证失败", authException);
            ResponseUtils.fail(response, authException.getMessage());
        };
    }
}
