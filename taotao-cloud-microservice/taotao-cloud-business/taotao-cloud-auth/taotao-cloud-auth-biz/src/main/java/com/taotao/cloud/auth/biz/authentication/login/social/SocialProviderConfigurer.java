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

package com.taotao.cloud.auth.biz.authentication.login.social;

import com.taotao.cloud.auth.biz.authentication.jwt.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.login.social.gitee.GiteeOAuth2UserService;
import com.taotao.cloud.auth.biz.authentication.login.social.github.GithubOAuth2UserService;
import com.taotao.cloud.auth.biz.authentication.login.social.qq.QQOauth2UserService;
import com.taotao.cloud.auth.biz.authentication.login.social.qq.QqOAuth2AccessTokenResponseHttpMessageConverter;
import com.taotao.cloud.auth.biz.authentication.login.social.wechat.WechatOAuth2UserService;
import com.taotao.cloud.auth.biz.authentication.login.social.wechatwork.WorkWechatOAuth2UserService;
import com.taotao.cloud.auth.biz.authentication.login.social.weibo.WeiboOAuth2UserService;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 基于spring security oauth2 client 扩展第三方登录
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-06-29 17:23:16
 */
public class SocialProviderConfigurer extends AbstractHttpConfigurer<SocialProviderConfigurer, HttpSecurity> {

	private final SocialDelegateClientRegistrationRepository socialDelegateClientRegistrationRepository;

	private Consumer<OAuth2LoginConfigurer<HttpSecurity>> oAuth2LoginConfigurerConsumer =
		oAuth2ProviderConfigurer -> {
		};

	/**
	 * Instantiates a new O auth 2 provider configurer.
	 *
	 * @param socialDelegateClientRegistrationRepository the delegate client registration repository
	 */
	public SocialProviderConfigurer(SocialDelegateClientRegistrationRepository socialDelegateClientRegistrationRepository) {
		this.socialDelegateClientRegistrationRepository = socialDelegateClientRegistrationRepository;
	}

	/**
	 * Wechat webclient o auth 2 provider configurer.
	 *
	 * @param appId  the app id
	 * @param secret the secret
	 * @return the o auth 2 provider configurer
	 */
	public SocialProviderConfigurer wechatWebclient(String appId, String secret) {
		ClientRegistration clientRegistration = getBuilder(
			SocialClientProviders.WECHAT_WEB_CLIENT.registrationId(), ClientAuthenticationMethod.NONE)
			.clientId(appId)
			.clientSecret(secret)
			.scope("snsapi_userinfo")
			.authorizationUri("https://open.weixin.qq.com/connect/oauth2/authorize")
			.tokenUri("https://api.weixin.qq.com/sns/oauth2/access_token")
			.userInfoUri("https://api.weixin.qq.com/sns/userinfo")
			.clientName("微信网页授权")
			.build();
		this.socialDelegateClientRegistrationRepository.addClientRegistration(clientRegistration);
		return this;
	}

	/**
	 * Wechat web loginclient o auth 2 provider configurer.
	 *
	 * @param appId  the app id
	 * @param secret the secret
	 * @return the o auth 2 provider configurer
	 */
	public SocialProviderConfigurer wechatWebLoginclient(String appId, String secret) {
		ClientRegistration clientRegistration = getBuilder(
			SocialClientProviders.WECHAT_WEB_LOGIN_CLIENT.registrationId(), ClientAuthenticationMethod.NONE)
			.clientId(appId)
			.clientSecret(secret)
			.scope("snsapi_login")
			.authorizationUri("https://open.weixin.qq.com/connect/qrconnect")
			.tokenUri("https://api.weixin.qq.com/sns/oauth2/access_token")
			.userInfoUri("https://api.weixin.qq.com/sns/userinfo")
			.clientName("微信扫码")
			.build();
		this.socialDelegateClientRegistrationRepository.addClientRegistration(clientRegistration);
		return this;
	}

