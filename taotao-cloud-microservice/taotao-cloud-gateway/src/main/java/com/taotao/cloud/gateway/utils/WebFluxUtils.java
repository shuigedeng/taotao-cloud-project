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

package com.taotao.cloud.gateway.utils;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.util.StringUtils.startsWithIgnoreCase;

//import com.taotao.cloud.gateway.filter.global.GlobalCacheRequestFilter;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.gateway.filter.global.GlobalCacheRequestFilter;
import com.taotao.boot.common.utils.lang.StringUtils;

import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

/**
 * WebFlux 工具类
 */
public class WebFluxUtils {

    /**
     * 获取原请求路径
     */
    public static String getOriginalRequestUrl(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        LinkedHashSet<URI> uris = exchange.getRequiredAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        URI requestUri = uris.stream().findFirst().orElse(request.getURI());
        return UriComponentsBuilder.fromPath(requestUri.getRawPath()).build().toUriString();
    }

    /**
     * 是否是Json请求
     *
     * @param exchange HTTP请求
     */
    public static boolean isJsonRequest(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        return startsWithIgnoreCase(header, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 读取request内的body
     *
     * 注意一个request只能读取一次 读取之后需要重新包装
     */
    public static String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        // 获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.readableByteCount());
            buffer.toByteBuffer(byteBuffer);
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }

    /**
     * 从缓存中读取request内的body
     *
     * 注意要求经过 {@link ServerWebExchangeUtils#cacheRequestBody(ServerWebExchange, Function)} 此方法创建缓存
     * 框架内已经使用 {@link GlobalCacheRequestFilter} 全局创建了body缓存
     *
     * @return body
     */
    public static String resolveBodyFromCacheRequest(ServerWebExchange exchange) {
        Object obj = exchange.getAttributes().get(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
        if (ObjUtil.isNull(obj)) {
            return null;
        }
        DataBuffer buffer = (DataBuffer) obj;
        ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.readableByteCount());
        buffer.toByteBuffer(byteBuffer);
        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
        return charBuffer.toString();
    }

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param value    响应内容
     * @return Mono<Void>
     */
    // public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value) {
    //    return webFluxResponseWriter(response, HttpStatus.OK, value, Result.FAIL);
    // }

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param code     响应状态码
     * @param value    响应内容
     * @return Mono<Void>
     */
    // public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value, int code) {
    //    return webFluxResponseWriter(response, HttpStatus.OK, value, code);
    // }

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param status   http状态码
     * @param code     响应状态码
     * @param value    响应内容
     * @return Mono<Void>
     */
    // public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, HttpStatus status, Object value, int
    // code) {
    //    return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, status, value, code);
    // }

    /**
     * 设置webflux模型响应
     *
     * @param response    ServerHttpResponse
     * @param contentType content-type
     * @param status      http状态码
     * @param code        响应状态码
     * @param value       响应内容
     * @return Mono<Void>
     */
    // public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, HttpStatus
    // status, Object value, int code) {
    //    response.setStatusCode(status);
    //    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
    //    R<?> result = R.fail(code, value.toString());
    //    DataBuffer dataBuffer = response.bufferFactory().wrap(JsonUtils.toJsonString(result).getBytes());
    //    return response.writeWith(Mono.just(dataBuffer));
    // }
}
