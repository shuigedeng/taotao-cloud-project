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

package com.taotao.cloud.auth.biz.authentication.authentication.oneClick;

import com.taotao.cloud.auth.biz.authentication.authentication.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.oneClick.service.OneClickUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.authentication.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.authentication.LoginAuthenticationSuccessHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class OneClickLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractLoginFilterConfigurer<
                H, OneClickLoginFilterConfigurer<H>, OneClickAuthenticationFilter, LoginFilterSecurityConfigurer<H>> {

    private OneClickUserDetailsService oneClickUserDetailsService;

    private JwtTokenGenerator jwtTokenGenerator;

    public OneClickLoginFilterConfigurer(LoginFilterSecurityConfigurer<H> securityConfigurer) {
        super(securityConfigurer, new OneClickAuthenticationFilter(), "/login/oneclick");
    }

    public OneClickLoginFilterConfigurer<H> oneClickUserDetailsService(
            OneClickUserDetailsService oneClickUserDetailsService) {
        this.oneClickUserDetailsService = oneClickUserDetailsService;
        return this;
    }

    public OneClickLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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

        OneClickUserDetailsService oneClickUserDetailsService = this.oneClickUserDetailsService != null
                ? this.oneClickUserDetailsService
                : getBeanOrNull(applicationContext, OneClickUserDetailsService.class);
        Assert.notNull(oneClickUserDetailsService, "oneClickUserDetailsService is required");

        return new OneClickAuthenticationProvider(oneClickUserDetailsService);
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