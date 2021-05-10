package com.taotao.cloud.oauth2.biz.config;

import com.taotao.cloud.oauth2.biz.social.SocialDetailsService;
import com.taotao.cloud.oauth2.biz.social.UserDetailTypeOAuthUserService;
import com.taotao.cloud.oauth2.biz.social.converter.CustomMapOAuth2AccessTokenResponseConverter;
import com.taotao.cloud.oauth2.biz.social.converter.CustomMappingJackson2HttpMessageConverter;
import com.taotao.cloud.oauth2.biz.social.converter.CustomOAuth2AuthorizationCodeGrantRequestEntityConverter;
import com.taotao.cloud.oauth2.biz.social.converter.CustomOAuth2UserRequestEntityConverter;
import com.taotao.cloud.oauth2.biz.social.user.GitHubOAuth2User;
import com.taotao.cloud.oauth2.biz.social.user.GiteeOAuth2User;
import com.taotao.cloud.oauth2.biz.social.user.QQOAuth2User;
import com.taotao.cloud.oauth2.biz.social.user.WechatOAuth2User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.CustomUserTypesOAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
@Order(101)
@AllArgsConstructor
public class OAuth2AuthorizationServerConfiguration extends WebSecurityConfigurerAdapter {

	private final ClientRegistrationRepository clientRegistrationRepository;

	private final SocialDetailsService socialDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.formLogin(config -> config.loginPage("/login"))
			.logout()
			.invalidateHttpSession(true)
			.and()
			.oauth2Login(config -> config
				// 认证成功后的处理器
				.successHandler(new CustomOAuth2AuthenticationSuccessHandler())
				// 认证失败后的处理器
				.failureHandler(new Oauth2AuthenticationFailureHandler())
				.loginPage("/login")
				.tokenEndpoint(this::tokenEndpoint)
				.userInfoEndpoint(this::userInfoEndpoint)
				.authorizationEndpoint(this::authorizationEndpoint)
			)
			.logout(config -> config.logoutSuccessHandler(new CustomLogoutSuccessHandler()))
			.authorizeRequests()
			.antMatchers(
				"/login",
				"/webjars/**",
				"/*.css",
				"/login/oauth2/code/*",
				"/resource/ids",
				"/v2/api-docs",
				"/actuator/**"
			)
			.permitAll()
			.anyRequest().authenticated()
			.and()
			.oauth2ResourceServer()
//			.authenticationManagerResolver(customAuthenticationManager())
			.jwt();
	}


	// 解决第三方授权登录兼容性问题
	private void authorizationEndpoint(
		OAuth2LoginConfigurer<HttpSecurity>.AuthorizationEndpointConfig authorization) {

		// 处理 /{registrationId}/oauth2/authorization/{registrationId}
		// 由于网关服务, 跳转需加前缀 spring.application.name, 将 name 和 registrationId 取名相同解决
		DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
			clientRegistrationRepository,
			OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);

		// 保存 redirect_url 参数
		resolver.setAuthorizationRequestCustomizer(build -> build.attributes(attributesConsumer -> {
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
			HttpServletRequest request = Objects.requireNonNull(attrs).getRequest();
			String redirectUri = request.getParameter(OAuth2ParameterNames.REDIRECT_URI);
			// todo 可以再这里做判断redirectUri,走白名单, 或者写死redirectUri
			assert StringUtils.isEmpty(redirectUri);
			attributesConsumer.put(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
		}));
		authorization.authorizationRequestResolver(resolver);

		// 恢复 redirect_url 参数
		authorization
			.authorizationRequestRepository(new CustomOAuth2AuthorizationRequestRepository());
	}

	// 解决第三方授权登录兼容性问题
	private void tokenEndpoint(
		OAuth2LoginConfigurer<HttpSecurity>.TokenEndpointConfig tokenEndpoint) {
		DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
		client.setRequestEntityConverter(
			new CustomOAuth2AuthorizationCodeGrantRequestEntityConverter());

		OAuth2AccessTokenResponseHttpMessageConverter oAuth2AccessTokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
		oAuth2AccessTokenResponseHttpMessageConverter
			.setTokenResponseConverter(new CustomMapOAuth2AccessTokenResponseConverter());

		ArrayList<MediaType> mediaTypes = new ArrayList<>(
			oAuth2AccessTokenResponseHttpMessageConverter.getSupportedMediaTypes());
		mediaTypes.add(MediaType.TEXT_PLAIN); // 解决微信问题:  放回是text/plain 的问题
		mediaTypes.add(MediaType.TEXT_HTML); // 解决QQ问题:  放回是text/html 的问题
		oAuth2AccessTokenResponseHttpMessageConverter.setSupportedMediaTypes(mediaTypes);

		RestTemplate restTemplate = new RestTemplate(Arrays
			.asList(new FormHttpMessageConverter(), oAuth2AccessTokenResponseHttpMessageConverter));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		client.setRestOperations(restTemplate);
		tokenEndpoint.accessTokenResponseClient(client);
	}

	/**
	 * 1. 解决第三方授权登录兼容性问题 2. 加载平台 userId
	 */
	private void userInfoEndpoint(
		OAuth2LoginConfigurer<HttpSecurity>.UserInfoEndpointConfig userInfo) {
		// 目前支持这四个, 如果是标准 OAuth2.0 协议,只需加入自定义的OAuth2User就好
		Map<String, Class<? extends OAuth2User>> customUserTypes = new HashMap<>();
		customUserTypes.put(GiteeOAuth2User.TYPE, GiteeOAuth2User.class);
		customUserTypes.put(WechatOAuth2User.TYPE, WechatOAuth2User.class);
		customUserTypes.put(QQOAuth2User.TYPE, QQOAuth2User.class);
		customUserTypes.put(GitHubOAuth2User.TYPE, GitHubOAuth2User.class);

		CustomUserTypesOAuth2UserService customOAuth2UserService = new UserDetailTypeOAuthUserService(
			socialDetailsService, customUserTypes);
		customOAuth2UserService
			.setRequestEntityConverter(new CustomOAuth2UserRequestEntityConverter());

		RestTemplate restTemplate = new RestTemplate(); // 解决微信问题: 放回是text/plain 的问题
		restTemplate.getMessageConverters().add(new CustomMappingJackson2HttpMessageConverter());
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		customOAuth2UserService.setRestOperations(restTemplate);

		userInfo.userService(customOAuth2UserService);
	}
}
