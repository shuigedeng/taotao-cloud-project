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

package com.taotao.cloud.auth.biz.authentication.login.extension.wechatminiapp;

import com.taotao.cloud.auth.biz.authentication.login.extension.AbstractExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.ExtensionLoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.JsonExtensionLoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.extension.wechatminiapp.service.WechatMiniAppClientService;
import com.taotao.cloud.auth.biz.authentication.login.extension.wechatminiapp.service.WechatMiniAppSessionKeyCacheService;
import com.taotao.cloud.auth.biz.authentication.login.extension.wechatminiapp.service.WechatMiniAppUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.token.JwtTokenGenerator;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class WechatMiniAppExtensionLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractExtensionLoginFilterConfigurer<
                H,
                WechatMiniAppExtensionLoginFilterConfigurer<H>,
                WechatMiniAppAuthenticationFilter,
                ExtensionLoginFilterSecurityConfigurer<H>> {

    private WechatMiniAppUserDetailsService wechatMiniAppUserDetailsService;

    private JwtTokenGenerator jwtTokenGenerator;

    private WechatMiniAppClientService wechatMiniAppClientService;

    private WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService;

    public WechatMiniAppExtensionLoginFilterConfigurer(ExtensionLoginFilterSecurityConfigurer<H> securityConfigurer) {
        super(securityConfigurer, new WechatMiniAppAuthenticationFilter(), "/login/miniapp");
    }

    public WechatMiniAppExtensionLoginFilterConfigurer<H> miniAppUserDetailsService(
            WechatMiniAppUserDetailsService wechatMiniAppUserDetailsService) {
        this.wechatMiniAppUserDetailsService = wechatMiniAppUserDetailsService;
        return this;
    }

    public WechatMiniAppExtensionLoginFilterConfigurer<H> miniAppClientService(
            WechatMiniAppClientService wechatMiniAppClientService) {
        this.wechatMiniAppClientService = wechatMiniAppClientService;
        return this;
    }

    public WechatMiniAppExtensionLoginFilterConfigurer<H> miniAppSessionKeyCacheService(
            WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService) {
        this.wechatMiniAppSessionKeyCacheService = wechatMiniAppSessionKeyCacheService;
        return this;
    }

    public WechatMiniAppExtensionLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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

        WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService =
                this.wechatMiniAppSessionKeyCacheService != null
                        ? this.wechatMiniAppSessionKeyCacheService
                        : getBeanOrNull(applicationContext, WechatMiniAppSessionKeyCacheService.class);

        WechatMiniAppPreAuthenticationFilter wechatMiniAppPreAuthenticationFilter =
                new WechatMiniAppPreAuthenticationFilter(
                        wechatMiniAppClientService, wechatMiniAppSessionKeyCacheService);
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

        WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService =
                this.wechatMiniAppSessionKeyCacheService != null
                        ? this.wechatMiniAppSessionKeyCacheService
                        : getBeanOrNull(applicationContext, WechatMiniAppSessionKeyCacheService.class);

        return new WechatMiniAppAuthenticationProvider(
                wechatMiniAppUserDetailsService, wechatMiniAppSessionKeyCacheService);
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
