// /*
//  * Copyright 2017-2020 original authors
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  * https://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
// package com.taotao.cloud.auth.biz.authentication.openid;
//
// import com.taotao.cloud.security.service.impl.UserDetailsServiceImpl;
// import lombok.AllArgsConstructor;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.DefaultSecurityFilterChain;
// import org.springframework.security.web.authentication.AuthenticationFailureHandler;
// import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.stereotype.Component;
//
// /**
//  * OpenId认证安全配置
//  *
//  * @author dengtao
//  * @date 2020/5/2 11:16
//  * @since v1.0
//  */
// @Component
// @AllArgsConstructor
// public class OpenIdAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//     private final UserDetailsServiceImpl userDetailsService;
//     private final AuthenticationSuccessHandler authenticationSuccessHandler;
//     private final AuthenticationFailureHandler authenticationFailureHandler;
//
//     @Override
//     public void configure(HttpSecurity http) throws Exception {
//         super.configure(http);
//         OpenIdAuthenticationFilter openIdAuthenticationFilter = new OpenIdAuthenticationFilter();
//
//         openIdAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
//         openIdAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
//         openIdAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
//
//         OpenIdAuthenticationProvider openIdAuthenticationProvider = new OpenIdAuthenticationProvider();
//         openIdAuthenticationProvider.setUserDetailService(userDetailsService);
//
//         http.authenticationProvider(openIdAuthenticationProvider)
//                 .addFilterAfter(openIdAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//     }
// }
