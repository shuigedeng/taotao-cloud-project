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
// import cn.hutool.core.util.StrUtil;
// import com.taotao.cloud.security.exception.InvalidException;
// import com.taotao.cloud.security.token.OpenIdAuthenticationToken;
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
//  * OpenId认证过滤
//  *
//  * @author dengtao
//  * @date 2020/4/29 19:58
//  * @since v1.0
//  */
// public class OpenIdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//     public OpenIdAuthenticationFilter() {
//         super(new AntPathRequestMatcher("/oauth/token/openId", HttpMethod.GET.name()));
//     }
//
//     @Override
//     public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//         String openId = request.getParameter("openId");
//         if (StrUtil.isBlank(openId)) {
//             throw new InvalidException("openId不能为空");
//         }
//         OpenIdAuthenticationToken authRequest = new OpenIdAuthenticationToken(openId);
//         authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
//         return this.getAuthenticationManager().authenticate(authRequest);
//     }
//
// }
