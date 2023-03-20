//package com.taotao.cloud.gateway.filter.global;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@Component
//public class VersionLbIsolationFilter implements GlobalFilter, Ordered {
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
//}
