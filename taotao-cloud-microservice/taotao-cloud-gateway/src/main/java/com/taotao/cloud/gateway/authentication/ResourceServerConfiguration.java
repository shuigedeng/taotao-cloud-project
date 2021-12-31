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

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import com.taotao.cloud.gateway.exception.InvalidTokenException;
import com.taotao.cloud.gateway.properties.SecurityProperties;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

/**
 * ResourceServerConfig
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/06/18 14:41
 */
@Configuration
@EnableWebFluxSecurity
@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class ResourceServerConfiguration {

	@Autowired
	private CustomReactiveAuthorizationManager customReactiveAuthorizationManager;
	@Autowired
	private SecurityProperties securityProperties;

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

		ServerAuthenticationEntryPoint serverAuthenticationEntryPoint = (exchange, e) -> {
			LogUtil.error(e, "user authentication error : {}", e.getMessage());

			if (e instanceof InvalidBearerTokenException) {
				return ResponseUtil.fail(exchange, "无效的token");
			}

			if (e instanceof InvalidTokenException) {
				return ResponseUtil.fail(exchange, e.getMessage());
			}

			return ResponseUtil.fail(exchange, ResultEnum.UNAUTHORIZED);
		};
		ServerAccessDeniedHandler serverAccessDeniedHandler = (exchange, e) -> {
			LogUtil.error(e, "user access denied error : {}", e.getMessage());
			return ResponseUtil.fail(exchange, ResultEnum.FORBIDDEN);
		};

		//ServerBearerTokenAuthenticationConverter serverBearerTokenAuthenticationConverter =
		//	new ServerBearerTokenAuthenticationConverter();
		//serverBearerTokenAuthenticationConverter.setAllowUriQueryParameter(true);

		//AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(
		//	new CustomReactiveAuthenticationManager());
		//authenticationWebFilter
		//	.setServerAuthenticationConverter(serverBearerTokenAuthenticationConverter);
		//authenticationWebFilter.setAuthenticationFailureHandler(
		//	new ServerAuthenticationEntryPointFailureHandler(serverAuthenticationEntryPoint));
		//authenticationWebFilter
		//	.setAuthenticationSuccessHandler(new CustomServerAuthenticationSuccessHandler());

		List<String> ignoreUrl = securityProperties.getIgnoreUrl();

		http
			.csrf().disable()
			.httpBasic().disable()
			.headers().frameOptions().disable()
			.and()
			.authorizeExchange()
			.pathMatchers(ignoreUrl.toArray(new String[ignoreUrl.size()])).permitAll()
			.pathMatchers(HttpMethod.OPTIONS).permitAll()
			.matchers(EndpointRequest.toAnyEndpoint()).permitAll()
			.anyExchange().access(customReactiveAuthorizationManager)
			.and()
			//.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
			.exceptionHandling()
			.authenticationEntryPoint(serverAuthenticationEntryPoint)
			.accessDeniedHandler(serverAccessDeniedHandler)
			.and()
			.oauth2ResourceServer(oauth2ResourceServerCustomizer ->
				oauth2ResourceServerCustomizer
					.accessDeniedHandler(serverAccessDeniedHandler)
					.authenticationEntryPoint(serverAuthenticationEntryPoint)
					.bearerTokenConverter(exchange -> {
						ServerBearerTokenAuthenticationConverter defaultBearerTokenResolver = new ServerBearerTokenAuthenticationConverter();
						defaultBearerTokenResolver.setAllowUriQueryParameter(true);
						return defaultBearerTokenResolver.convert(exchange);
					})
					.jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
			);
		return http.build();
	}

	@Autowired(required = false)
	private DiscoveryClient discoveryClient;
	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	private String jwkSetUri;

	@Bean
	public NimbusReactiveJwtDecoder jwtDecoder() {
		if (Objects.nonNull(discoveryClient)) {
			jwkSetUri = discoveryClient.getServices().stream()
				.filter(s -> s.contains(ServiceName.TAOTAO_CLOUD_AUTH))
				.flatMap(s -> discoveryClient.getInstances(s).stream())
				.map(instance -> String.format("http://%s:%s" + "/oauth2/jwks", instance.getHost(),
					instance.getPort()))
				.findFirst()
				.orElse(jwkSetUri);
		}
		return NimbusReactiveJwtDecoder.withJwkSetUri("http://127.0.0.1:33336/oauth2/jwks").build();
	}

}
