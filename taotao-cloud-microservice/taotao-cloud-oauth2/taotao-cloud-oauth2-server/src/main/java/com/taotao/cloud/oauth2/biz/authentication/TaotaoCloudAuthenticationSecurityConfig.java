/*
 * Copyright 2002-2021 the original author or authors.
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
 package com.taotao.cloud.oauth2.biz.authentication;

 import com.taotao.cloud.oauth2.biz.service.impl.SmsCodeServiceImpl;
 import com.taotao.cloud.security.service.IUserDetailsService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
 import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.web.DefaultSecurityFilterChain;
 import org.springframework.security.web.authentication.AuthenticationFailureHandler;
 import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
 import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
 import org.springframework.stereotype.Component;

 /**
  * TaotaoCloudAuthentication认证安全配置
  *
  * @author dengtao
  * @since 2020/5/2 11:16
  * @version 1.0.0
  */
 @Component
 public class TaotaoCloudAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
     @Autowired
     private PasswordEncoder passwordEncoder;
     @Autowired
     private IUserDetailsService userDetailsService;
     @Autowired
     private SmsCodeServiceImpl smsCodeService;
     @Autowired
     private AuthenticationFailureHandler authenticationFailureHandler;
     @Autowired
     private AuthenticationSuccessHandler authenticationSuccessHandler;

     @Override
     public void configure(HttpSecurity http) throws Exception {
         super.configure(http);
         TaotaoCloudAuthenticationFilter taotaoCloudAuthenticationFilter = new TaotaoCloudAuthenticationFilter();

         taotaoCloudAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
         taotaoCloudAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
         taotaoCloudAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);

         TaotaoCloudAuthenticationProvider taotaoCloudAuthenticationProvider = new TaotaoCloudAuthenticationProvider();
         taotaoCloudAuthenticationProvider.setUserDetailService(userDetailsService);
         taotaoCloudAuthenticationProvider.setHideUserNotFoundExceptions(false);
         taotaoCloudAuthenticationProvider.setSmsCodeService(smsCodeService);
         taotaoCloudAuthenticationProvider.setPasswordEncoder(passwordEncoder);

         http.authenticationProvider(taotaoCloudAuthenticationProvider)
                 .addFilterAt(taotaoCloudAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
     }
 }
