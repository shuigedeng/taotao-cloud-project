/*
 * Copyright 2020-2021 the original author or authors.
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
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An {@link OAuth2AuthorizationConsentService} that stores {@link OAuth2AuthorizationConsent}'s in-memory.
 *
 * <p>
 * <b>NOTE:</b> This implementation should ONLY be used during development/testing.
 *
 * @author Daniel Garnier-Moiroux
 * @see OAuth2AuthorizationConsentService
 * @since 0.1.2
 */
public final class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

	private final RedisRepository redisRepository;

	/**
	 * Constructs an {@code InMemoryOAuth2AuthorizationConsentService}.
	 */
	public RedisOAuth2AuthorizationConsentService(RedisRepository redisRepository) {
		this(redisRepository, Collections.emptyList());
	}

	/**
	 * Constructs an {@code InMemoryOAuth2AuthorizationConsentService} using the provided parameters.
	 *
	 * @param authorizationConsents the authorization consent(s)
	 */
	public RedisOAuth2AuthorizationConsentService(RedisRepository redisRepository,
												  OAuth2AuthorizationConsent... authorizationConsents) {
		this(redisRepository, Arrays.asList(authorizationConsents));
	}

	/**
	 * Constructs an {@code InMemoryOAuth2AuthorizationConsentService} using the provided parameters.
	 *
	 * @param authorizationConsents the authorization consent(s)
	 */
	public RedisOAuth2AuthorizationConsentService(RedisRepository redisRepository,
												  List<OAuth2AuthorizationConsent> authorizationConsents) {
		Assert.notNull(authorizationConsents, "authorizationConsents cannot be null");
		this.redisRepository = redisRepository;

		authorizationConsents.forEach(authorizationConsent -> {
			Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
			int id = getId(authorizationConsent);
			Object authorizationConsentObj = redisRepository.get(String.valueOf(id));

			Assert.isTrue(Objects.isNull(authorizationConsentObj),
				"The authorizationConsent must be unique. Found duplicate, with registered client id: ["
					+ authorizationConsent.getRegisteredClientId()
					+ "] and principal name: [" + authorizationConsent.getPrincipalName() + "]");

			this.redisRepository.set(String.valueOf(id), authorizationConsent);
		});
	}

	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		int id = getId(authorizationConsent);
		this.redisRepository.set(String.valueOf(id), authorizationConsent);
	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		int id = getId(authorizationConsent);
		this.redisRepository.del(String.valueOf(id));
	}

	@Override
	@Nullable
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		int id = getId(registeredClientId, principalName);
		Object authorizationConsentObj = redisRepository.get(String.valueOf(id));
		return (OAuth2AuthorizationConsent) authorizationConsentObj;
	}

	private static int getId(String registeredClientId, String principalName) {
		return Objects.hash(registeredClientId, principalName);
	}

	private static int getId(OAuth2AuthorizationConsent authorizationConsent) {
		return getId(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}

}
