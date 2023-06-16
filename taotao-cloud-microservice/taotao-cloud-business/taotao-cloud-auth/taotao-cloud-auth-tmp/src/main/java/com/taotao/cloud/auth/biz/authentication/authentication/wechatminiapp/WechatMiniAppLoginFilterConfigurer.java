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

package com.taotao.cloud.auth.biz.authentication.authentication.wechatminiapp;

import com.taotao.cloud.auth.biz.authentication.authentication.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.wechatminiapp.service.WechatMiniAppClientService;
import com.taotao.cloud.auth.biz.authentication.authentication.wechatminiapp.service.WechatMiniAppSessionKeyCacheService;
import com.taotao.cloud.auth.biz.authentication.authentication.wechatminiapp.service.WechatMiniAppUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.authentication.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.authentication.LoginAuthenticationSuccessHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class WechatMiniAppLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractLoginFilterConfigurer<
                H, WechatMiniAppLoginFilterConfigurer<H>, WechatMiniAppAuthenticationFilter, LoginFilterSecurityConfigurer<H>> {

    private WechatMiniAppUserDetailsService wechatMiniAppUserDetailsService;

    private JwtTokenGenerator jwtTokenGenerator;

    private WechatMiniAppClientService wechatMiniAppClientService;

    private WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService;

    public WechatMiniAppLoginFilterConfigurer(LoginFilterSecurityConfigurer<H> securityConfigurer) {
        super(securityConfigurer, new WechatMiniAppAuthenticationFilter(), "/login/miniapp");
    }

    public WechatMiniAppLoginFilterConfigurer<H> miniAppUserDetailsService(
            WechatMiniAppUserDetailsService wechatMiniAppUserDetailsService) {
        this.wechatMiniAppUserDetailsService = wechatMiniAppUserDetailsService;
        return this;
    }

    public WechatMiniAppLoginFilterConfigurer<H> miniAppClientService(WechatMiniAppClientService wechatMiniAppClientService) {
        this.wechatMiniAppClientService = wechatMiniAppClientService;
        return this;
    }

    public WechatMiniAppLoginFilterConfigurer<H> miniAppSessionKeyCacheService(
            WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService) {
        this.wechatMiniAppSessionKeyCacheService = wechatMiniAppSessionKeyCacheService;
        return this;
    }

    public WechatMiniAppLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        return this;
    }

    @Override
    public void configure(H http) throws Exception {
        super.configure(http);
        initPreAuthenticationFilter(http);
    }

    private void initPreAuthenticationFilter(H http) {
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        WechatMiniAppClientService wechatMiniAppClientService = this.wechatMiniAppClientService != null
                ? this.wechatMiniAppClientService
                : getBeanOrNull(applicationContext, WechatMiniAppClientService.class);

        WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService = this.wechatMiniAppSessionKeyCacheService != null
                ? this.wechatMiniAppSessionKeyCacheService
                : getBeanOrNull(applicationContext, WechatMiniAppSessionKeyCacheService.class);

        WechatMiniAppPreAuthenticationFilter wechatMiniAppPreAuthenticationFilter =
                new WechatMiniAppPreAuthenticationFilter(wechatMiniAppClientService, wechatMiniAppSessionKeyCacheService);
        http.addFilterBefore(postProcess(wechatMiniAppPreAuthenticationFilter), LogoutFilter.class);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    @Override
    protected AuthenticationProvider authenticationProvider(H http) {
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        WechatMiniAppUserDetailsService wechatMiniAppUserDetailsService = this.wechatMiniAppUserDetailsService != null
                ? this.wechatMiniAppUserDetailsService
                : getBeanOrNull(applicationContext, WechatMiniAppUserDetailsService.class);

        WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService = this.wechatMiniAppSessionKeyCacheService != null
                ? this.wechatMiniAppSessionKeyCacheService
                : getBeanOrNull(applicationContext, WechatMiniAppSessionKeyCacheService.class);

        return new WechatMiniAppAuthenticationProvider(wechatMiniAppUserDetailsService, wechatMiniAppSessionKeyCacheService);
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
