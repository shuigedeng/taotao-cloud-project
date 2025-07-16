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

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关在路由完一个ServerWebExchange后，会通过为交换属性添加gatewayAlreadyRouted标志来标记该交换已路由。一旦请求被标记为已路由，其他路由过滤器将不会再次路由该请求，实际上是跳过了该过滤器。你可以自定义全局路由来控制过滤器是否执行：
 *
 * 通过ServerWebExchangeUtils工具类设置当前执行上下文的GATEWAY_ALREADY_ROUTED_ATTR属性为true。
 */
// @Component
public class PackGlobalRoutingFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!ServerWebExchangeUtils.isAlreadyRouted(exchange)) {
            System.out.println("还没被北路由...");
        }
        // 设置已被路由，后续的过滤器将不会被执行（注意过滤器的顺序）
        ServerWebExchangeUtils.setAlreadyRouted(exchange);
        return chain.filter(exchange);
    }
}
