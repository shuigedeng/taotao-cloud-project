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

package com.taotao.cloud.auth.infrastructure.authentication.extension;

import com.taotao.cloud.auth.infrastructure.utils.OAuth2AuthenticationProviderUtils;
import com.taotao.boot.security.spring.authority.TtcGrantedAuthority;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * <p>扩展的 OAuth2ClientCredentialsAuthenticationProvider</p>
 * <p>
 * 用于支持 客户端权限验证 以及 支持 Refresh_Token
 *
 * @author shuigedeng
 * @version 2023.07
 * @see OAuth2AbstractAuthenticationProvider
 * @since 2023-07-10 17:39:52
 */
public class OAuth2ClientCredentialsAuthenticationProvider extends
	OAuth2AbstractAuthenticationProvider {

	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(
		OAuth2ClientCredentialsAuthenticationProvider.class);

	/**
	 * 错误uri
	 */
	private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
	/**
	 * 授权服务
	 */
	private final OAuth2AuthorizationService authorizationService;
	/**
	 * 令牌生成器
	 */
	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
	/**
	 * 客户详细信息服务
	 */
	private final ClientDetailsService clientDetailsService;

	/**
	 * Constructs an {@code OAuth2ClientCredentialsAuthenticationProvider} using the provided
	 * parameters.
	 *
	 * @param authorizationService the authorization service
	 * @param tokenGenerator       the token generator
	 * @param clientDetailsService 客户详细信息服务
	 * @return
	 * @since 2023-07-10 17:39:52
	 */
	public OAuth2ClientCredentialsAuthenticationProvider(
		OAuth2AuthorizationService authorizationService,
		OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
		ClientDetailsService clientDetailsService) {
		Assert.notNull(authorizationService, "authorizationService cannot be null");
		Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
		this.clientDetailsService = clientDetailsService;
	}

	/**
	 * 验证
	 *
	 * @param authentication 身份验证
	 * @return {@link Authentication }
	 * @since 2023-07-10 17:39:52
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
		throws AuthenticationException {
		OAuth2ClientCredentialsAuthenticationToken clientCredentialsAuthentication =
			(OAuth2ClientCredentialsAuthenticationToken) authentication;

		OAuth2ClientAuthenticationToken clientPrincipal =
			OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient(
				clientCredentialsAuthentication);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		if (!registeredClient.getAuthorizationGrantTypes()
			.contains(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}

		log.trace("Retrieved registered client");

		// Default to configured scopes
		Set<String> authorizedScopes = getStrings(clientCredentialsAuthentication,
			registeredClient);

		Set<TtcGrantedAuthority> authorities =
			clientDetailsService.findAuthoritiesById(registeredClient.getClientId());
		if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(authorities)) {
			FieldUtil.setFieldValue(clientPrincipal, "authorities", authorities);
			log.debug("Assign authorities to OAuth2ClientAuthenticationToken.");
		}

		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(
				registeredClient)
			.principalName(clientPrincipal.getName())
			.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
			.authorizedScopes(authorizedScopes);

		// @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(clientPrincipal)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrant(clientCredentialsAuthentication);
        // @formatter:on

		// ----- Access token -----
		OAuth2AccessToken accessToken =
			createOAuth2AccessToken(tokenContextBuilder, authorizationBuilder, this.tokenGenerator,
				ERROR_URI);

		OAuth2Authorization authorization = authorizationBuilder.build();

		this.authorizationService.save(authorization);

		log.debug("Client Credentials returning OAuth2AccessTokenAuthenticationToken.");

		return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal,
			accessToken);
	}

	/**
	 * 获取字符串
	 *
	 * @param clientCredentialsAuthentication 客户端凭证认证
	 * @param registeredClient                注册客户
	 * @return {@link Set }<{@link String }>
	 * @since 2023-07-10 17:39:53
	 */
	@NotNull
	private static Set<String> getStrings(
		OAuth2ClientCredentialsAuthenticationToken clientCredentialsAuthentication,
		RegisteredClient registeredClient) {
		Set<String> authorizedScopes = Collections.emptySet();
		if (!CollectionUtils.isEmpty(clientCredentialsAuthentication.getScopes())) {
			for (String requestedScope : clientCredentialsAuthentication.getScopes()) {
				if (!registeredClient.getScopes().contains(requestedScope)) {
					throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
				}
			}
			authorizedScopes = new LinkedHashSet<>(clientCredentialsAuthentication.getScopes());
		}
		return authorizedScopes;
	}

	/**
	 * 支持
	 *
	 * @param authentication 身份验证
	 * @return boolean
	 * @since 2023-07-10 17:39:53
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2ClientCredentialsAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
