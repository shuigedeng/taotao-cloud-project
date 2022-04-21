///*
// * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
//package com.taotao.cloud.oauth2.api.oauth2_server.configuration;
//
//import com.taotao.cloud.common.constant.SecurityConstant;
//import com.taotao.cloud.core.model.SecurityUser;
//import com.taotao.cloud.oauth2.api.oauth2_server.component.RedisClientDetailsServiceComponent;
//import com.taotao.cloud.oauth2.api.oauth2_server.component.TokenGranterComponent;
//import com.taotao.cloud.oauth2.api.oauth2_server.component.UserAuthenticationConverterComponent;
//import com.taotao.cloud.redis.repository.RedisRepository;
//import com.taotao.cloud.security.configuration.WebResponseExceptionTranslatorComponent;
//import com.taotao.cloud.security.service.IUserDetailsService;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Objects;
//import javax.sql.DataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.access.AccessDeniedHandler;
//
///**
// * 授权服务器 主要是配置客户端信息和认证信息
// *
// * @author shuigedeng
// * @since 2020/4/29 20:01
// * @version 2022.03
// */
//@Configuration
//@EnableAuthorizationServer
//public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
//
//	private final AuthenticationManager authenticationManager;
//	private final DataSource dataSource;
//	private final TokenStore tokenStore;
//	private final AuthenticationEntryPoint authenticationEntryPoint;
//	private final AccessDeniedHandler accessDeniedHandler;
//	private final IUserDetailsService userDetailsService;
//	private final RedisRepository redisRepository;
//
//	/**
//	 * 用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化，
//	 * 客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
//	 *
//	 * @param clients clients
//	 * @author shuigedeng
//	 * @since 2020/4/29 20:01
//	 */
//	@Override
//	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		clients.withClientDetails(clientDetailsService());
//	}
//
//	/**
//	 * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services),告诉spring security token的生成方式。
//	 *
//	 * @param endpoints endpoints
//	 * @author shuigedeng
//	 * @since 2020/4/29 20:08
//	 */
//	@Override
//	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		endpoints
//			//指定token存储位置
//			.tokenStore(tokenStore)
//			.userDetailsService(userDetailsService)
//			//指定认证管理器,当你选择了资源所有者密码（password）授权类型的时候，需设置这个属性注入一个 AuthenticationManager 对象。
//			.authenticationManager(authenticationManager)
//			.exceptionTranslator(new WebResponseExceptionTranslatorComponent())
//			.tokenGranter(new TokenGranterComponent(endpoints, authenticationManager))
//			.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
//
//		DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
//		defaultAccessTokenConverter.setUserTokenConverter(new UserAuthenticationConverterComponent());
//
//		endpoints.tokenServices(defaultTokenServices());
//		endpoints.accessTokenConverter(defaultAccessTokenConverter);
//
//		endpoints.pathMapping("/oauth/confirm_access", "/custom/confirm_access");
//	}
//
//	@Bean
//	public ClientDetailsService clientDetailsService() {
//		RedisClientDetailsServiceComponent clientDetailsServiceImpl = new RedisClientDetailsServiceComponent(dataSource, redisRepository);
//		clientDetailsServiceImpl.setSelectClientDetailsSql(SecurityConstant.DEFAULT_SELECT);
//		clientDetailsServiceImpl.setFindClientDetailsSql(SecurityConstant.DEFAULT_FIND);
//		return clientDetailsServiceImpl;
//	}
//
//	@Primary
//	@Bean
//	public DefaultTokenServices defaultTokenServices() {
//		DefaultTokenServices tokenServices = new DefaultTokenServices();
//		tokenServices.setTokenStore(tokenStore);
//		tokenServices.setSupportRefreshToken(true);
//		tokenServices.setAccessTokenValiditySeconds(60 * 60 * 24 * 90);
//		tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 90);
//		tokenServices.setTokenEnhancer(tokenEnhancer());
//
//		return tokenServices;
//	}
//
//	@Bean
//	public TokenEnhancer tokenEnhancer() {
//		return (accessToken, authentication) -> {
//			final Map<String, Object> additionalInfo = new HashMap<>(4);
//			Authentication userAuthentication = authentication.getUserAuthentication();
//			if (Objects.nonNull(userAuthentication)) {
//				Object principal = userAuthentication.getPrincipal();
//
//				if (Objects.nonNull(principal)) {
//					if (principal instanceof SecurityUser) {
//						SecurityUser user = (SecurityUser) principal;
//						additionalInfo.put("userId", user.getUserId());
//						additionalInfo.put("username", user.getUsername());
//						additionalInfo.put("phone", user.getPhone());
//						additionalInfo.put("deptId", user.getDeptId());
//						additionalInfo.put("permissions", user.getPermissions() == null ? new HashSet<>() : user.getPermissions());
//						additionalInfo.put("roles", user.getRoles() == null ? new HashSet<>() : user.getRoles());
//					}
//				}
//			}
//
//			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
//			return accessToken;
//		};
//	}
//
//	/**
//	 * 用来配置令牌端点(Token Endpoint)的安全约束.
//	 *
//	 * @param securityConfigurer securityConfigurer
//	 * @author shuigedeng
//	 * @since 2020/4/29 20:12
//	 */
//	@Override
//	public void configure(AuthorizationServerSecurityConfigurer securityConfigurer) throws Exception {
//		securityConfigurer
//			// 允许客户表单认证,不加的话/oauth/token无法访问
//			.allowFormAuthenticationForClients()
//			// 对于CheckEndpoint控制器[框架自带的校验]的/oauth/token端点允许所有客户端发送器请求而不会被Spring-security拦截
//			// 开启/oauth/token_key验证端口无权限访问
//			.tokenKeyAccess("permitAll()")
//			// For endpoint /oauth/check_token, 用于资源服务访问的令牌解析端点
//			// 只有内部应用才能访问这个端点
//			// .checkTokenAccess("hasAuthority('INNER_CLIENT')")
//			.checkTokenAccess("permitAll()")
//			.authenticationEntryPoint(authenticationEntryPoint)
//			// ~ AccessDeniedHandler: called by ExceptionTranslationFilter when AccessDeniedException be thrown.
//			.accessDeniedHandler(accessDeniedHandler)
//		// ~ 为 /oauth/token 端点 (TokenEndpoint) 添加自定义的过滤器
//		// .addTokenEndpointAuthenticationFilter(
//		//         new CustomClientCredentialsTokenEndpointFilter(passwordEncoder, clientDetailsService(),
//		//                 authenticationEntryPoint))
//		;
//	}
//}
