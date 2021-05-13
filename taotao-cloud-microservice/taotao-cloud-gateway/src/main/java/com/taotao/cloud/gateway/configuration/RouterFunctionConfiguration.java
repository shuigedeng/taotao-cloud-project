/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.gateway.configuration;

import com.taotao.cloud.gateway.handler.HystrixFallbackHandler;
import com.taotao.cloud.gateway.handler.ImageCodeHandler;
import com.taotao.cloud.gateway.properties.CustomGatewayProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 特殊路由配置信息
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/29 22:11
 */
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {

	private static final String FALLBACK = "/fallback";
	private static final String CODE = "/code";

	private final HystrixFallbackHandler hystrixFallbackHandler;
	private final ImageCodeHandler imageCodeWebHandler;
	private final CustomGatewayProperties customGatewayProperties;
//    private final SwaggerResourceHandler swaggerResourceHandler;
//    private final SwaggerSecurityHandler swaggerSecurityHandler;
//    private final SwaggerUiHandler swaggerUiHandler;

	@Bean
	public RouterFunction<ServerResponse> routerFunction() {
		return RouterFunctions.route(
			RequestPredicates.path(FALLBACK)
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler)
			.andRoute(RequestPredicates.GET(customGatewayProperties.getBaseUri() + CODE)
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), imageCodeWebHandler);
//                .andRoute(RequestPredicates.GET("/swagger-resources")
//                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerResourceHandler)
//                .andRoute(RequestPredicates.GET("/swagger-resources/configuration/ui")
//                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerUiHandler)
//                .andRoute(RequestPredicates.GET("/swagger-resources/configuration/security")
//                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerSecurityHandler);
	}
}
