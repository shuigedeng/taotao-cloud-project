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
package com.taotao.cloud.auth.biz.service;

import java.security.Principal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * CloudJdbcOAuth2AuthorizationConsentService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/12/07 22:02
 */
public class CloudJdbcOAuth2AuthorizationConsentService extends
	JdbcOAuth2AuthorizationConsentService {

	public CloudJdbcOAuth2AuthorizationConsentService(
		JdbcTemplate jdbcTemplate,
		RegisteredClientRepository registeredClientRepository) {
		super(jdbcTemplate, registeredClientRepository);
	}

	/**
	 * Saves the {@link OAuth2AuthorizationConsent}.
	 *
	 * @param authorizationConsent the {@link OAuth2AuthorizationConsent}
	 */
	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		super.save(authorizationConsent);
	}

	/**
	 * Removes the {@link OAuth2AuthorizationConsent}.
	 *
	 * @param authorizationConsent the {@link OAuth2AuthorizationConsent}
	 */
	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		super.remove(authorizationConsent);
	}

	/**
	 * Returns the {@link OAuth2AuthorizationConsent} identified by the provided
	 * {@code registeredClientId} and {@code principalName}, or {@code null} if not found.
	 *
	 * @param registeredClientId the identifier for the {@link RegisteredClient}
	 * @param principalName      the name of the {@link Principal}
	 * @return the {@link OAuth2AuthorizationConsent} if found, otherwise {@code null}
	 */
	@Override
	@Nullable
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		return super.findById(registeredClientId, principalName);
	}


}
