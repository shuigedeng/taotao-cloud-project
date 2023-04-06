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

package com.taotao.cloud.auth.biz.demo.starter.configuration;

import cn.herodotus.engine.captcha.core.processor.CaptchaRendererFactory;
import cn.herodotus.engine.oauth2.authentication.form.OAuth2FormLoginConfigurer;
import cn.herodotus.engine.oauth2.authentication.properties.OAuth2UiProperties;
import cn.herodotus.engine.oauth2.authentication.response.DefaultOAuth2AuthenticationEventPublisher;
import cn.herodotus.engine.oauth2.authorization.customizer.HerodotusAuthorizationManager;
import cn.herodotus.engine.oauth2.authorization.customizer.HerodotusTokenStrategyConfigurer;
import cn.herodotus.engine.oauth2.authorization.processor.SecurityMatcherConfigurer;
import cn.herodotus.engine.oauth2.core.definition.service.ClientDetailsService;
import cn.herodotus.engine.oauth2.core.definition.strategy.StrategyUserDetailsService;
import cn.herodotus.engine.oauth2.core.response.HerodotusAccessDeniedHandler;
import cn.herodotus.engine.oauth2.core.response.HerodotusAuthenticationEntryPoint;
import cn.herodotus.engine.oauth2.server.authentication.processor.HerodotusClientDetailsService;
import cn.herodotus.engine.oauth2.server.authentication.processor.HerodotusUserDetailsService;
import cn.herodotus.engine.oauth2.server.authentication.service.OAuth2ApplicationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Description: 默认安全配置
 *
 * @author : gengwei.zheng
 * @date : 2022/2/12 20:53
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DefaultSecurityConfiguration.class);

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity httpSecurity,
            UserDetailsService userDetailsService,
            CaptchaRendererFactory captchaRendererFactory,
            OAuth2UiProperties uiProperties,
            SecurityMatcherConfigurer securityMatcherConfigurer,
            HerodotusAuthorizationManager herodotusAuthorizationManager,
            HerodotusTokenStrategyConfigurer herodotusTokenStrategyConfigurer)
            throws Exception {

        log.debug("[Herodotus] |- Core [Default Security Filter Chain] Auto Configure.");
        // 禁用CSRF 开启跨域
        httpSecurity.csrf().disable().cors();

        httpSecurity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(securityMatcherConfigurer.getPermitAllArray())
                        .permitAll()
                        .requestMatchers(securityMatcherConfigurer.getStaticResourceArray())
                        .permitAll()
                        .requestMatchers(EndpointRequest.toAnyEndpoint())
                        .permitAll()
                        .anyRequest()
                        .access(herodotusAuthorizationManager))
                .formLogin(form -> {
                    form.loginPage(uiProperties.getLoginPageUrl())
                            .usernameParameter(uiProperties.getUsernameParameter())
                            .passwordParameter(uiProperties.getPasswordParameter());
                    if (StringUtils.isNotBlank(uiProperties.getFailureForwardUrl())) {
                        form.failureForwardUrl(uiProperties.getFailureForwardUrl());
                    }
                    if (StringUtils.isNotBlank(uiProperties.getSuccessForwardUrl())) {
                        form.successForwardUrl(uiProperties.getSuccessForwardUrl());
                    }
                })
                .sessionManagement(Customizer.withDefaults())
                .exceptionHandling()
                .authenticationEntryPoint(new HerodotusAuthenticationEntryPoint())
                .accessDeniedHandler(new HerodotusAccessDeniedHandler())
                .and()
                .oauth2ResourceServer(configurer -> herodotusTokenStrategyConfigurer.from(configurer))
                .apply(new OAuth2FormLoginConfigurer(userDetailsService, uiProperties, captchaRendererFactory));

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationContext applicationContext) {
        log.debug("[Herodotus] |- Bean [Authentication Event Publisher] Auto Configure.");
        return new DefaultOAuth2AuthenticationEventPublisher(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsService userDetailsService(StrategyUserDetailsService strategyUserDetailsService) {
        HerodotusUserDetailsService herodotusUserDetailsService =
                new HerodotusUserDetailsService(strategyUserDetailsService);
        log.debug("[Herodotus] |- Bean [Herodotus User Details Service] Auto Configure.");
        return herodotusUserDetailsService;
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientDetailsService clientDetailsService(OAuth2ApplicationService applicationService) {
        HerodotusClientDetailsService herodotusClientDetailsService =
                new HerodotusClientDetailsService(applicationService);
        log.debug("[Herodotus] |- Bean [Herodotus Client Details Service] Auto Configure.");
        return herodotusClientDetailsService;
    }
}
