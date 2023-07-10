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

import com.taotao.cloud.auth.biz.jpa.service.HerodotusAuthorizationConsentService;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.concurrent.TimeUnit;

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
public final class RedisOAuth2AuthorizationConsentService extends JpaOAuth2AuthorizationConsentService {

	/**
	 * 查询时放入Redis中的部分 key
	 */
	public static final String OAUTH2_AUTHORIZATION_CONSENT = ":oauth2_authorization_consent:";

	public static final String PREFIX = "spring-authorization-server";

	public static final long AUTHORIZATION_CONSENT_TIMEOUT = 300L;

	private final RedisRepository redisRepository;

	public RedisOAuth2AuthorizationConsentService(HerodotusAuthorizationConsentService herodotusAuthorizationConsentService,
												  RegisteredClientRepository registeredClientRepository,
												  RedisRepository redisRepository) {
		super(herodotusAuthorizationConsentService, registeredClientRepository);
		this.redisRepository = redisRepository;
	}

	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		if (authorizationConsent != null) {
			set(authorizationConsent, AUTHORIZATION_CONSENT_TIMEOUT, TimeUnit.SECONDS);
			super.save(authorizationConsent);
		}
	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		if (authorizationConsent != null) {
			String registeredClientId = authorizationConsent.getRegisteredClientId();
			String principalName = authorizationConsent.getPrincipalName();
			redisRepository.del(principalName + OAUTH2_AUTHORIZATION_CONSENT + registeredClientId + ":" + principalName);
			super.remove(authorizationConsent);
		}
	}


	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {

		OAuth2AuthorizationConsent oauth2AuthorizationConsentRedis = (OAuth2AuthorizationConsent) redisRepository.opsForValue().get(PREFIX + OAUTH2_AUTHORIZATION_CONSENT + registeredClientId  + ":"+ principalName);

		OAuth2AuthorizationConsent oauth2AuthorizationResult;
		OAuth2AuthorizationConsent oauth2AuthorizationByDatabase;

		if (oauth2AuthorizationConsentRedis == null) {
			oauth2AuthorizationByDatabase = super.findById(registeredClientId,
				principalName);
			LogUtils.debug("根据 registeredClientId：{}、principalName：{} 直接查询数据库中的授权：{}", registeredClientId, principalName,
				oauth2AuthorizationByDatabase);
			if (oauth2AuthorizationByDatabase != null) {
				set(oauth2AuthorizationByDatabase, AUTHORIZATION_CONSENT_TIMEOUT, TimeUnit.SECONDS);
			}
			oauth2AuthorizationResult = oauth2AuthorizationByDatabase;
		} else {
			LogUtils.debug("根据 registeredClientId：{}、principalName：{} 直接查询Redis中的授权：{}", registeredClientId, principalName,
				oauth2AuthorizationConsentRedis);
			oauth2AuthorizationResult = oauth2AuthorizationConsentRedis;
		}

		return oauth2AuthorizationResult;
	}

	public void set(OAuth2AuthorizationConsent authorizationConsent, long timeout, TimeUnit unit) {
		String registeredClientId = authorizationConsent.getRegisteredClientId();
		String principalName = authorizationConsent.getPrincipalName();

		redisRepository.opsForValue().set(PREFIX + OAUTH2_AUTHORIZATION_CONSENT + registeredClientId + ":"+ principalName , authorizationConsent, timeout, unit);
	}

}
