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

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.support.function.FuncUtil;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import com.taotao.cloud.gateway.exception.InvalidTokenException;
import com.taotao.cloud.gateway.properties.SecurityProperties;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.beans.factory.InitializingBean;
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
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.web.server.authentication.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

import java.util.List;
import java.util.Objects;

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
public class GatewayResourceServerConfiguration {

	@Autowired
	private GatewayReactiveAuthorizationManager gatewayReactiveAuthorizationManager;

	@Autowired
	private SecurityProperties securityProperties;

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

		ServerAuthenticationEntryPoint serverAuthenticationEntryPoint = (exchange, e) -> {
			LogUtils.error(e, "user authentication error : {}", e.getMessage());

			if (e instanceof InvalidBearerTokenException) {
				return ResponseUtils.fail(exchange, "无效的token");
			}

			if (e instanceof InvalidTokenException) {
				return ResponseUtils.fail(exchange, e.getMessage());
			}

			return ResponseUtils.fail(exchange, ResultEnum.UNAUTHORIZED);
		};
		ServerAccessDeniedHandler serverAccessDeniedHandler = (exchange, e) -> {
			LogUtils.error(e, "user access denied error : {}", e.getMessage());
			return ResponseUtils.fail(exchange, ResultEnum.FORBIDDEN);
		};

		// ServerBearerTokenAuthenticationConverter serverBearerTokenAuthenticationConverter =
		//	new ServerBearerTokenAuthenticationConverter();
		// serverBearerTokenAuthenticationConverter.setAllowUriQueryParameter(true);

		// AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(
		//	new CustomReactiveAuthenticationManager());
		// authenticationWebFilter
		//	.setServerAuthenticationConverter(serverBearerTokenAuthenticationConverter);
		// authenticationWebFilter.setAuthenticationFailureHandler(
		//	new ServerAuthenticationEntryPointFailureHandler(serverAuthenticationEntryPoint));
		// authenticationWebFilter
		//	.setAuthenticationSuccessHandler(new CustomServerAuthenticationSuccessHandler());

		List<String> ignoreUrl = securityProperties.getIgnoreUrl();

		http.csrf(ServerHttpSecurity.CsrfSpec::disable)
			.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
			.headers((headerCustomizer) -> {
				headerCustomizer
					.frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable);
			})
			.authorizeExchange((authorizeExchangeCustomizer) -> {
				authorizeExchangeCustomizer
					.pathMatchers(ignoreUrl.toArray(new String[ignoreUrl.size()]))
					.permitAll()
					.pathMatchers(HttpMethod.OPTIONS)
					.permitAll()
					.matchers(EndpointRequest.toAnyEndpoint())
					.permitAll()
					.anyExchange()
					.access(gatewayReactiveAuthorizationManager);
			})
			// .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
			.exceptionHandling((exceptionHandlingCustomizer) -> {
				exceptionHandlingCustomizer
					.authenticationEntryPoint(serverAuthenticationEntryPoint)
					.accessDeniedHandler(serverAccessDeniedHandler);
			})
			.oauth2ResourceServer(oauth2ResourceServerCustomizer -> oauth2ResourceServerCustomizer
				.accessDeniedHandler(serverAccessDeniedHandler)
				.authenticationEntryPoint(serverAuthenticationEntryPoint)
				.bearerTokenConverter(exchange -> {
					ServerBearerTokenAuthenticationConverter defaultBearerTokenResolver =
						new ServerBearerTokenAuthenticationConverter();
					defaultBearerTokenResolver.setAllowUriQueryParameter(true);
					return defaultBearerTokenResolver.convert(exchange);
				})
				.jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
			);
		return http.build();
	}

	@Autowired(required = false)
	private DiscoveryClient discoveryClient;

	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:#{null}}")
	private String jwkSetUri;

	@Bean
	public ReactiveJwtDecoder jwtDecoder() {
		if (Objects.nonNull(discoveryClient)) {
			jwkSetUri = discoveryClient.getServices().stream()
				.filter(s -> s.contains(ServiceName.TAOTAO_CLOUD_AUTH))
				.flatMap(s -> discoveryClient.getInstances(s).stream())
				.map(instance ->
					String.format("http://%s:%s" + "/oauth2/jwks", instance.getHost(), instance.getPort()))
				.findFirst()
				.orElse(jwkSetUri);
		}

		NimbusReactiveJwtDecoder nimbusReactiveJwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(
				FuncUtil.predicate(jwkSetUri, StrUtil::isBlank, "http://127.0.0.1:33336/oauth2/jwks"))
			.jwsAlgorithm(SignatureAlgorithm.RS256)
			.build();

		// String issuerUri = null;
		// Supplier<OAuth2TokenValidator<Jwt>> defaultValidator = (issuerUri != null)
		//	? () -> JwtValidators.createDefaultWithIssuer(issuerUri) : JwtValidators::createDefault;
		// nimbusReactiveJwtDecoder.setJwtValidator(defaultValidator.get());

		nimbusReactiveJwtDecoder.setJwtValidator(JwtValidators.createDefault());
		return nimbusReactiveJwtDecoder;

		// return NimbusReactiveJwtDecoder
		//	.withJwkSetUri(FuncUtil.predicate(jwkSetUri, StrUtil::isBlank,
		//		"http://127.0.0.1:33336/oauth2/jwks"))
		//	.build();
	}

	@Configuration
	@ConditionalOnNacosDiscoveryEnabled
	public static class NacosServiceListenerWithAuth implements InitializingBean {

		@Autowired
		private NacosServiceManager nacosServiceManager;

		@Autowired
		private NacosDiscoveryProperties properties;

		@Override
		public void afterPropertiesSet() throws Exception {
			nacosServiceManager
				.getNamingService()
				.subscribe(
					ServiceName.TAOTAO_CLOUD_AUTH,
					this.properties.getGroup(),
					List.of(this.properties.getClusterName()),
					event -> {
						if (event instanceof NamingEvent) {
							List<Instance> instances = ((NamingEvent) event).getInstances();
							if (instances.isEmpty()) {
								return;
							}
							Instance instance = instances.get(0);
							String jwkSetUri = String.format(
								"http://%s:%s" + "/oauth2/jwks", instance.getIp(), instance.getPort());

							NimbusReactiveJwtDecoder nimbusReactiveJwtDecoder =
								NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri)
									.jwsAlgorithm(SignatureAlgorithm.RS256)
									.build();
							nimbusReactiveJwtDecoder.setJwtValidator(JwtValidators.createDefault());
							ContextUtils.destroySingletonBean("reactiveJwtDecoder");
							ContextUtils.registerSingletonBean("reactiveJwtDecoder", nimbusReactiveJwtDecoder);
						}
					});
		}
	}
}
