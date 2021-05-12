///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.gateway.authentication;
//
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
//import reactor.core.publisher.Mono;
//
///**
// * 自定义认证管理器
// *
// * @author dengtao
// * @since 2020/4/29 22:09
// * @version 1.0.0
// */
//public class AuthenticationManagerComponent implements ReactiveAuthenticationManager {
//    private static final String FAILURE = "token已失效";
//    private static final String EXPIRED = "token已过期";
//    private static final String FAILED = "用户认证失败";
//
//    private final TokenStore tokenStore;
//
//    public AuthenticationManagerComponent(TokenStore tokenStore) {
//        this.tokenStore = tokenStore;
//    }
//
//    @Override
//    public Mono<Authentication> authenticate(Authentication authentication) {
//        return Mono.justOrEmpty(authentication)
//                .filter(a -> a instanceof BearerTokenAuthenticationToken)
//                .cast(BearerTokenAuthenticationToken.class)
//                .map(BearerTokenAuthenticationToken::getToken)
//                .flatMap((accessTokenValue -> {
//                    OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
//                    if (accessToken == null) {
//                        return Mono.error(new InvalidTokenException(FAILURE));
//                    } else if (accessToken.isExpired()) {
//                        tokenStore.removeAccessToken(accessToken);
//                        return Mono.error(new InvalidTokenException(EXPIRED));
//                    }
//
//                    OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
//                    if (result == null) {
//                        return Mono.error(new InvalidTokenException(FAILED));
//                    }
//                    return Mono.just(result);
//                }))
//                .cast(Authentication.class);
//    }
//}
