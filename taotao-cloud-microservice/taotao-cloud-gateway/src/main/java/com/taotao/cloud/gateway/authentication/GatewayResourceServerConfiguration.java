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

package com.taotao.cloud.gateway.authentication;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.security.spring.enums.Target;
import com.taotao.boot.security.spring.autoconfigure.properties.OAuth2AuthorizationProperties;
import com.taotao.boot.security.spring.autoconfigure.properties.OAuth2EndpointProperties;
import com.taotao.cloud.gateway.properties.SecurityProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.security.autoconfigure.actuate.web.reactive.EndpointRequest;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.server.authentication.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 资源服务器配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/06/18 14:41
 */
@Configuration
@EnableWebFluxSecurity
@ConditionalOnProperty(
	prefix = SecurityProperties.PREFIX,
	name = "enabled",
	havingValue = "true",
	matchIfMissing = true)
@AllArgsConstructor
public class GatewayResourceServerConfiguration {

	private final GatewayReactiveAuthorizationManager gatewayReactiveAuthorizationManager;
	private final SecurityProperties securityProperties;
	private final OAuth2EndpointProperties endpointProperties;
	private final OAuth2ResourceServerProperties resourceServerProperties;
	private final ReactiveJwtDecoder jwtDecoder;
	private final OAuth2AuthorizationProperties authorizationProperties;

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(
		ServerHttpSecurity http, ReactiveJwtDecoder jwtDecoder) {
		// ServerBearerTokenAuthenticationConverter serverBearerTokenAuthenticationConverter =
		//	new ServerBearerTokenAuthenticationConverter();
		// serverBearerTokenAuthenticationConverter.setAllowUriQueryParameter(true);

		// AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(
		//	new CustomReactiveAuthenticationManager());
		// authenticationWebFilter
		//	.setServerAuthenticationConverter(serverBearerTokenAuthenticationConverter);
		// authenticationWebFilter.setAuthenticationFailureHandler(
		//	new ServerAuthenticationEntryPointFailureHandler(new
		// JsonServerAuthenticationEntryPoint()));
		// authenticationWebFilter
		//	.setAuthenticationSuccessHandler(new CustomServerAuthenticationSuccessHandler());

		http.csrf(ServerHttpSecurity.CsrfSpec::disable)
			.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
			.headers(
				(headerCustomizer) -> {
					headerCustomizer.frameOptions(
						ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable);
				})
			.csrf(ServerHttpSecurity.CsrfSpec::disable)
			.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
			.cors(ServerHttpSecurity.CorsSpec::disable)
			.logout(ServerHttpSecurity.LogoutSpec::disable)
			.authorizeExchange(
				authorizeExchangeCustomizer -> {
					permitAllUrls(authorizeExchangeCustomizer);

					authorizeExchangeCustomizer
						.anyExchange()
						.access(gatewayReactiveAuthorizationManager);
				})
			// .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
			.exceptionHandling(
				(exceptionHandlingCustomizer) -> {
					exceptionHandlingCustomizer
						.authenticationEntryPoint(
							new JsonServerAuthenticationEntryPoint())
						.accessDeniedHandler(new JsonServerAccessDeniedHandler());
				})
			.oauth2ResourceServer(this::from);
		return http.build();
	}

	public ServerHttpSecurity.OAuth2ResourceServerSpec from(
		ServerHttpSecurity.OAuth2ResourceServerSpec oAuth2ResourceServerSpec) {
		if (isRemoteValidate()) {
			ReactiveSecurityOpaqueTokenIntrospector opaqueTokenIntrospector =
				new ReactiveSecurityOpaqueTokenIntrospector(
					endpointProperties, resourceServerProperties);

			oAuth2ResourceServerSpec
				.opaqueToken(
					opaqueTokenCustomizer -> {
						opaqueTokenCustomizer.introspector(opaqueTokenIntrospector);
					})
				.accessDeniedHandler(new JsonServerAccessDeniedHandler())
				.authenticationEntryPoint(new JsonServerAuthenticationEntryPoint());
		} else {
			oAuth2ResourceServerSpec
				.jwt(
					jwtCustomizer -> {
						jwtCustomizer
							.jwtDecoder(this.jwtDecoder)
							.jwtAuthenticationConverter(
								new ReactiveJwtAuthenticationConverter());
						// .jwtAuthenticationConverter(jwtAuthenticationConverter());
					})
				.bearerTokenConverter(
					exchange -> {
						ServerBearerTokenAuthenticationConverter
							defaultBearerTokenResolver =
							new ServerBearerTokenAuthenticationConverter();
						defaultBearerTokenResolver.setAllowUriQueryParameter(true);
						return defaultBearerTokenResolver.convert(exchange);
					})
				.accessDeniedHandler(new JsonServerAccessDeniedHandler())
				.authenticationEntryPoint(new JsonServerAuthenticationEntryPoint());
		}
		return oAuth2ResourceServerSpec;
	}

	/**
	 * 远程验证
	 *
	 * @return boolean
	 * @since 2023-07-04 09:58:49
	 */
	private boolean isRemoteValidate() {
		return this.authorizationProperties.getValidate() == Target.REMOTE;
	}

	private void permitAllUrls(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {
		List<String> permitAllUrls = securityProperties.getIgnoreUrl();

		permitAllUrls.forEach(url -> authorizeExchangeSpec.pathMatchers(url).permitAll());

		authorizeExchangeSpec
			.pathMatchers(permitAllUrls.toArray(new String[permitAllUrls.size()]))
			.permitAll()
			.pathMatchers(HttpMethod.OPTIONS)
			.permitAll()
			.matchers(EndpointRequest.toAnyEndpoint())
			.permitAll();

		LogUtils.info("permit all urls: {}", permitAllUrls.toString());
	}
}
