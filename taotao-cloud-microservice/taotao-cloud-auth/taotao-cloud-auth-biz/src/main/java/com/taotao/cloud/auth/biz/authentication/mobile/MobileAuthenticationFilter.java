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
// import com.taotao.cloud.security.exception.InvalidException;
// import com.taotao.cloud.security.token.MobileAuthenticationToken;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
//
// /**
//  * 手机认证过滤器
//  * <p>
//  * 1.认证请求的方法必须为POST
//  * 2.从request中获取手机号
//  * 3.封装成自己的Authentication的实现类SmsCodeAuthenticationToken（未认证）
//  * 4.调用 AuthenticationManager 的 authenticate 方法进行验证（即MobileAuthenticationProvider
//  *
//  * @author dengtao
//  * @date 2020/4/29 20:23
//  * @since v1.0
//  */
// public class MobileAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//     /**
//      * 处理的手机验证码登录请求处理url
//      */
//     MobileAuthenticationFilter() {
//         super(new AntPathRequestMatcher("/oauth/token/mobile", HttpMethod.GET.toString()));
//     }
//
//     @Override
//     public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//         String mobile = request.getParameter("mobile");
//         if (mobile == null) {
//             throw new InvalidException("手机号码不能为空");
//         }
//         mobile = mobile.trim();
//         MobileAuthenticationToken mobileToken = new MobileAuthenticationToken(mobile);
//         mobileToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
//         return this.getAuthenticationManager().authenticate(mobileToken);
//     }
// }
