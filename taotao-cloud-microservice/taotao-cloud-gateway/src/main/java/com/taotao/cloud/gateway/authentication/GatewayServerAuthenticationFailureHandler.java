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

import com.taotao.boot.common.utils.log.LogUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 登录认证失败处理类 (网关目前不支持登录 现在此类无用)
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:10
 */
@Component
public class GatewayServerAuthenticationFailureHandler
        implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(
            WebFilterExchange webFilterExchange, AuthenticationException exception) {
        LogUtils.error("认证失败", exception);

        //		ServerHttpResponse response = exchange.getExchange().getResponse();
        //
        //		Map<String, Object> responseBody = new HashMap<>(2);
        //		responseBody.put("ERROR_CODE", "000000");
        //		responseBody.put("ERROR_TYPE", exception.getClass().getName());
        //		responseBody.put("ERROR_MESSAGE", exception.getMessage());
        //		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(responseBody,
        // HttpStatus.INTERNAL_SERVER_ERROR);
        //
        //		response.setStatusCode(HttpStatus.FORBIDDEN);
        //		return
        // response.writeWith(Mono.just(response.bufferFactory().wrap(JSON.toJSONBytes(responseEntity))));

        return null;
    }
}
