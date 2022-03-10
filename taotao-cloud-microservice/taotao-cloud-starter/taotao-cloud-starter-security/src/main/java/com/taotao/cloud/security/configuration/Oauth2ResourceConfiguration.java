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
package com.taotao.cloud.security.configuration;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.servlet.ResponseUtil;
import com.taotao.cloud.security.annotation.NotAuth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.log.LogMessage;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

/**
 * Oauth2ResourceSecurityConfigurer
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/25 09:57
 */
@Configuration
public class Oauth2ResourceConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	private String jwkSetUri;

	@Autowired(required = false)
	private DiscoveryClient discoveryClient;

	@Bean
	public JwtDecoder jwtDecoder() {
		if (Objects.nonNull(discoveryClient)) {
			jwkSetUri = discoveryClient.getServices().stream()
				.filter(s -> s.contains(ServiceName.TAOTAO_CLOUD_AUTH))
				.flatMap(s -> discoveryClient.getInstances(s).stream())
				.map(instance -> String.format("http://%s:%s" + "/oauth2/jwks", instance.getHost(),
					instance.getPort()))
				.findFirst()
				.orElse(jwkSetUri);
		}
		return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}

	JwtAuthenticationConverter jwtAuthenticationConverter() {
		CustomJwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new CustomJwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests(registry -> {
				permitAllUrls(registry, http.getSharedObject(ApplicationContext.class));
				registry.anyRequest().authenticated();
			})
			.oauth2ResourceServer(config -> config
				.accessDeniedHandler((request, response, accessDeniedException) -> {
					LogUtil.error("用户权限不足", accessDeniedException);
					ResponseUtil.fail(response, ResultEnum.FORBIDDEN);
				})
				.authenticationEntryPoint((request, response, authException) -> {
					LogUtil.error("用户未登录认证失败", authException);
					ResponseUtil.fail(response, ResultEnum.UNAUTHORIZED);
				})
				.bearerTokenResolver(bearerTokenResolver())
				.jwt(jwt -> jwt.decoder(jwtDecoder())
					.jwtAuthenticationConverter(jwtAuthenticationConverter())));
	}

	private void permitAllUrls(
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry,
		ApplicationContext ac) {
		List<String> permitAllUrls = new ArrayList<>(Arrays.asList(
			"/swagger-ui.html",
			"/v3/**",
			"/favicon.ico",
			"/swagger-resources/**",
			"/webjars/**",
			"/actuator/**",
			"/index",
			"/index.html",
			"/doc.html",
			"/*.js",
			"/*.css",
			"/*.json",
			"/*.min.js",
			"/*.min.css",
			"/doc/**",
			"/health/**"));

		RequestMappingHandlerMapping mapping = ac.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		map.keySet().forEach(info -> {
			HandlerMethod handlerMethod = map.get(info);

			Set<NotAuth> set = new HashSet<>();
			set.add(AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), NotAuth.class));
			set.add(AnnotationUtils.findAnnotation(handlerMethod.getMethod(), NotAuth.class));
			set.forEach(annotation -> Optional.ofNullable(annotation)
				.flatMap(inner -> Optional.ofNullable(info.getPathPatternsCondition()))
				.ifPresent(pathPatternsRequestCondition -> {
					permitAllUrls.addAll(pathPatternsRequestCondition.getPatterns()
						.stream()
						.map(PathPattern::getPatternString).toList());
				}));
		});

		permitAllUrls.forEach(url -> registry.antMatchers(url).permitAll());

		LogUtil.info("permit all urls: {}", permitAllUrls.toString());
	}

	/**
	 * 启用参数传递token
	 */
	private DefaultBearerTokenResolver bearerTokenResolver() {
		DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
		defaultBearerTokenResolver.setAllowFormEncodedBodyParameter(true);
		defaultBearerTokenResolver.setAllowUriQueryParameter(true);
		return defaultBearerTokenResolver;
	}

	public static class CustomJwtGrantedAuthoritiesConverter implements
		Converter<Jwt, Collection<GrantedAuthority>> {

		private final Log logger = LogFactory.getLog(getClass());

		private static final String DEFAULT_AUTHORITY_PREFIX = "SCOPE_";

		private static final Collection<String> WELL_KNOWN_AUTHORITIES_CLAIM_NAMES = Arrays.asList(
			"scope", "scp");

		private String authorityPrefix = DEFAULT_AUTHORITY_PREFIX;

		private String authoritiesClaimName;

		/**
		 * Extract {@link GrantedAuthority}s from the given {@link Jwt}.
		 *
		 * @param jwt The {@link Jwt} token
		 * @return The {@link GrantedAuthority authorities} read from the token scopes
		 */
		@Override
		public Collection<GrantedAuthority> convert(Jwt jwt) {
			Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			for (String authority : getAuthorities(jwt)) {
				grantedAuthorities.add(
					new SimpleGrantedAuthority(this.authorityPrefix + authority));
			}
			return grantedAuthorities;
		}

		/**
		 * Sets the prefix to use for {@link GrantedAuthority authorities} mapped by this converter.
		 * Defaults to {@link JwtGrantedAuthoritiesConverter#DEFAULT_AUTHORITY_PREFIX}.
		 *
		 * @param authorityPrefix The authority prefix
		 * @since 5.2
		 */
		public void setAuthorityPrefix(String authorityPrefix) {
			//Assert.notNull(authorityPrefix, "authorityPrefix cannot be null");
			this.authorityPrefix = authorityPrefix;
		}

		/**
		 * Sets the name of token claim to use for mapping {@link GrantedAuthority authorities} by
		 * this converter. Defaults to {@link JwtGrantedAuthoritiesConverter#WELL_KNOWN_AUTHORITIES_CLAIM_NAMES}.
		 *
		 * @param authoritiesClaimName The token claim name to map authorities
		 * @since 5.2
		 */
		public void setAuthoritiesClaimName(String authoritiesClaimName) {
			Assert.hasText(authoritiesClaimName, "authoritiesClaimName cannot be empty");
			this.authoritiesClaimName = authoritiesClaimName;
		}

		private String getAuthoritiesClaimName(Jwt jwt) {
			if (this.authoritiesClaimName != null) {
				return this.authoritiesClaimName;
			}
			for (String claimName : WELL_KNOWN_AUTHORITIES_CLAIM_NAMES) {
				if (jwt.hasClaim(claimName)) {
					return claimName;
				}
			}
			return null;
		}

		private Collection<String> getAuthorities(Jwt jwt) {
			String claimName = getAuthoritiesClaimName(jwt);
			if (claimName == null) {
				this.logger.trace(
					"Returning no authorities since could not find any claims that might contain scopes");
				return Collections.emptyList();
			}
			if (this.logger.isTraceEnabled()) {
				this.logger.trace(LogMessage.format("Looking for scopes in claim %s", claimName));
			}
			Object authorities = jwt.getClaim(claimName);
			if (authorities instanceof String) {
				if (StringUtils.hasText((String) authorities)) {
					return Arrays.asList(((String) authorities).split(" "));
				}
				return Collections.emptyList();
			}
			if (authorities instanceof Collection) {
				return castAuthoritiesToCollection(authorities);
			}
			return Collections.emptyList();
		}

		private Collection<String> castAuthoritiesToCollection(Object authorities) {
			return (Collection<String>) authorities;
		}

	}
}
