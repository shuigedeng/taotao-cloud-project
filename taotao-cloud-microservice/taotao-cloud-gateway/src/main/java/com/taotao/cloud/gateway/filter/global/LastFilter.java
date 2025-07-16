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

import static com.taotao.boot.common.constant.CommonConstants.TTC_TRACE_ID;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.servlet.ResponseUtils;
import com.taotao.boot.common.utils.servlet.TraceUtils;
import java.util.Objects;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 最后执行 生成日志链路追踪id
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:13
 */
@Component
public class LastFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        TraceUtils.removeTraceId();
        LocaleContextHolder.resetLocaleContext();

        return chain.filter(exchange)
                .then(
                        Mono.fromRunnable(
                                () -> {
                                    LogUtils.info("Response LastFilter 最终-----返回数据");

                                    ServerHttpResponse response = exchange.getResponse();
                                    HttpHeaders httpHeaders = response.getHeaders();
                                    ResponseUtils.addHeader(
                                            httpHeaders, "tid", TraceContext.traceId());

                                    Object traceId = exchange.getAttributes().get(TTC_TRACE_ID);
                                    if (Objects.nonNull(traceId)
                                            && traceId instanceof String trace) {
                                        TraceUtils.setTraceId(trace);
                                    }
                                }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
