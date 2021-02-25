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
// package com.taotao.cloud.auth.biz.authentication.mobile;
//
// import com.taotao.cloud.security.service.IUserDetailsService;
// import org.springframework.beans.factory.annotation.Autowired;
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
//  * 手机认证安全配置
//  *
//  * @author dengtao
//  * @date 2020/4/29 20:26
//  * @since v1.0
//  */
// @Component
// public class MobileAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//
//     @Autowired
//     private IUserDetailsService userDetailsService;
//
//     @Autowired
//     private AuthenticationSuccessHandler authenticationSuccessHandler;
//
//     @Autowired
//     private AuthenticationFailureHandler authenticationFailureHandler;
//
//     @Override
//     public void configure(HttpSecurity http) throws Exception {
//         super.configure(http);
//         MobileAuthenticationFilter mobileAuthenticationFilter = new MobileAuthenticationFilter();
//         mobileAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
//         mobileAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
//         mobileAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
//
//         MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
//         mobileAuthenticationProvider.setUserDetailService(userDetailsService);
//
//         http.authenticationProvider(mobileAuthenticationProvider)
//                 .addFilterAfter(mobileAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//     }
// }
