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
package com.taotao.cloud.security.resource;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import com.taotao.cloud.security.annotation.NotAuth;
import com.taotao.cloud.security.security.CustomizedAccessDeniedHandler;
import com.taotao.cloud.security.security.CustomizedAuthenticationEntryPoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Oauth2ResourceSecurityConfigurer
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/25 09:57
 */
@Order(101)
@Configuration
public class Oauth2ResourceSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests(registry -> {
				permitAllUrls(registry, http.getSharedObject(ApplicationContext.class));
				registry.anyRequest().authenticated();
			})
			.oauth2ResourceServer(config -> config
				.authenticationEntryPoint(new CustomizedAuthenticationEntryPoint())
				.accessDeniedHandler(new CustomizedAccessDeniedHandler())
				.bearerTokenResolver(bearerTokenResolver())
				.jwt());
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
			"/order/**",
			"/uc/**",
			"/health/**"));

		RequestMappingHandlerMapping mapping = ac.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		// 收集 NotAuth 注解的接口
		map.keySet().forEach(info -> {
			HandlerMethod handlerMethod = map.get(info);

			Set<NotAuth> set = new HashSet<>();
			set.add(AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), NotAuth.class));
			set.add(AnnotationUtils.findAnnotation(handlerMethod.getMethod(), NotAuth.class));
			set.forEach(annotation -> {
				Optional.ofNullable(annotation).ifPresent(
					inner -> permitAllUrls.addAll(info.getPatternsCondition().getPatterns()));
			});
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

	private void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException)
		throws IOException {

		if (!response.isCommitted()) {
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpServletResponse.SC_OK);
			Result<String> r = Result.fail(authException.getMessage());
			ResponseUtil.fail(response, r);
		}
	}
}
