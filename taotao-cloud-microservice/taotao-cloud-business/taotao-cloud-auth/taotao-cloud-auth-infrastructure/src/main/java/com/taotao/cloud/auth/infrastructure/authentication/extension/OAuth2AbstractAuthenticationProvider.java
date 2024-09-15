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

import com.taotao.boot.security.spring.core.userdetails.TtcUser;
import com.taotao.boot.security.spring.utils.PrincipalUtils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;

/**
 * <p>基于spring security oauth2 server 扩展oauth2登录方式(账号+密码, justauth第三方登录) </p>
 * <p>
 * 提取公共的通用认证基类，方便设置返回Token的信息设置
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:36:28
 */
public abstract class OAuth2AbstractAuthenticationProvider implements AuthenticationProvider {

	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(
		OAuth2AbstractAuthenticationProvider.class);

	/**
	 * id令牌令牌类型
	 */
	private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(
		OidcParameterNames.ID_TOKEN);

	/**
	 * 创建oauth2访问令牌
	 *
	 * @param tokenContextBuilder  令牌上下文生成器
	 * @param authorizationBuilder 授权生成器
	 * @param tokenGenerator       令牌生成器
	 * @param errorUri             错误uri
	 * @return {@link OAuth2AccessToken }
	 * @since 2023-07-10 17:36:29
	 */
	protected OAuth2AccessToken createOAuth2AccessToken(
		DefaultOAuth2TokenContext.Builder tokenContextBuilder,
		OAuth2Authorization.Builder authorizationBuilder,
		OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
		String errorUri) {
		OAuth2TokenContext tokenContext =
			tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
		OAuth2Token generatedAccessToken = tokenGenerator.generate(tokenContext);
		if (generatedAccessToken == null) {
			OAuth2Error error = new OAuth2Error(
				OAuth2ErrorCodes.SERVER_ERROR,
				"The token generator failed to generate the access token.",
				errorUri);
			throw new OAuth2AuthenticationException(error);
		}

		log.trace("Generated access token");

		OAuth2AccessToken accessToken = new OAuth2AccessToken(
			OAuth2AccessToken.TokenType.BEARER,
			generatedAccessToken.getTokenValue(),
			generatedAccessToken.getIssuedAt(),
			generatedAccessToken.getExpiresAt(),
			tokenContext.getAuthorizedScopes());

		if (generatedAccessToken instanceof ClaimAccessor) {
			authorizationBuilder.token(
				accessToken,
				(metadata) -> metadata.put(
					OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
					((ClaimAccessor) generatedAccessToken).getClaims()));
		}
		else {
			authorizationBuilder.accessToken(accessToken);
		}

		return accessToken;
	}

	/**
	 * 创建oauth2刷新令牌
	 *
	 * @param tokenContextBuilder  令牌上下文生成器
	 * @param authorizationBuilder 授权生成器
	 * @param tokenGenerator       令牌生成器
	 * @param errorUri             错误uri
	 * @param clientPrincipal      客户委托人
	 * @param registeredClient     注册客户
	 * @return {@link OAuth2RefreshToken }
	 * @since 2023-07-10 17:36:30
	 */
	protected OAuth2RefreshToken creatOAuth2RefreshToken(
		DefaultOAuth2TokenContext.Builder tokenContextBuilder,
		OAuth2Authorization.Builder authorizationBuilder,
		OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
		String errorUri,
		OAuth2ClientAuthenticationToken clientPrincipal,
		RegisteredClient registeredClient) {
		OAuth2RefreshToken refreshToken = null;
		if (registeredClient.getAuthorizationGrantTypes()
			.contains(AuthorizationGrantType.REFRESH_TOKEN)
			&&
			// Do not issue refresh token to public client
			!clientPrincipal.getClientAuthenticationMethod()
				.equals(ClientAuthenticationMethod.NONE)) {

			OAuth2TokenContext tokenContext =
				tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
			OAuth2Token generatedRefreshToken = tokenGenerator.generate(tokenContext);
			if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
				OAuth2Error error = new OAuth2Error(
					OAuth2ErrorCodes.SERVER_ERROR,
					"The token generator failed to generate the refresh token.",
					errorUri);
				throw new OAuth2AuthenticationException(error);
			}

			log.trace("Generated refresh token");

			refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
			authorizationBuilder.refreshToken(refreshToken);
		}

