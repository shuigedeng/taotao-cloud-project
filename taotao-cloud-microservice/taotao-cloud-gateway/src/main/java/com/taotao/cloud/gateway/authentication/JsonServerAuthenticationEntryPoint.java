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

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.servlet.ResponseUtils;
import com.taotao.cloud.gateway.exception.InvalidTokenException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * json服务器身份验证入口点
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-05 11:30:47
 */
public class JsonServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        LogUtils.error(e, "user authentication error : {}", e.getMessage());

        if (e instanceof InvalidBearerTokenException) {
            return ResponseUtils.fail(exchange, "无效的token");
        }

        if (e instanceof InvalidTokenException) {
            return ResponseUtils.fail(exchange, e.getMessage());
        }

        return ResponseUtils.fail(exchange, ResultEnum.UNAUTHORIZED);
    }
}
