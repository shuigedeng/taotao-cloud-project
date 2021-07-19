///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.security.configuration;
//
//import com.taotao.cloud.common.constant.StarterNameConstant;
//import com.taotao.cloud.common.utils.LogUtil;
//import com.taotao.cloud.security.component.UserAuthenticationConverterComponent;
//import com.taotao.cloud.security.properties.SecurityProperties;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
//import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
//import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
//import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.Resource;
//
///**
// * oauth2资源服务器
// *
// * @author shuigedeng
// * @version 1.0.0
// * @since 2020/4/30 09:04
// */
//@Order(6)
//@EnableResourceServer
//@ConditionalOnProperty(prefix = "taotao.cloud.oauth2.security", name = "enabled", havingValue = "true")
//public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter implements
//	InitializingBean {
//
//	@Resource
//	private AuthenticationEntryPoint authenticationEntryPoint;
//	@Resource
//	private OAuth2WebSecurityExpressionHandler expressionHandler;
//	@Resource
//	private OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler;
//	@Resource
//	private SecurityProperties securityProperties;
//	// @Resource
//	// private AuthorizationServerProperties authorizationServerProperties;
//	// @Autowired
//	// private OAuth2ClientProperties oAuth2ClientProperties;
//
//	@Override
//	public void afterPropertiesSet() {
//		LogUtil.info(
//			"[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_AUTH_STARTER + "]" + "资源服务器已启动");
//	}
//
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl = setHttp(
//			http)
//			.authorizeRequests()
//			// .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
//			.antMatchers(securityProperties.getIgnore().getUrls()).permitAll()
//			.antMatchers(HttpMethod.OPTIONS).permitAll()
//			.anyRequest();
//
//		setAuthenticate(authorizedUrl);
//
//		http.sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
//			.httpBasic().disable()
//			.headers()
//			.frameOptions().disable()
//			.and()
//			.csrf().disable();
//	}
//
//	@Override
//	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//		resources.resourceId("taotao-cloud-uc-service")
//			.tokenServices(remoteTokenServices())
//			.stateless(true)
//			.authenticationEntryPoint(authenticationEntryPoint)
//			.expressionHandler(expressionHandler)
//			.accessDeniedHandler(oAuth2AccessDeniedHandler);
//	}
//
//	public RemoteTokenServices remoteTokenServices() {
//		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
//		UserAuthenticationConverter userTokenConverter = new UserAuthenticationConverterComponent();
//		accessTokenConverter.setUserTokenConverter(userTokenConverter);
//
//		final RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
//		remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9800/oauth/check_token");
//		remoteTokenServices.setClientId("taotao-cloud-uc-service");
//		remoteTokenServices.setClientSecret("taotao-cloud-uc-service");
//		// remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
//		remoteTokenServices.setRestTemplate(new RestTemplate());
//
//		// RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
//		// remoteTokenServices.setCheckTokenEndpointUrl(authorizationServerProperties.getCheckTokenAccess());
//		// remoteTokenServices.setClientId(oAuth2ClientProperties.g());
//		// remoteTokenServices.setClientSecret(oAuth2ClientProperties.getClientSecret());
//		// remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
//		return remoteTokenServices;
//	}
//
//	/**
//	 * 留给子类重写扩展功能
//	 *
//	 * @param http http
//	 */
//	public HttpSecurity setHttp(HttpSecurity http) {
//		return http;
//	}
//
//	/**
//	 * url权限控制，默认是认证就通过，可以重写实现个性化
//	 *
//	 * @param authorizedUrl authorizedUrl
//	 */
//	public HttpSecurity setAuthenticate(
//		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl) {
//		return authorizedUrl.authenticated().and();
//	}
//}
