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

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.taotao.boot.common.constant.CommonConstants;
import com.taotao.boot.security.spring.core.userdetails.TtcUser;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录认证成功处理类 (网关目前不支持登录 现在此类无用)
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:10
 */
@Component
public class GatewayServerAuthenticationSuccessHandler
        implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(
            WebFilterExchange webFilterExchange, Authentication authentication) {
        //		UserDetails user = (UserDetails) authentication.getPrincipal();
        //
        //		Map<String, Object> tokenInfo = new HashMap<>();
        //		tokenInfo.put("USER_NAME", user.getUsername());
        //		tokenInfo.put("AUTHORITIES", user.getAuthorities());
        //
        //		ServerHttpResponse response = exchange.getExchange().getResponse();
        //		exchange.getExchange().getRequest().mutate().header("X-AUTHENTICATION-TOKEN",
        // JSONObject.toJSONString(tokenInfo));
        //
        //		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(tokenInfo,
        // HttpStatus.OK);
        //		return
        // response.writeWith(Mono.just(response.bufferFactory().wrap(JSON.toJSONBytes(responseEntity))));

        Map<String, String> headerValues = new HashMap<>(4);
        Object principal = authentication.getPrincipal();

        if (principal instanceof TtcUser) {
            TtcUser user = (TtcUser) authentication.getPrincipal();
            headerValues.put(CommonConstants.TTC_USER_ID_HEADER, String.valueOf(user.getUserId()));
            headerValues.put(CommonConstants.TTC_USER_HEADER, JSON.toJSONString(user));
            headerValues.put(CommonConstants.TTC_USER_NAME_HEADER, user.getUsername());
        }

        //        OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authentication;
        //        String clientId = oauth2Authentication.getOAuth2Request().getClientId();
        //        headerValues.add(CommonConstants.TTC_TENANT_ID, clientId);
        headerValues.put(
                CommonConstants.TTC_USER_ROLE_HEADER,
                CollUtil.join(authentication.getAuthorities(), ","));

        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpRequest serverHttpRequest =
                exchange.getRequest().mutate().headers(h -> {
					headerValues.forEach(h::add);
				}).build();

        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
        return webFilterExchange.getChain().filter(build);
    }
}
