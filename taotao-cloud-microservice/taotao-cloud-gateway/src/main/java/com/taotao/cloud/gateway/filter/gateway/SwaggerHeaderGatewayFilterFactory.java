///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.gateway.filter.gateway;
//
//import lombok.AllArgsConstructor;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.server.ServerWebExchange;
//
///**
// * SwaggerHeader过滤
// *
// * @author shuigedeng
// * @version 1.0.0
// * @since 2020/4/29 22:13
// */
//@Component
//@AllArgsConstructor
//public class SwaggerHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
//
//	private static final String HEADER_NAME = "X-Forwarded-Prefix";
//
//	private final SwaggerAggProperties swaggerAggProperties;
//
//	@Override
//	public GatewayFilter apply(Object config) {
//		return (exchange, chain) -> {
//			ServerHttpRequest request = exchange.getRequest();
//			String path = request.getURI().getPath();
//			if (!StringUtils.endsWithIgnoreCase(path, swaggerAggProperties.getApiDocsPath())) {
//				return chain.filter(exchange);
//			}
//			String basePath = path
//				.substring(0, path.lastIndexOf(swaggerAggProperties.getApiDocsPath()));
//			ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME, basePath).build();
//			ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
//			return chain.filter(newExchange);
//		};
//	}
//}
