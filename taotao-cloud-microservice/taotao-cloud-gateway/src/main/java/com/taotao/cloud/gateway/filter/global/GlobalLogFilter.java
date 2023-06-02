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

import org.dromara.hutoolcore.map.MapUtil;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.gateway.utils.WebFluxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局日志过滤器
 * <p>
 * 用于打印请求执行参数与响应时间等等
 */
@Slf4j
@Component
public class GlobalLogFilter implements GlobalFilter, Ordered {

    // @Autowired
    // private CustomGatewayProperties customGatewayProperties;

    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // if (!customGatewayProperties.getRequestLog()) {
        //    return chain.filter(exchange);
        // }

        ServerHttpRequest request = exchange.getRequest();
        String path = WebFluxUtils.getOriginalRequestUrl(exchange);
        String url = request.getMethod().name() + " " + path;

        // 打印请求参数
        if (WebFluxUtils.isJsonRequest(exchange)) {
            String jsonParam = WebFluxUtils.resolveBodyFromCacheRequest(exchange);
            log.debug("[PLUS]开始请求 => URL[{}],参数类型[json],参数:[{}]", url, jsonParam);
        } else {
            MultiValueMap<String, String> parameterMap = request.getQueryParams();
            if (MapUtil.isNotEmpty(parameterMap)) {
                String parameters = JsonUtils.toJSONString(parameterMap);
                log.debug("[PLUS]开始请求 => URL[{}],参数类型[param],参数:[{}]", url, parameters);
            } else {
                log.debug("[PLUS]开始请求 => URL[{}],无参数", url);
            }
        }

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                long executeTime = (System.currentTimeMillis() - startTime);
                log.debug("[PLUS]结束请求 => URL[{}],耗时:[{}]毫秒", url, executeTime);
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
