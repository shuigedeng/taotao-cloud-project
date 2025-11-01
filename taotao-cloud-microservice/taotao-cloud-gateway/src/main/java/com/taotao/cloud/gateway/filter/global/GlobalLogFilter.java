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

import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.gateway.properties.FilterProperties;
import com.taotao.cloud.gateway.utils.WebFluxUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 第六执行 全局日志过滤器
 * <p>
 * 用于打印请求执行参数与响应时间等等
 *
 * @author shuigedeng
 * @version 2023.07
 * @see GlobalFilter
 * @see Ordered
 * @since 2023-08-17 11:41:33
 */
@Component
@ConditionalOnProperty(
        prefix = FilterProperties.PREFIX,
        name = "globalLog",
        havingValue = "true",
        matchIfMissing = true)
public class GlobalLogFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestUrl = exchange.getRequest().getURI().getRawPath();
        // String path = WebFluxUtils.getOriginalRequestUrl(exchange);
        String url = request.getMethod().name() + " " + requestUrl;

        // 打印请求参数
        if (WebFluxUtils.isJsonRequest(exchange)) {
            String jsonParam = WebFluxUtils.resolveBodyFromCacheRequest(exchange);
            LogUtils.info("[PLUS]开始请求 => URL[{}],参数类型[json],参数:[{}]", url, jsonParam);
        } else {
            MultiValueMap<String, String> parameterMap = request.getQueryParams();
            if (MapUtil.isNotEmpty(parameterMap)) {
                String parameters = JsonUtils.toJSONString(parameterMap);
                LogUtils.info("[PLUS]开始请求 => URL[{}],参数类型[param],参数:[{}]", url, parameters);
            } else {
                LogUtils.info("[PLUS]开始请求 => URL[{}],无参数", url);
            }
        }

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());

        return chain.filter(exchange)
                .then(
                        Mono.fromRunnable(
                                () -> {
                                    Long startTime = exchange.getAttribute(START_TIME);
                                    if (startTime != null) {
                                        long executeTime = (System.currentTimeMillis() - startTime);
                                        LogUtils.info(
                                                "Response GlobalLogFilter [PLUS]结束请求 => URL[{}],耗时:[{}]毫秒",
                                                url,
                                                executeTime);
                                    }
                                }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 6;
    }
}
