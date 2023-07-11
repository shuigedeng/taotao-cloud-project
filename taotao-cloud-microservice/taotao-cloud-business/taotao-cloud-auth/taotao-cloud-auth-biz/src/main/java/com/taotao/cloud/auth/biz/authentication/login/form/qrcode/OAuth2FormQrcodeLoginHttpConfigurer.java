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

package com.taotao.cloud.auth.biz.authentication.login.form.qrcode;

import com.taotao.cloud.auth.biz.authentication.login.form.OAuth2FormLoginAuthenticationFailureHandler;
import com.taotao.cloud.auth.biz.authentication.login.form.OAuth2FormLoginWebAuthenticationDetailSource;
import com.taotao.cloud.auth.biz.authentication.login.form.qrcode.service.impl.DefaultOAuth2FormQrcodeService;
import com.taotao.cloud.auth.biz.authentication.login.form.qrcode.service.impl.DefaultOAuth2FormQrcodeUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * <p>基于spring security 扩展表单登录方式(基于表单请求)</p>
 * <p>
 * 使用此种方式，相当于额外增加了一种表单登录方式。因此对原有的 http.formlogin进行的配置，对当前此种方式的配置并不生效。
 *
 * @date : 2022/4/12 13:29
 * @see org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer
 */
public class OAuth2FormQrcodeLoginHttpConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractHttpConfigurer<OAuth2FormQrcodeLoginHttpConfigurer<H>, H> {

    private static final Logger log = LoggerFactory.getLogger(OAuth2FormQrcodeLoginHttpConfigurer.class);

    private final OAuth2AuthenticationProperties authenticationProperties;

    public OAuth2FormQrcodeLoginHttpConfigurer(OAuth2AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public void configure(H httpSecurity) throws Exception {
        AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
        SecurityContextRepository securityContextRepository =
                httpSecurity.getSharedObject(SecurityContextRepository.class);

        OAuth2FormQrcodeAuthenticationFilter filter = new OAuth2FormQrcodeAuthenticationFilter(authenticationManager);
        filter.setAuthenticationDetailsSource(
                new OAuth2FormLoginWebAuthenticationDetailSource(authenticationProperties));

        filter.setAuthenticationFailureHandler(
                new OAuth2FormLoginAuthenticationFailureHandler(getFormLogin().getFailureForwardUrl()));
        filter.setSecurityContextRepository(securityContextRepository);

        OAuth2FormQrcodeAuthenticationProvider provider = new OAuth2FormQrcodeAuthenticationProvider(
                new DefaultOAuth2FormQrcodeService(), new DefaultOAuth2FormQrcodeUserDetailsService());

        httpSecurity
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    public H httpSecurity() {
        return getBuilder();
    }

    private OAuth2AuthenticationProperties.FormLogin getFormLogin() {
        return authenticationProperties.getFormLogin();
    }
}
