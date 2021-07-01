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
package com.taotao.cloud.gateway.authentication;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

/**
 * ResourceServerConfig
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/06/18 14:41
 */
@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfiguration {

	private final CustomReactiveAuthorizationManager customReactiveAuthorizationManager;

	private static final String[] ENDPOINTS = {
		"/actuator/**",
		"/v3/**",
		"/*/v3/**",
		"/fallback",
		"/favicon.ico",
		"/swagger-resources/**",
		"/webjars/**",
		"/druid/**",
		"/*/*.html",
		"/*/*.css",
		"/*/*.js",
		"/*.js",
		"/*.css",
		"/*.html",
		"/*/favicon.ico",
		"/*/api-docs",
		"/css/**",
		"/js/**",
		"/images/**"
	};

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		ServerBearerTokenAuthenticationConverter serverBearerTokenAuthenticationConverter =
			new ServerBearerTokenAuthenticationConverter();
		serverBearerTokenAuthenticationConverter.setAllowUriQueryParameter(true);

		ServerAuthenticationEntryPoint serverAuthenticationEntryPoint = (exchange, e) -> {
			LogUtil.error("user authentication error : {0}", e, e.getMessage());
			return ResponseUtil.fail(exchange, ResultEnum.UNAUTHORIZED);
		};
		ServerAccessDeniedHandler serverAccessDeniedHandler = (exchange, e) -> {
			LogUtil.error("user access denied error : {0}", e, e.getMessage());
			return ResponseUtil.fail(exchange, ResultEnum.FORBIDDEN);
		};

		CustomReactiveAuthenticationManager customReactiveAuthenticationManager = new CustomReactiveAuthenticationManager();
		AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(customReactiveAuthenticationManager);
		authenticationWebFilter.setServerAuthenticationConverter(serverBearerTokenAuthenticationConverter);
		authenticationWebFilter.setAuthenticationFailureHandler(
			new ServerAuthenticationEntryPointFailureHandler(serverAuthenticationEntryPoint));
		authenticationWebFilter
			.setAuthenticationSuccessHandler(new CustomServerAuthenticationSuccessHandler());

		http
			.csrf().disable()
			.httpBasic().disable()
			.headers().frameOptions().disable()
			.and()
			.authorizeExchange()
			.pathMatchers(ENDPOINTS).permitAll()
			.pathMatchers(HttpMethod.OPTIONS).permitAll()
			.matchers(EndpointRequest.toAnyEndpoint()).permitAll()
			.anyExchange().access(customReactiveAuthorizationManager)
			.and()
			.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
			.exceptionHandling()
			.authenticationEntryPoint(serverAuthenticationEntryPoint)
			.accessDeniedHandler(serverAccessDeniedHandler)
			.and()
			.oauth2ResourceServer()
			.jwt();
		return http.build();
	}
}