		return refreshToken;
	}

	/**
	 * 创建oidc id令牌
	 *
	 * @param principal            校长
	 * @param sessionRegistry      会话注册表
	 * @param tokenContextBuilder  令牌上下文生成器
	 * @param authorizationBuilder 授权生成器
	 * @param tokenGenerator       令牌生成器
	 * @param errorUri             错误uri
	 * @param requestedScopes      请求范围
	 * @return {@link OidcIdToken }
	 * @since 2023-07-10 17:36:31
	 */
	protected OidcIdToken createOidcIdToken(
		Authentication principal,
		SessionRegistry sessionRegistry,
		DefaultOAuth2TokenContext.Builder tokenContextBuilder,
		OAuth2Authorization.Builder authorizationBuilder,
		OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
		String errorUri,
		Set<String> requestedScopes) {
		OidcIdToken idToken;
		if (requestedScopes.contains(OidcScopes.OPENID)) {

			SessionInformation sessionInformation = getSessionInformation(principal,
				sessionRegistry);
			if (sessionInformation != null) {
				try {
					// Compute (and use) hash for Session ID
					sessionInformation = new SessionInformation(
						sessionInformation.getPrincipal(),
						createHash(sessionInformation.getSessionId()),
						sessionInformation.getLastRequest());
				}
				catch (NoSuchAlgorithmException ex) {
					OAuth2Error error = new OAuth2Error(
						OAuth2ErrorCodes.SERVER_ERROR, "Failed to compute hash for Session ID.",
						errorUri);
					throw new OAuth2AuthenticationException(error);
				}
				tokenContextBuilder.put(SessionInformation.class, sessionInformation);
			}

			OAuth2TokenContext tokenContext = tokenContextBuilder
				.tokenType(ID_TOKEN_TOKEN_TYPE)
				.authorization(
					authorizationBuilder
						.build()) // ID token customizer may need access to the access token and/or refresh
				// token
				.build();

			OAuth2Token generatedIdToken = tokenGenerator.generate(tokenContext);
			if (!(generatedIdToken instanceof Jwt)) {
				OAuth2Error error = new OAuth2Error(
					OAuth2ErrorCodes.SERVER_ERROR,
					"The token generator failed to generate the ID token.",
					errorUri);
				throw new OAuth2AuthenticationException(error);
			}

			log.trace("Generated id token");

			idToken = new OidcIdToken(
				generatedIdToken.getTokenValue(),
				generatedIdToken.getIssuedAt(),
				generatedIdToken.getExpiresAt(),
				((Jwt) generatedIdToken).getClaims());
			authorizationBuilder.token(
				idToken,
				(metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
					idToken.getClaims()));
		}
		else {
			idToken = null;
		}

		return idToken;
	}

	/**
	 * 获取会话信息
	 *
	 * @param principal       校长
	 * @param sessionRegistry 会话注册表
	 * @return {@link SessionInformation }
	 * @since 2023-07-10 17:36:32
	 */
	private SessionInformation getSessionInformation(Authentication principal,
		SessionRegistry sessionRegistry) {
		SessionInformation sessionInformation = null;
		if (sessionRegistry != null) {
			List<SessionInformation> sessions = sessionRegistry.getAllSessions(
				principal.getPrincipal(), false);
			if (!CollectionUtils.isEmpty(sessions)) {
				sessionInformation = sessions.get(0);
				if (sessions.size() > 1) {
					// Get the most recent session
					sessions = new ArrayList<>(sessions);
					sessions.sort(Comparator.comparing(SessionInformation::getLastRequest));
					sessionInformation = sessions.get(sessions.size() - 1);
				}
			}
		}
		return sessionInformation;
	}

	/**
	 * 创建哈希
	 *
	 * @param value 值
	 * @return {@link String }
	 * @since 2023-07-10 17:36:32
	 */
	private static String createHash(String value) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(value.getBytes(StandardCharsets.US_ASCII));
		return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
	}

	/**
	 * id令牌附加参数
	 *
	 * @param idToken id令牌
	 * @return {@link Map }<{@link String }, {@link Object }>
	 * @since 2023-07-10 17:36:32
	 */
	protected Map<String, Object> idTokenAdditionalParameters(OidcIdToken idToken) {
		Map<String, Object> additionalParameters = Collections.emptyMap();
		if (idToken != null) {
			additionalParameters = new HashMap<>();
			additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
		}
		return additionalParameters;
	}

	/**
	 * 验证范围
	 *
	 * @param requestedScopes  请求范围
	 * @param registeredClient 注册客户
	 * @return {@link Set }<{@link String }>
	 * @since 2023-07-10 17:36:33
	 */
	protected Set<String> validateScopes(Set<String> requestedScopes,
		RegisteredClient registeredClient) {
		Set<String> authorizedScopes = Collections.emptySet();
		if (!CollectionUtils.isEmpty(requestedScopes)) {
			for (String requestedScope : requestedScopes) {
				if (!registeredClient.getScopes().contains(requestedScope)) {
					throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
				}
			}
			authorizedScopes = new LinkedHashSet<>(requestedScopes);
		}
		return authorizedScopes;
	}

	/**
	 * 创建oauth2访问令牌身份验证令牌
	 *
	 * @param source      来源
	 * @param destination 目地
	 * @return {@link OAuth2AccessTokenAuthenticationToken }
	 * @since 2023-07-10 17:36:34
	 */
	protected OAuth2AccessTokenAuthenticationToken createOAuth2AccessTokenAuthenticationToken(
		Authentication source, OAuth2AccessTokenAuthenticationToken destination) {
		if (source instanceof UsernamePasswordAuthenticationToken) {
			if (source.getPrincipal() instanceof TtcUser user) {
				destination.setDetails(PrincipalUtils.toPrincipalDetails(user));
			}
		}

		return destination;
	}
}
