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

package com.taotao.cloud.gateway.filter.global.rsa;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.taotao.boot.common.utils.secure.RSAUtils;

import java.lang.reflect.Field;
import java.net.URI;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// @Component
/**
 * RequestEncryptFilter
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class RequestEncryptFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter( ServerWebExchange exchange, GatewayFilterChain chain ) {

        log.info(
                "============================RequestEncryptFilter start===================================");

        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        // 请求方法
        HttpMethod method = request.getMethod();
        MediaType mediaType = request.getHeaders().getContentType();
        //        if(mediaType==null){
        //            return MonoUtils.commomError("MediaType 为 null ",exchange);
        //        }

        String sign = request.getHeaders().getFirst("encrypt");
        if (StringUtils.isEmpty(sign)) {
            log.info("不需要解密 ");
            return chain.filter(exchange);
        }
        log.info("需要解密数据 ");

        if (method == HttpMethod.GET) {
            // 1 修改请求参数,并获取请求参数
            try {
                updateRequestParam(exchange);
            } catch (Exception e) {
                return MonoUtils.invalidUrl(exchange);
            }
        }

        if (method != HttpMethod.POST) {
            log.info("非POST请求 不需要解密 ");
            return chain.filter(exchange);
        }
        // 2 获取请求体,修改请求体
        ServerRequest serverRequest =
                ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<String> modifiedBody =
                serverRequest
                        .bodyToMono(String.class)
                        .flatMap(
                                body -> {
                                    // 解密请求体
                                    String encrypt =
                                            RSAUtils.decrypt(body, RSAConstant.PRIVATE_KEY);
                                    return Mono.just(encrypt);
                                });

        // 3 创建BodyInserter修改请求体
        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter =
                BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        // 4 创建CachedBodyOutputMessage并且把请求param加入
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter
                .insert(outputMessage, new BodyInserterContext())
                .then(
                        Mono.defer(
                                () -> {
                                    ServerHttpRequestDecorator decorator =
                                            new ServerHttpRequestDecorator(exchange.getRequest()) {
                                                @Override
                                                public Flux<DataBuffer> getBody() {
                                                    return outputMessage.getBody();
                                                }
                                            };
                                    return chain.filter(
                                            exchange.mutate().request(decorator).build());
                                }));
    }

    /**
     * 修改前端传的参数
     */
    private void updateRequestParam( ServerWebExchange exchange )
            throws NoSuchFieldException, IllegalAccessException {
        ServerHttpRequest request = exchange.getRequest();
        // 请求链接
        URI uri = request.getURI();
        // 请求参数
        String query = uri.getQuery();
        // 判断是否有加密的参数 这里的约定是 param
        if (StringUtils.isNotBlank(query) && query.contains("param")) {
            String[] split = query.split("=");
            String paramValue = split[1];
            // 解密请求参数
            String param = RSAUtils.decrypt(paramValue, RSAConstant.PRIVATE_KEY);
            // 使用反射强行拿出 URI 的 query
            Field targetQuery = uri.getClass().getDeclaredField("query");
            // 授权
            targetQuery.setAccessible(true);
            // 重新设置参数
            targetQuery.set(uri, param);
        }
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