	/**
	 * Work wechat web loginclient o auth 2 provider configurer.
	 *
	 * @param corpId  the corp id
	 * @param secret  the secret
	 * @param agentId the agent id
	 * @return the o auth 2 provider configurer
	 */
	public SocialProviderConfigurer workWechatWebLoginclient(String corpId, String secret, String agentId) {
		ClientRegistration clientRegistration = getBuilder(
			SocialClientProviders.WORK_WECHAT_SCAN_CLIENT.registrationId(), ClientAuthenticationMethod.NONE)
			.clientId(corpId)
			.clientSecret(secret)
			.scope(agentId)
			.authorizationUri("https://open.work.weixin.qq.com/wwopen/sso/qrConnect")
			.tokenUri("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
			.userInfoUri("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo")
			.clientName("企业微信")
			.build();
		this.socialDelegateClientRegistrationRepository.addClientRegistration(clientRegistration);
		return this;
	}

	/**
	 * O auth 2 login configurer consumer o auth 2 provider configurer.
	 *
	 * @param oAuth2LoginConfigurerConsumer the o auth 2 login configurer consumer
	 * @return the o auth 2 provider configurer
	 */
	public SocialProviderConfigurer oAuth2LoginConfigurerConsumer(
		Consumer<OAuth2LoginConfigurer<HttpSecurity>> oAuth2LoginConfigurerConsumer) {
		this.oAuth2LoginConfigurerConsumer = oAuth2LoginConfigurerConsumer;
		return this;
	}

	protected final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method) {
		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
		builder.clientAuthenticationMethod(method);
		builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
		builder.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}");
		return builder;
	}

	@Override
	public void init(HttpSecurity httpSecurity) throws Exception {
		OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
			new OAuth2AccessTokenResponseHttpMessageConverter();
		// 微信返回的content-type 是 text-plain
		tokenResponseHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
			MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, new MediaType("application", "*+json")));
		// 兼容微信解析
		tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(
			new SocialDelegateMapOAuth2AccessTokenResponseConverter());

		RestTemplate restTemplate = new RestTemplate(Arrays.asList(
			new FormHttpMessageConverter(),
			// 解析标准的AccessToken响应信息转换器
			tokenResponseHttpMessageConverter,
			// 解析qq的AccessToken响应信息转换器
			new QqOAuth2AccessTokenResponseHttpMessageConverter(
				MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN,
				new MediaType("application", "*+json"
				)
			)
		));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

		DefaultAuthorizationCodeTokenResponseClient tokenResponseClient =
			new DefaultAuthorizationCodeTokenResponseClient();
		tokenResponseClient.setRequestEntityConverter(new SocialOAuth2ProviderAuthorizationCodeGrantRequestEntityConverter());
		tokenResponseClient.setRestOperations(restTemplate);

		WechatOAuth2UserService wechatOAuth2UserService = new WechatOAuth2UserService();
		Map<String, OAuth2UserService<OAuth2UserRequest, OAuth2User>> oAuth2UserServiceMap = new HashMap<>();
		oAuth2UserServiceMap.put(SocialClientProviders.WECHAT_WEB_CLIENT.registrationId(), wechatOAuth2UserService);
		oAuth2UserServiceMap.put(SocialClientProviders.WECHAT_WEB_LOGIN_CLIENT.registrationId(), wechatOAuth2UserService);
		oAuth2UserServiceMap.put(
			SocialClientProviders.WORK_WECHAT_SCAN_CLIENT.registrationId(), new WorkWechatOAuth2UserService());
		oAuth2UserServiceMap.put("web", new WeiboOAuth2UserService());
		oAuth2UserServiceMap.put("qq", new QQOauth2UserService());
		oAuth2UserServiceMap.put("gitee", new GiteeOAuth2UserService());
		oAuth2UserServiceMap.put("github", new GithubOAuth2UserService());

		httpSecurity.setSharedObject(ClientRegistrationRepository.class, socialDelegateClientRegistrationRepository);

		DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
			socialDelegateClientRegistrationRepository,
			OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
		resolver.setAuthorizationRequestCustomizer(SocialOAuth2AuthorizationRequestCustomizer::customize);


		httpSecurity
			.oauth2Login(oauth2LoginCustomizer -> {
				oauth2LoginCustomizer
					.successHandler(authenticationSuccessHandler(httpSecurity))
					// 认证失败后的处理器
					.failureHandler((request, response, authException) -> {
						LogUtils.error("用户认证失败", authException);
						ResponseUtils.fail(response, authException.getMessage());
					})
					.authorizationEndpoint(authorizationEndpointCustomizer -> {
						// 授权端点配置
						authorizationEndpointCustomizer.authorizationRequestResolver(resolver);
					})
					// 获取token端点配置  比如根据code 获取 token
					.tokenEndpoint(tokenEndpointCustomizer -> {
						tokenEndpointCustomizer.accessTokenResponseClient(
							new SocialDelegateOAuth2AccessTokenResponseClient(tokenResponseClient, restTemplate));
					})
					// 获取用户信息端点配置  根据accessToken获取用户基本信息
					.userInfoEndpoint(userInfoEndpointCustomizer -> {
						userInfoEndpointCustomizer.userService(new SocialDelegatingOAuth2UserService<>(oAuth2UserServiceMap));
					});

				this.oAuth2LoginConfigurerConsumer.accept(oauth2LoginCustomizer);
			});
	}

	private AuthenticationSuccessHandler authenticationSuccessHandler(HttpSecurity httpSecurity) {
		ApplicationContext applicationContext = httpSecurity.getSharedObject(ApplicationContext.class);
		JwtTokenGenerator jwtTokenGenerator = applicationContext.getBean(JwtTokenGenerator.class);
		Assert.notNull(jwtTokenGenerator, "jwtTokenGenerator is required");

		return new SocialAuthenticationSuccessHandler(jwtTokenGenerator);
	}

	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		DefaultLoginPageGeneratingFilter loginPageGeneratingFilter =
			httpSecurity.getSharedObject(DefaultLoginPageGeneratingFilter.class);
		if (loginPageGeneratingFilter != null) {
			Map<String, String> loginUrlToClientName = new HashMap<>();
			socialDelegateClientRegistrationRepository.getClientRegistrationMap().forEach((s, v) -> {
				String authorizationRequestUri =
					OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
						+ "/"
						+ v.getRegistrationId();
				loginUrlToClientName.put(authorizationRequestUri, v.getClientName());
			});
			loginPageGeneratingFilter.setOauth2AuthenticationUrlToClientName(loginUrlToClientName);
		}
	}

	public HttpSecurity httpSecurity(){
		return getBuilder();
	}

}
