/*
 * Copyright 2020-2023 the original author or authors.
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
package com.taotao.cloud.auth.biz.jpa.storage;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * An {@link OAuth2AuthorizationService} that stores {@link OAuth2Authorization}'s in-memory.
 *
 * <p>
 * <b>NOTE:</b> This implementation should ONLY be used during development/testing.
 *
 * @author Krisztian Toth
 * @author Joe Grandja
 * @see OAuth2AuthorizationService
 * @since 0.0.1
 */
public final class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {
	private RedisRepository redisRepository;


	/**
	 * Constructs an {@code InMemoryOAuth2AuthorizationService}.
	 */
	public RedisOAuth2AuthorizationService(RedisRepository redisRepository) {
		this(redisRepository, Collections.emptyList());
	}

	/**
	 * Constructs an {@code InMemoryOAuth2AuthorizationService} using the provided parameters.
	 *
	 * @param authorizations the authorization(s)
	 */
	public RedisOAuth2AuthorizationService(RedisRepository redisRepository,OAuth2Authorization... authorizations) {
		this(redisRepository, Arrays.asList(authorizations));
	}

	/**
	 * Constructs an {@code InMemoryOAuth2AuthorizationService} using the provided parameters.
	 *
	 * @param authorizations the authorization(s)
	 */
	public RedisOAuth2AuthorizationService( RedisRepository redisRepository, List<OAuth2Authorization> authorizations) {
		Assert.notNull(authorizations, "authorizations cannot be null");

		this.redisRepository = redisRepository;

		authorizations.forEach(authorization -> {
			Assert.notNull(authorization, "authorization cannot be null");

			Object authorizationObj = this.redisRepository.get(authorization.getId());

			Assert.isTrue(Objects.isNull(authorizationObj),
				"The authorization must be unique. Found duplicate identifier: " + authorization.getId());

			this.redisRepository.set(authorization.getId(), authorization);
		});
	}

	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");

		//todo 此处代码可以优化

		//if (isComplete(authorization)) {
		//	this.authorizations.put(authorization.getId(), authorization);
		//} else {
		//	this.initializedAuthorizations.put(authorization.getId(), authorization);
		//}

		redisRepository.set(authorization.getId(), authorization);

//		OAuth2RefreshToken oAuth2RefreshToken = authorization.getRefreshToken().getToken();
//		long oAuth2RefreshTokenTimeOut = ChronoUnit.SECONDS.between(oAuth2RefreshToken.getIssuedAt(), oAuth2RefreshToken.getExpiresAt());
//		redisRepository.getRedisTemplate().opsForValue().set(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, oAuth2RefreshToken.getTokenValue()),
//			authorization, oAuth2RefreshTokenTimeOut, TimeUnit.SECONDS);
//
//		OAuth2AccessToken oAuth2AccessToken = authorization.getAccessToken().getToken();
//		long oAuth2AccessTokenTimeOut = ChronoUnit.SECONDS.between(oAuth2AccessToken.getIssuedAt(), oAuth2RefreshToken.getExpiresAt());
//		redisRepository.getRedisTemplate().opsForValue().set(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, oAuth2AccessToken.getTokenValue()),
//			authorization, oAuth2AccessTokenTimeOut, TimeUnit.SECONDS);
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		//if (isComplete(authorization)) {
		//	this.authorizations.remove(authorization.getId(), authorization);
		//} else {
		//	this.initializedAuthorizations.remove(authorization.getId(), authorization);
		//}

		redisRepository.del(authorization.getId());
	}

	@Nullable
	@Override
	public OAuth2Authorization findById(String id) {
		Assert.hasText(id, "id cannot be empty");
		//OAuth2Authorization authorization = this.authorizations.get(id);
		//return authorization != null ?
		//	authorization :
		//	this.initializedAuthorizations.get(id);

		return (OAuth2Authorization)redisRepository.get(id);
	}

	@Nullable
	@Override
	public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
		Assert.hasText(token, "token cannot be empty");
		//for (OAuth2Authorization authorization : this.authorizations.values()) {
		//	if (hasToken(authorization, token, tokenType)) {
		//		return authorization;
		//	}
		//}
		//for (OAuth2Authorization authorization : this.initializedAuthorizations.values()) {
		//	if (hasToken(authorization, token, tokenType)) {
		//		return authorization;
		//	}
		//}
		return null;
	}

	private static boolean isComplete(OAuth2Authorization authorization) {
		return authorization.getAccessToken() != null;
	}

	private static boolean hasToken(OAuth2Authorization authorization, String token, @Nullable OAuth2TokenType tokenType) {
		if (tokenType == null) {
			return matchesState(authorization, token) ||
				matchesAuthorizationCode(authorization, token) ||
				matchesAccessToken(authorization, token) ||
				matchesIdToken(authorization, token) ||
				matchesRefreshToken(authorization, token) ||
				matchesDeviceCode(authorization, token) ||
				matchesUserCode(authorization, token);
		} else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
			return matchesState(authorization, token);
		} else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
			return matchesAuthorizationCode(authorization, token);
		} else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
			return matchesAccessToken(authorization, token);
		} else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
			return matchesIdToken(authorization, token);
		} else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
			return matchesRefreshToken(authorization, token);
		} else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
			return matchesDeviceCode(authorization, token);
		} else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
			return matchesUserCode(authorization, token);
		}
		return false;
	}

	private static boolean matchesState(OAuth2Authorization authorization, String token) {
		return token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE));
	}

	private static boolean matchesAuthorizationCode(OAuth2Authorization authorization, String token) {
		OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
			authorization.getToken(OAuth2AuthorizationCode.class);
		return authorizationCode != null && authorizationCode.getToken().getTokenValue().equals(token);
	}

	private static boolean matchesAccessToken(OAuth2Authorization authorization, String token) {
		OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
			authorization.getToken(OAuth2AccessToken.class);
		return accessToken != null && accessToken.getToken().getTokenValue().equals(token);
	}

	private static boolean matchesRefreshToken(OAuth2Authorization authorization, String token) {
		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
			authorization.getToken(OAuth2RefreshToken.class);
		return refreshToken != null && refreshToken.getToken().getTokenValue().equals(token);
	}

	private static boolean matchesIdToken(OAuth2Authorization authorization, String token) {
		OAuth2Authorization.Token<OidcIdToken> idToken =
			authorization.getToken(OidcIdToken.class);
		return idToken != null && idToken.getToken().getTokenValue().equals(token);
	}

	private static boolean matchesDeviceCode(OAuth2Authorization authorization, String token) {
		OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode =
			authorization.getToken(OAuth2DeviceCode.class);
		return deviceCode != null && deviceCode.getToken().getTokenValue().equals(token);
	}

	private static boolean matchesUserCode(OAuth2Authorization authorization, String token) {
		OAuth2Authorization.Token<OAuth2UserCode> userCode =
			authorization.getToken(OAuth2UserCode.class);
		return userCode != null && userCode.getToken().getTokenValue().equals(token);
	}

	private static final class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
		private final int maxSize;

		private MaxSizeHashMap(int maxSize) {
			this.maxSize = maxSize;
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
			return size() > this.maxSize;
		}

	}

	private String buildKey(String key, String token) {
		return key + ":" + token;
	}
}
