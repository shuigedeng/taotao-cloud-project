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
// import com.taotao.cloud.core.model.SecurityUser;
// import com.taotao.cloud.security.exception.InvalidException;
// import com.taotao.cloud.security.service.IUserDetailsService;
// import com.taotao.cloud.security.token.MobileAuthenticationToken;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.core.Authentication;
//
// import java.util.Objects;
//
// /**
//  * 手机认证过滤器提供者
//  *
//  * @author dengtao
//  * @date 2020/4/29 20:26
//  * @since v1.0
//  */
// public class MobileAuthenticationProvider implements AuthenticationProvider {
//
//     private IUserDetailsService userDetailService;
//
//     @Override
//     public Authentication authenticate(Authentication authentication) {
//         MobileAuthenticationToken authenticationToken = (MobileAuthenticationToken) authentication;
//         String mobile = (String) authenticationToken.getPrincipal();
//         SecurityUser securityUser = null;
//         if (Objects.isNull(securityUser)) {
//             throw new InvalidException("用户在系统中不存在");
//         }
//         MobileAuthenticationToken authenticationResult = new MobileAuthenticationToken(securityUser, securityUser.getAuthorities());
//         authenticationResult.setDetails(authenticationToken.getDetails());
//
//         return authenticationResult;
//     }
//
//     /**
//      * 只有Authentication为SmsCodeAuthenticationToken使用此Provider认证
//      *
//      * @param aClass class
//      * @return boolean
//      * @author dengtao
//      * @date 2020/5/14 21:30
//      */
//     @Override
//     public boolean supports(Class<?> aClass) {
//         return MobileAuthenticationToken.class.isAssignableFrom(aClass);
//     }
//
//     public IUserDetailsService getUserDetailService() {
//         return userDetailService;
//     }
//
//     public void setUserDetailService(IUserDetailsService userDetailService) {
//         this.userDetailService = userDetailService;
//     }
//
// }
