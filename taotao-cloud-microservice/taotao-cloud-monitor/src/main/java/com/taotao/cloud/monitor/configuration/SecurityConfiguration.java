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

package com.taotao.cloud.monitor.configuration;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * SecuritySecureConfig
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/12/01 10:00
 */
@Configuration
public class SecurityConfiguration {

    private final String adminContextPath;
    private final List<String> patterns;

    public SecurityConfiguration(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
        this.patterns =
                Arrays.asList(
                        this.adminContextPath + "/assets/**",
                        this.adminContextPath + "/login",
                        "/actuator/**",
                        "/actuator",
                        "/instances",
                        "/instances/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        return http.authorizeHttpRequests(
                        authorizeHttpRequestsCustomizer -> {
                            // 1.配置所有静态资源和登录页可以公开访问
                            authorizeHttpRequestsCustomizer
                                    .requestMatchers(toRequestMatchers(this.patterns))
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated();
                        })
                // 2.配置登录和登出路径
                .formLogin(
                        formLoginCustomizer -> {
                            formLoginCustomizer
                                    .loginPage(adminContextPath + "/login")
                                    .successHandler(successHandler);
                        })
                .logout(
                        logoutCustomizer -> {
                            logoutCustomizer.logoutUrl(adminContextPath + "/logout");
                        })
                // 3.开启http basic支持，admin-client注册时需要使用
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(
                        csrfCustomizer -> {
                            List<String> csrfPatterns =
                                    Arrays.asList(
                                            adminContextPath + "/instances",
                                            adminContextPath + "/actuator/**");
                            // 4.开启基于cookie的csrf保护
                            csrfCustomizer
                                    .csrfTokenRepository(
                                            CookieCsrfTokenRepository.withHttpOnlyFalse())
                                    // 5.忽略这些路径的csrf保护以便admin-client注册
                                    .ignoringRequestMatchers(toRequestMatchers(csrfPatterns));
                        })
                .rememberMe(
                        (rememberMe) ->
                                rememberMe
                                        .key(UUID.randomUUID().toString())
                                        .tokenValiditySeconds(1209600))
                .build();
    }

    public RequestMatcher[] toRequestMatchers(List<String> paths) {
        if (CollectionUtils.isNotEmpty(paths)) {
            List<PathPatternRequestMatcher> matchers =
                    paths.stream().map(PathPatternRequestMatcher.withDefaults()::matcher).toList();
            RequestMatcher[] result = new RequestMatcher[matchers.size()];
            return matchers.toArray(result);
        } else {
            return new RequestMatcher[] {};
        }
    }
    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //	SavedRequestAwareAuthenticationSuccessHandler successHandler = new
    // SavedRequestAwareAuthenticationSuccessHandler();
    //	successHandler.setTargetUrlParameter("redirectTo");
    //	successHandler.setDefaultTargetUrl(adminContextPath + "/");
    //	http.authorizeRequests()
    //		//1.配置所有静态资源和登录页可以公开访问
    //		.antMatchers(adminContextPath + "/assets/**").permitAll()
    //		.antMatchers(adminContextPath + "/login").permitAll()
    //		.antMatchers("/actuator/**").permitAll()
    //		.antMatchers("/actuator").permitAll()
    //		.antMatchers("/instances").permitAll()
    //		.antMatchers("/instances/**").permitAll()
    //		.anyRequest().authenticated()
    //		.and()
    //		//2.配置登录和登出路径
    //		.formLogin().loginPage(adminContextPath + "/login")
    //		.successHandler(successHandler)
    //		.and()
    //		.logout().logoutUrl(adminContextPath + "/logout").and()
    //		//3.开启http basic支持，admin-client注册时需要使用
    //		.httpBasic().and()
    //		.csrf()
    //		//4.开启基于cookie的csrf保护
    //		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    //		//5.忽略这些路径的csrf保护以便admin-client注册
    //		.ignoringAntMatchers(
    //			adminContextPath + "/instances",
    //			adminContextPath + "/actuator/**"
    //		);
    // }
}
