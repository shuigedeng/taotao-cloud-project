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
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.core.Ordered;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;
//
// import java.util.List;
//
// @Component
// public class VersionLbIsolationFilter implements GlobalFilter, Ordered {
//
//    @Value("${zlt.ribbon.isolation.enabled}")
//    private Boolean enableVersionControl;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        if(Boolean.TRUE.equals(enableVersionControl)
//                && exchange.getRequest().getQueryParams().containsKey("zlt.loadbalance.version")){
//            String version = exchange.getRequest().getQueryParams().get("zlt.loadbalance.version").get(0);
//            ServerHttpRequest rebuildRequest = exchange.getRequest().mutate().headers(header -> {
//                header.add("zlt.loadbalance.version", version);
//            }).build();
//            ServerWebExchange rebuildServerWebExchange = exchange.mutate().request(rebuildRequest).build();
//            return chain.filter(rebuildServerWebExchange);
//        }
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.HIGHEST_PRECEDENCE;
//    }
// }
