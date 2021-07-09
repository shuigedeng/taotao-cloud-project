/*
 * Copyright 2020 the original author or authors.
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
package com.taotao.cloud.oauth2.biz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author Joe Grandja
 * @since 0.1.0
 */
@EnableWebSecurity
public class DefaultSecurityConfig {
//	private final ClientRegistrationRepository clientRegistrationRepository;

//	private final SocialDetailsService socialDetailsService;

//	@Bean
//	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//		http
//			.authorizeRequests(authorizeRequests ->
//				authorizeRequests.anyRequest().authenticated()
//			)
//			.formLogin(withDefaults());
////			.oauth2Login(config -> config
////				// 认证成功后的处理器
//////				.successHandler(new CustomOAuth2AuthenticationSuccessHandler())
////				// 认证失败后的处理器
//////				.failureHandler(new CustomOauth2AuthenticationFailureHandler())
////				.tokenEndpoint(this::tokenEndpoint)
////				.userInfoEndpoint(this::userInfoEndpoint)
////				.authorizationEndpoint(this::authorizationEndpoint)
////			);
//
//
//		return http.build();
//	}

	@Bean
	UserDetailsService users() {
		UserDetails user = User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();
		return new InMemoryUserDetailsManager(user);
	}
//
//	// 解决第三方授权登录兼容性问题
//	private void authorizationEndpoint(
//		OAuth2LoginConfigurer<HttpSecurity>.AuthorizationEndpointConfig authorization) {
//
//		// 处理 /{registrationId}/oauth2/authorization/{registrationId}
//		// 由于网关服务, 跳转需加前缀 spring.application.name, 将 name 和 registrationId 取名相同解决
//		DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
//			clientRegistrationRepository,
//			OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
//
//		// 保存 redirect_url 参数
//		resolver.setAuthorizationRequestCustomizer(build -> build.attributes(attributesConsumer -> {
//			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
//				.getRequestAttributes();
//			HttpServletRequest request = Objects.requireNonNull(attrs).getRequest();
//			String redirectUri = request.getParameter(OAuth2ParameterNames.REDIRECT_URI);
//			// todo 可以再这里做判断redirectUri,走白名单, 或者写死redirectUri
//			assert StringUtils.isEmpty(redirectUri);
//			attributesConsumer.put(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
//		}));
//		authorization.authorizationRequestResolver(resolver);
//
//		// 恢复 redirect_url 参数
////		authorization
////			.authorizationRequestRepository(new CustomOAuth2AuthorizationRequestRepository());
//	}
//
//	// 解决第三方授权登录兼容性问题
//	private void tokenEndpoint(
//		OAuth2LoginConfigurer<HttpSecurity>.TokenEndpointConfig tokenEndpoint) {
//		DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
////		client.setRequestEntityConverter(
////			new CustomOAuth2AuthorizationCodeGrantRequestEntityConverter());
//
//		OAuth2AccessTokenResponseHttpMessageConverter oAuth2AccessTokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
////		oAuth2AccessTokenResponseHttpMessageConverter
////			.setTokenResponseConverter(new CustomMapOAuth2AccessTokenResponseConverter());
//
//		ArrayList<MediaType> mediaTypes = new ArrayList<>(
//			oAuth2AccessTokenResponseHttpMessageConverter.getSupportedMediaTypes());
//		mediaTypes.add(MediaType.TEXT_PLAIN); // 解决微信问题:  放回是text/plain 的问题
//		mediaTypes.add(MediaType.TEXT_HTML); // 解决QQ问题:  放回是text/html 的问题
//		oAuth2AccessTokenResponseHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
//
//		RestTemplate restTemplate = new RestTemplate(Arrays
//			.asList(new FormHttpMessageConverter(), oAuth2AccessTokenResponseHttpMessageConverter));
//		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
//		client.setRestOperations(restTemplate);
//		tokenEndpoint.accessTokenResponseClient(client);
//	}
//
//	/**
//	 * 1. 解决第三方授权登录兼容性问题 2. 加载平台 userId
//	 */
//	private void userInfoEndpoint(
//		OAuth2LoginConfigurer<HttpSecurity>.UserInfoEndpointConfig userInfo) {
//		// 目前支持这四个, 如果是标准 OAuth2.0 协议,只需加入自定义的OAuth2User就好
//		Map<String, Class<? extends OAuth2User>> customUserTypes = new HashMap<>();
////		customUserTypes.put(GiteeOAuth2User.TYPE, GiteeOAuth2User.class);
////		customUserTypes.put(WechatOAuth2User.TYPE, WechatOAuth2User.class);
////		customUserTypes.put(QQOAuth2User.TYPE, QQOAuth2User.class);
////		customUserTypes.put(GitHubOAuth2User.TYPE, GitHubOAuth2User.class);
////
////		CustomUserTypesOAuth2UserService customOAuth2UserService = new UserDetailTypeOAuthUserService(
////			socialDetailsService, customUserTypes);
////		customOAuth2UserService
////			.setRequestEntityConverter(new CustomOAuth2UserRequestEntityConverter());
////
////		RestTemplate restTemplate = new RestTemplate(); // 解决微信问题: 放回是text/plain 的问题
////		restTemplate.getMessageConverters().add(new CustomMappingJackson2HttpMessageConverter());
////		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
////		customOAuth2UserService.setRestOperations(restTemplate);
//
////		userInfo.userService(customOAuth2UserService);
//	}

}
