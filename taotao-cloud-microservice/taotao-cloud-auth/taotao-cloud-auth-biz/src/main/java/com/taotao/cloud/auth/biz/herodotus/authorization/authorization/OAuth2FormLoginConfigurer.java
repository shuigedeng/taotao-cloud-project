/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.herodotus.authorization.authorization;

import cn.herodotus.engine.captcha.core.processor.CaptchaRendererFactory;
import cn.herodotus.engine.oauth2.authorization.properties.OAuth2UiProperties;
import cn.herodotus.engine.oauth2.authorization.authentication.OAuth2FormLoginAuthenticationFailureHandler;
import cn.herodotus.engine.oauth2.authorization.authentication.OAuth2FormLoginAuthenticationFilter;
import cn.herodotus.engine.oauth2.authorization.authentication.OAuth2FormLoginAuthenticationProvider;
import cn.herodotus.engine.oauth2.authorization.authentication.OAuth2FormLoginWebAuthenticationDetailSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>Description: OAuth2 Form Login Configurer </p>
 *
 * 使用此种方式，相当于额外增加了一种表单登录方式。因此对原有的 http.formlogin进行的配置，对当前此种方式的配置并不生效。
 *
 * @author : gengwei.zheng
 * @date : 2022/4/12 13:29
 * @see org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer
 */
public class OAuth2FormLoginConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private static final Logger log = LoggerFactory.getLogger(OAuth2FormLoginConfigurer.class);

    private final UserDetailsService userDetailsService;
    private final OAuth2UiProperties uiProperties;
    private final CaptchaRendererFactory captchaRendererFactory;

    public OAuth2FormLoginConfigurer(UserDetailsService userDetailsService, OAuth2UiProperties uiProperties, CaptchaRendererFactory captchaRendererFactory) {
        this.userDetailsService = userDetailsService;
        this.uiProperties = uiProperties;
        this.captchaRendererFactory = captchaRendererFactory;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {

        AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);

        OAuth2FormLoginAuthenticationFilter filter = new OAuth2FormLoginAuthenticationFilter(authenticationManager);
        filter.setUsernameParameter(uiProperties.getUsernameParameter());
        filter.setPasswordParameter(uiProperties.getPasswordParameter());
        filter.setAuthenticationDetailsSource(new OAuth2FormLoginWebAuthenticationDetailSource(uiProperties));
        filter.setAuthenticationFailureHandler(new OAuth2FormLoginAuthenticationFailureHandler(uiProperties.getFailureForwardUrl()));

        OAuth2FormLoginAuthenticationProvider provider = new OAuth2FormLoginAuthenticationProvider(captchaRendererFactory);
        provider.setUserDetailsService(userDetailsService);
        provider.setHideUserNotFoundExceptions(false);

        httpSecurity.authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
