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
// package com.taotao.cloud.security.component;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.authentication.AbstractAuthenticationToken;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.oauth2.provider.ClientDetails;
// import org.springframework.security.oauth2.provider.ClientDetailsService;
// import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
// import org.springframework.security.web.AuthenticationEntryPoint;
//
// /**
//  * Description: 自定义的 {@link ClientCredentialsTokenEndpointFilter}<br>
//  * Details: 为了使用自定义的 {@link AuthenticationEntryPoint}, 从而自定义发生异常时的响应格式
//  *
//  * @author dengtao
//  * @date 2020/9/29 16:08
//  * @since v1.0
//  */
// @Slf4j
// public class CustomClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {
//
//     public CustomClientCredentialsTokenEndpointFilter(
//             PasswordEncoder passwordEncoder,
//             ClientDetailsService clientDetailsService,
//             AuthenticationEntryPoint authenticationEntryPoint) {
//         super.setAllowOnlyPost(true);
//         super.setAuthenticationEntryPoint(authenticationEntryPoint);
//         super.setAuthenticationManager(new ClientAuthenticationManager(passwordEncoder, clientDetailsService));
//
//         this.postProcess();
//     }
//
//     private void postProcess() {
//         super.afterPropertiesSet();
//     }
//
//     private static class ClientAuthenticationManager implements AuthenticationManager {
//
//         private final PasswordEncoder passwordEncoder;
//
//         private final ClientDetailsService clientDetailsService;
//
//         public ClientAuthenticationManager(PasswordEncoder passwordEncoder, ClientDetailsService clientDetailsService) {
//             this.passwordEncoder = passwordEncoder;
//             this.clientDetailsService = clientDetailsService;
//         }
//
//         /**
//          * @param authentication {"authenticated":false,"authorities":[],"credentials":"client-a-p","name":"client-a","principal":"client-a"}
//          * @see AuthenticationManager#authenticate(Authentication)
//          */
//         @Override
//         public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//             final String clientId = authentication.getName();
//             final ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
//
//             if (!passwordEncoder.matches((CharSequence) authentication.getCredentials(), clientDetails.getClientSecret())) {
//                 throw new BadCredentialsException("客户端密码错误!");
//             }
//
//             return new ClientAuthenticationToken(clientDetails);
//         }
//     }
//
//     private static class ClientAuthenticationToken extends AbstractAuthenticationToken {
//
//         private final Object principal;
//         private final Object credentials;
//
//         public ClientAuthenticationToken(ClientDetails clientDetails) {
//             super(clientDetails.getAuthorities());
//             this.principal = clientDetails.getClientId();
//             this.credentials = clientDetails.getClientSecret();
//             super.setAuthenticated(true);
//         }
//
//         @Override
//         public Object getCredentials() {
//             return credentials;
//         }
//
//         @Override
//         public Object getPrincipal() {
//             return principal;
//         }
//     }
// }
