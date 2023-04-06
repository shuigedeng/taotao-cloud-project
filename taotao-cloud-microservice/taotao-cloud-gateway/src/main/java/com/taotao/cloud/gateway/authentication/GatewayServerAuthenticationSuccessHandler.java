/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.gateway.authentication;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.SecurityUser;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证成功处理类
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:10
 */
public class GatewayServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        MultiValueMap<String, String> headerValues = new LinkedMultiValueMap<>(4);
        Object principal = authentication.getPrincipal();

        if (principal instanceof SecurityUser) {
            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            headerValues.add(CommonConstant.TAOTAO_CLOUD_USER_ID_HEADER, String.valueOf(user.getUserId()));
            headerValues.add(CommonConstant.TAOTAO_CLOUD_USER_HEADER, JSON.toJSONString(user));
            headerValues.add(CommonConstant.TAOTAO_CLOUD_USER_NAME_HEADER, user.getUsername());
        }

        //        OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authentication;
        //        String clientId = oauth2Authentication.getOAuth2Request().getClientId();
        //        headerValues.add(CommonConstant.TAOTAO_CLOUD_TENANT_HEADER, clientId);
        headerValues.add(
                CommonConstant.TAOTAO_CLOUD_USER_ROLE_HEADER,
                CollectionUtil.join(authentication.getAuthorities(), ","));

        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpRequest serverHttpRequest = exchange.getRequest()
                .mutate()
                .headers(h -> h.addAll(headerValues))
                .build();

        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
        return webFilterExchange.getChain().filter(build);
    }
}
