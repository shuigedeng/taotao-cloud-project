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
package com.taotao.cloud.security.component;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.utils.ResponseUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * SecurityHandlerComponent
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 09:05
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityHandlerComponent {

	/**
	 * AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
	 *
	 * @author dengtao
	 * @since 2020/10/9 11:12
	 */
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPointComponent();
	}

	/**
	 * OAuth2AccessDeniedHandler 用来解决认证过的用户访问无权限资源时的异常
	 *
	 * @author dengtao
	 * @since 2020/10/9 11:13
	 */
	@Bean
	public OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler() {
		return new OAuth2AccessDeniedHandler() {
			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
				AccessDeniedException authException) throws IOException, ServletException {
				LogUtil.error("权限不足", authException);
				ResponseUtil.fail(response, ResultEnum.FORBIDDEN);
			}
		};
	}

//    @Bean
//    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
//        return (registry) -> registry.config().commonTags("application", applicationName);
//    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(
		ApplicationContext applicationContext) {
		OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
		expressionHandler.setApplicationContext(applicationContext);
		return expressionHandler;
	}


	public static class AuthenticationEntryPointComponent implements AuthenticationEntryPoint {

		private final RequestMatcher authorizationCodeGrantRequestMatcher = new AuthorizationCodeGrantRequestMatcher();
		private final AuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(
			"/auth/login");

		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
			LogUtil.error("认证失败", authException);
			// 触发重定向到登陆页面
			if (authorizationCodeGrantRequestMatcher.matches(request)) {
				loginUrlAuthenticationEntryPoint.commence(request, response, authException);
				return;
			}

			ResponseUtil.fail(response, ResultEnum.UNAUTHORIZED);
		}

		private class AuthorizationCodeGrantRequestMatcher implements RequestMatcher {

			/**
			 * <ol>
			 *     <li>授权码模式 URI</li>
			 *     <li>隐式授权模式 URI</li>
			 * </ol>
			 */
			private final Set<String> SUPPORT_URIS = new HashSet<>(
				Arrays.asList("response_type=code", "response_type=token"));

			@Override
			public boolean matches(HttpServletRequest request) {
				if (StringUtils.equals(request.getServletPath(), "/oauth/authorize")) {
					final String queryString = request.getQueryString();
					return SUPPORT_URIS.stream().anyMatch(
						supportUri -> StringUtils.indexOf(queryString, supportUri)
							!= StringUtils.INDEX_NOT_FOUND);
				}

				return false;
			}
		}
	}
}
