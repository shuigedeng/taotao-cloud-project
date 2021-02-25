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
// import com.taotao.cloud.auth.biz.service.ISmsCodeService;
// import com.taotao.cloud.core.utils.AuthUtil;
// import com.taotao.cloud.security.exception.ValidateCodeException;
// import com.taotao.cloud.security.properties.SecurityProperties;
// import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.web.authentication.AuthenticationFailureHandler;
// import org.springframework.stereotype.Component;
// import org.springframework.util.AntPathMatcher;
// import org.springframework.web.filter.OncePerRequestFilter;
//
// import javax.servlet.FilterChain;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
//
// /**
//  * 手机过滤器
//  *
//  * @author dengtao
//  * @date 2020/4/29 20:27
//  * @since v1.0
//  */
// @Slf4j
// @Component
// @AllArgsConstructor
// public class MobileFilter extends OncePerRequestFilter {
//
//     private final ISmsCodeService smsCodeService;
//     private final AuthenticationFailureHandler authenticationFailureHandler;
//     private final SecurityProperties securityProperties;
//     private final AntPathMatcher pathMatcher = new AntPathMatcher();
//
//     /**
//      * 返回true代表不执行过滤器，false代表执行
//      */
//     @Override
//     protected boolean shouldNotFilter(HttpServletRequest request) {
//         // 登录提交的时候验证验证码
//         if (pathMatcher.match("/oauth/token/mobile", request.getRequestURI())) {
//             // 判断是否有不验证验证码的client
//             if (securityProperties.getCode().getIgnoreClientCode().length > 0) {
//                 try {
//                     final String[] clientInfos = AuthUtil.extractClient(request);
//                     String clientId = clientInfos[0];
//                     for (String client : securityProperties.getCode().getIgnoreClientCode()) {
//                         if (client.equals(clientId)) {
//                             return true;
//                         }
//                     }
//                 } catch (Exception e) {
//                     log.error("解析client信息失败", e);
//                 }
//             }
//         }
//         return true;
//     }
//
//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//         try {
//             // smsCodeService.validate(request);
//         } catch (ValidateCodeException e) {
//             authenticationFailureHandler.onAuthenticationFailure(request, response, e);
//             return;
//         }
//         filterChain.doFilter(request, response);
//     }
// }
