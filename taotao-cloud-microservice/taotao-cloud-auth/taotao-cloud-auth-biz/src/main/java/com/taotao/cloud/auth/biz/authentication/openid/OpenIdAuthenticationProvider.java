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
// import com.taotao.cloud.security.exception.InvalidException;
// import com.taotao.cloud.security.service.IUserDetailsService;
// import com.taotao.cloud.security.token.OpenIdAuthenticationToken;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.userdetails.UserDetails;
//
// /**
//  * OpenId认证提供者
//  *
//  * @author dengtao
//  * @date 2020/4/29 21:24
//  * @since v1.0
//  */
// public class OpenIdAuthenticationProvider implements AuthenticationProvider {
//
//     private IUserDetailsService userDetailService;
//
//     @Override
//     public Authentication authenticate(Authentication authentication) {
//         OpenIdAuthenticationToken authenticationToken = (OpenIdAuthenticationToken) authentication;
//         String openId = (String) authenticationToken.getPrincipal();
//         UserDetails user = null;
//         if (user == null) {
//             throw new InvalidException("openId认证失败");
//         }
//         OpenIdAuthenticationToken authenticationResult = new OpenIdAuthenticationToken(user, user.getAuthorities());
//         authenticationResult.setDetails(authenticationToken.getDetails());
//         return authenticationResult;
//     }
//
//     @Override
//     public boolean supports(Class<?> authentication) {
//         return OpenIdAuthenticationToken.class.isAssignableFrom(authentication);
//     }
//
//     public IUserDetailsService getUserDetailService() {
//         return userDetailService;
//     }
//
//     public void setUserDetailService(IUserDetailsService userDetailService) {
//         this.userDetailService = userDetailService;
//     }
// }
