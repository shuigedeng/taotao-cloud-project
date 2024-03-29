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

package com.taotao.cloud.auth.application.login.extension.face;

import com.taotao.cloud.auth.application.login.extension.AbstractExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.application.login.extension.ExtensionLoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.application.login.extension.JsonExtensionLoginAuthenticationFailureHandler;
import com.taotao.cloud.auth.application.login.extension.JsonExtensionLoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.application.login.extension.face.service.FaceCheckService;
import com.taotao.cloud.auth.application.login.extension.face.service.FaceUserDetailsService;
import com.taotao.cloud.auth.infrastructure.oauth2.token.JwtTokenGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class FaceExtensionLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractExtensionLoginFilterConfigurer<
                H,
                FaceExtensionLoginFilterConfigurer<H>,
                FaceAuthenticationFilter,
                ExtensionLoginFilterSecurityConfigurer<H>> {

    private FaceUserDetailsService faceUserDetailsService;
    private FaceCheckService faceCheckService;
    private JwtTokenGenerator jwtTokenGenerator;

    public FaceExtensionLoginFilterConfigurer(ExtensionLoginFilterSecurityConfigurer<H> securityConfigurer) {
        super(securityConfigurer, new FaceAuthenticationFilter(), "/login/face");
    }

    public FaceExtensionLoginFilterConfigurer<H> faceUserDetailsService(FaceUserDetailsService faceUserDetailsService) {
        this.faceUserDetailsService = faceUserDetailsService;
        return this;
    }

    public FaceExtensionLoginFilterConfigurer<H> faceCheckService(FaceCheckService faceCheckService) {
        this.faceCheckService = faceCheckService;
        return this;
    }

    public FaceExtensionLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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

        FaceUserDetailsService captchaUserDetailsService = this.faceUserDetailsService != null
                ? this.faceUserDetailsService
                : getBeanOrNull(applicationContext, FaceUserDetailsService.class);
        Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService is required");

        FaceCheckService faceCheckService = this.faceUserDetailsService != null
                ? this.faceCheckService
                : getBeanOrNull(applicationContext, FaceCheckService.class);
        Assert.notNull(captchaUserDetailsService, "faceCheckService is required");

        return new FaceAuthenticationProvider(faceUserDetailsService, faceCheckService);
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
