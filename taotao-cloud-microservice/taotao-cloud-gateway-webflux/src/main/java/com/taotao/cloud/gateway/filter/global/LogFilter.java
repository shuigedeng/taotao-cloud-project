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

package com.taotao.cloud.gateway.filter.global;

import com.alibaba.fastjson.JSON;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.gateway.model.AccessRecord;
import com.taotao.cloud.gateway.properties.FilterProperties;
import com.taotao.cloud.gateway.service.VisitRecordService;
import java.util.List;
import lombok.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 第七执行 日志过滤器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-08 13:18:58
 */
@Component
@AllArgsConstructor
@ConditionalOnProperty(
        prefix = FilterProperties.PREFIX,
        name = "log",
        havingValue = "true",
        matchIfMissing = true)
public class LogFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";
    private static final List<HttpMessageReader<?>> messageReaders =
            HandlerStrategies.withDefaults().messageReaders();

    private final VisitRecordService visitRecordService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        // 请求方法
        HttpMethod method = request.getMethod();
        // 请求头
        HttpHeaders headers = request.getHeaders();
        // 设置startTime 用来计算响应的时间
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        // 构建日志记录
        AccessRecord accessRecord = visitRecordService.build(exchange);

        // 设置请求方法
        accessRecord.setMethod(method.name());
        if (method == HttpMethod.GET) {
            // 获取get请求参数
            MultiValueMap<String, String> formData = request.getQueryParams();
            if (!formData.isEmpty()) {
                // 保存请求参数
                accessRecord.setFormData(JSON.toJSONString(formData));
            }
        } else if (method == HttpMethod.POST) {
            MediaType contentType = headers.getContentType();
            if (contentType != null) {
                Mono<Void> voidMono = null;
                if (contentType.equals(MediaType.APPLICATION_JSON)) {
                    // JSON
                    voidMono = readBody(exchange, chain, accessRecord);
                }
                if (voidMono != null) {
                    // 计算请求时间
                    cacueConsumTime(exchange);

                    return voidMono;
                }
            }
        }

        visitRecordService.put(exchange, accessRecord);
        // 请求后执行保存
        return chain.filter(exchange).then(saveRecord(exchange));
    }

    private Mono<Void> saveRecord(ServerWebExchange exchange) {
        return Mono.fromRunnable(
                () -> {
                    cacueConsumTime(exchange);
                });
    }

    /**
     * 计算访问时间
     *
     * @param exchange
     */
    private void cacueConsumTime(ServerWebExchange exchange) {
        // 请求开始时设置的自定义属性标识
        Long startTime = exchange.getAttribute(START_TIME);
        long consumingTime = 0L;
        if (startTime != null) {
            consumingTime = System.currentTimeMillis() - startTime;
            LogUtils.info(
                    exchange.getRequest().getURI().getRawPath() + ": 耗时 " + consumingTime + "ms");
        }
        visitRecordService.add(exchange, consumingTime);
    }

    private Mono<Void> readBody(
            ServerWebExchange exchange, GatewayFilterChain chain, AccessRecord accessRecord) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(
                        dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);
                            Flux<DataBuffer> cachedFlux =
                                    Flux.defer(
                                            () -> {
                                                DataBuffer buffer =
                                                        exchange.getResponse()
                                                                .bufferFactory()
                                                                .wrap(bytes);
                                                DataBufferUtils.retain(buffer);
                                                return Mono.just(buffer);
                                            });

                            // 重写请求体,因为请求体数据只能被消费一次
                            ServerHttpRequest mutatedRequest =
                                    new ServerHttpRequestDecorator(exchange.getRequest()) {
                                        @Override
                                        public Flux<DataBuffer> getBody() {
                                            return cachedFlux;
                                        }
                                    };

                            ServerWebExchange mutatedExchange =
                                    exchange.mutate().request(mutatedRequest).build();

                            return ServerRequest.create(mutatedExchange, messageReaders)
                                    .bodyToMono(String.class)
                                    .doOnNext(
                                            objectValue -> {
                                                accessRecord.setBody(objectValue);
                                                visitRecordService.put(exchange, accessRecord);
                                            })
                                    .then(chain.filter(mutatedExchange));
                        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 7;
    }
}
