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
package com.taotao.cloud.gateway.component;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.core.model.SecurityUser;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证成功处理类
 *
 * @author dengtao
 * @date 2020/4/29 22:10
 * @since v1.0
 */
public class Oauth2AuthSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        MultiValueMap<String, String> headerValues = new LinkedMultiValueMap(4);
        Object principal = authentication.getPrincipal();

        if (principal instanceof SecurityUser) {
            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            headerValues.add(CommonConstant.USER_ID_HEADER, String.valueOf(user.getUserId()));
            headerValues.add(CommonConstant.USER_HEADER, JSON.toJSONString(user));
            headerValues.add(CommonConstant.USER_NAME_HEADER, user.getUsername());
        }

        OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authentication;
        String clientId = oauth2Authentication.getOAuth2Request().getClientId();
        headerValues.add(CommonConstant.TENANT_HEADER, clientId);
        headerValues.add(CommonConstant.ROLE_HEADER, CollectionUtil.join(authentication.getAuthorities(), ","));

        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                .headers(h -> h.addAll(headerValues))
                .build();

        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
        return webFilterExchange.getChain().filter(build);
    }
}
