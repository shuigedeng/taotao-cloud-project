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

package com.taotao.cloud.auth.biz.demo.authentication.form;

import cn.herodotus.engine.captcha.core.processor.CaptchaRendererFactory;
import cn.herodotus.engine.oauth2.authentication.properties.OAuth2UiProperties;
import cn.herodotus.engine.oauth2.authentication.response.OAuth2FormLoginAuthenticationFailureHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * Description: OAuth2 Form Login Configurer 使用此种方式，相当于额外增加了一种表单登录方式。因此对原有的
 * http.formlogin进行的配置，对当前此种方式的配置并不生效。
 *
 * @author : gengwei.zheng
 * @date : 2022/4/12 13:29
 * @see
 *     org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer
 */
public class OAuth2FormLoginConfigurer
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private static final Logger log = LoggerFactory.getLogger(OAuth2FormLoginConfigurer.class);

    private final UserDetailsService userDetailsService;
    private final OAuth2UiProperties uiProperties;
    private final CaptchaRendererFactory captchaRendererFactory;

    public OAuth2FormLoginConfigurer(
            UserDetailsService userDetailsService,
            OAuth2UiProperties uiProperties,
            CaptchaRendererFactory captchaRendererFactory) {
        this.userDetailsService = userDetailsService;
        this.uiProperties = uiProperties;
        this.captchaRendererFactory = captchaRendererFactory;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {

        AuthenticationManager authenticationManager =
                httpSecurity.getSharedObject(AuthenticationManager.class);
        SecurityContextRepository securityContextRepository =
                httpSecurity.getSharedObject(SecurityContextRepository.class);

        OAuth2FormLoginAuthenticationFilter filter =
                new OAuth2FormLoginAuthenticationFilter(authenticationManager);
        filter.setUsernameParameter(uiProperties.getUsernameParameter());
        filter.setPasswordParameter(uiProperties.getPasswordParameter());
        filter.setAuthenticationDetailsSource(
                new OAuth2FormLoginWebAuthenticationDetailSource(uiProperties));
        filter.setAuthenticationFailureHandler(
                new OAuth2FormLoginAuthenticationFailureHandler(
                        uiProperties.getFailureForwardUrl()));
        filter.setSecurityContextRepository(securityContextRepository);

        OAuth2FormLoginAuthenticationProvider provider =
                new OAuth2FormLoginAuthenticationProvider(captchaRendererFactory);
        provider.setUserDetailsService(userDetailsService);
        provider.setHideUserNotFoundExceptions(false);

        httpSecurity
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
