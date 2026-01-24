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

import cn.hutool.core.util.StrUtil;
import com.taotao.boot.common.constant.CommonConstants;
import com.taotao.boot.common.utils.id.IdGeneratorUtils;
import com.taotao.boot.common.utils.servlet.TraceUtils;
import com.taotao.cloud.gateway.properties.FilterProperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 第四执行 生成日志链路追踪id
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:13
 */
@Component
@ConditionalOnProperty(
        prefix = FilterProperties.PREFIX,
        name = "trace",
        havingValue = "true",
        matchIfMissing = true)
public class TraceLogFilter implements GlobalFilter, Ordered {

    @Value("${ttcVersion:--}")
    private String ttcVersion;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = IdGeneratorUtils.getIdStr();
        TraceUtils.setTraceId(traceId);

        HttpHeaders headers = exchange.getRequest().getHeaders();
        String version = headers.getFirst(CommonConstants.TTC_REQUEST_VERSION);
        String tenantId = headers.getFirst(CommonConstants.TTC_TENANT_ID);

        ServerHttpRequest.Builder builder =
                exchange.getRequest()
                        .mutate()
                        .headers(h -> h.add(CommonConstants.TTC_TRACE_ID, traceId));
        if (StrUtil.isEmpty(version)) {
            builder.headers(h -> h.add(CommonConstants.TTC_REQUEST_VERSION, ttcVersion));
        }
        if (StrUtil.isEmpty(tenantId)) {
            builder.headers(
                    h ->
                            h.add(
                                    CommonConstants.TTC_TENANT_ID,
                                    CommonConstants.TTC_TENANT_ID_DEFAULT));
        }
        ServerHttpRequest serverHttpRequest = builder.build();

        ServerWebExchange serverWebExchange = exchange.mutate().request(serverHttpRequest).build();
        return chain.filter(serverWebExchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 4;
    }
}
