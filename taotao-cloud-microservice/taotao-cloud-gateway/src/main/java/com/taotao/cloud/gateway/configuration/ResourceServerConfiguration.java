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

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.utils.ResponseUtil;
import com.taotao.cloud.gateway.component.AuthenticationManagerComponent;
import com.taotao.cloud.gateway.component.Oauth2AuthSuccessHandler;
import com.taotao.cloud.gateway.component.PermissionAuthManager;
import com.taotao.cloud.gateway.properties.CustomGatewayProperties;
import com.taotao.cloud.security.properties.PermitProperties;
import com.taotao.cloud.security.properties.SecurityProperties;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 过滤器配置
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/29 22:11
 */
@Configuration
@AllArgsConstructor
@EnableWebFluxSecurity
public class ResourceServerConfiguration {

	private final SecurityProperties securityProperties;
	private final TokenStore tokenStore;
	private final PermissionAuthManager permissionAuthManager;
	private final CustomGatewayProperties customGatewayProperties;

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		AuthenticationManagerComponent customAuthenticationManager = new AuthenticationManagerComponent(
			tokenStore);

		//token转换器
		ServerBearerTokenAuthenticationConverter tokenAuthenticationConverter =
			new ServerBearerTokenAuthenticationConverter();
		tokenAuthenticationConverter.setAllowUriQueryParameter(true);

		JsonAuthenticationEntryPoint entryPoint = new JsonAuthenticationEntryPoint();
		//oauth2认证过滤器
		AuthenticationWebFilter oauth2Filter = new AuthenticationWebFilter(
			customAuthenticationManager);
		oauth2Filter.setServerAuthenticationConverter(tokenAuthenticationConverter);
		oauth2Filter.setAuthenticationFailureHandler(
			new ServerAuthenticationEntryPointFailureHandler(entryPoint));
		oauth2Filter.setAuthenticationSuccessHandler(new Oauth2AuthSuccessHandler());

		PermitProperties ignore = securityProperties.getIgnore();
		ignore.setHttpUrls(Arrays.stream(ignore.getHttpUrls())
			.map(uri -> customGatewayProperties.getBaseUri() + uri).toArray(String[]::new));
		http.httpBasic().disable()
			.csrf().disable()
			.authorizeExchange()
			.pathMatchers(securityProperties.getIgnore().getUrls()).permitAll()
			.pathMatchers(HttpMethod.OPTIONS).permitAll()
			.matchers(EndpointRequest.toAnyEndpoint()).permitAll()
			// 访问权限控制
			.anyExchange().access(permissionAuthManager)
			.and()
			.addFilterAt(oauth2Filter, SecurityWebFiltersOrder.AUTHENTICATION)
			.exceptionHandling()
			//处理未授权
			.accessDeniedHandler((exchange, e) -> {
				LogUtil.error(e);
				return ResponseUtil.failed(exchange, ResultEnum.FORBIDDEN);
			})
			//处理未认证
			.authenticationEntryPoint(entryPoint)
			.and()
			.headers().frameOptions().disable();

		return http.build();
	}

	public static class JsonAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

		@Override
		public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
			LogUtil.error("认证失败", e);
			return ResponseUtil.failed(exchange, ResultEnum.UNAUTHORIZED);
		}
	}

}
