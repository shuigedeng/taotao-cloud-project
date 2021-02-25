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
package com.taotao.cloud.gateway.handler;

import cn.hutool.http.HttpStatus;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Result;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;

/**
 * Hystrix 降级处理
 *
 * @author dengtao
 * @since 2020/4/29 22:11
 * @version 1.0.0
 */
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {

    private static final int DEFAULT_PORT = 9700;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        Optional<Object> originalUris = serverRequest.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        Optional<InetSocketAddress> socketAddress = serverRequest.remoteAddress();

        originalUris.ifPresent(originalUri -> LogUtil.error("网关执行请求:{}失败,请求主机: {},请求数据:{} hystrix服务降级处理", null, originalUri,
                socketAddress.orElse(new InetSocketAddress(DEFAULT_PORT)).getHostString(), buildMessage(serverRequest)));

        return ServerResponse
                .status(HttpStatus.HTTP_OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Result.failed("服务异常,请稍后重试")));
    }

    private String buildMessage(ServerRequest request) {
        StringBuilder message = new StringBuilder("[");
        message.append(request.methodName());
        message.append(" ");
        message.append(request.uri());
        MultiValueMap<String, String> params = request.queryParams();
        Map<String, String> map = params.toSingleValueMap();
        if (map.size() > 0) {
            message.append(" 请求参数: ");
            String serialize = JsonUtil.toJSONString(message);
            message.append(serialize);
        }
        Object requestBody = request.exchange().getAttribute(CACHED_REQUEST_BODY_ATTR);
        if (Objects.nonNull(requestBody)) {
            message.append(" 请求body: ");
            message.append(requestBody.toString());
        }
        message.append("]");
        return message.toString();
    }
}
