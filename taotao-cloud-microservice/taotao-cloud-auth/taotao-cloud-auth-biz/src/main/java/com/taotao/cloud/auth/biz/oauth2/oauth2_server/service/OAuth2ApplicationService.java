/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.oauth2.oauth2_server.service;

import cn.hutool.core.date.DateUtil;
import com.taotao.cloud.auth.biz.oauth2.core.properties.SecurityProperties;
import com.taotao.cloud.auth.biz.oauth2.jpa.repository.HerodotusRegisteredClientRepository;
import com.taotao.cloud.auth.biz.oauth2.jpa.utils.OAuth2AuthorizationUtils;
import com.taotao.cloud.auth.biz.oauth2.oauth2_server.entity.OAuth2Application;
import com.taotao.cloud.auth.biz.oauth2.oauth2_server.entity.OAuth2Scope;
import com.taotao.cloud.auth.biz.oauth2.oauth2_server.repository.OAuth2ApplicationRepository;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>Description: OAuth2ApplicationService </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/1 18:06
 */
@Service
public class OAuth2ApplicationService extends BaseLayeredService<OAuth2Application, String> {

	private static final Logger log = LoggerFactory.getLogger(OAuth2ApplicationService.class);

	private final RegisteredClientRepository registeredClientRepository;
	private final HerodotusRegisteredClientRepository herodotusRegisteredClientRepository;
	private final OAuth2ApplicationRepository applicationRepository;
	private final SecurityProperties securityProperties;

	@Autowired
	public OAuth2ApplicationService(RegisteredClientRepository registeredClientRepository,
		HerodotusRegisteredClientRepository herodotusRegisteredClientRepository,
		OAuth2ApplicationRepository applicationRepository, SecurityProperties securityProperties) {
		this.registeredClientRepository = registeredClientRepository;
		this.herodotusRegisteredClientRepository = herodotusRegisteredClientRepository;
		this.applicationRepository = applicationRepository;
		this.securityProperties = securityProperties;
	}

	@Override
	public BaseRepository<OAuth2Application, String> getRepository() {
		return this.applicationRepository;
	}

	@Transactional(rollbackFor = TransactionRollbackException.class)
	@Override
	public OAuth2Application saveOrUpdate(OAuth2Application entity) {
		OAuth2Application application = super.saveOrUpdate(entity);
		if (ObjectUtils.isNotEmpty(application)) {
			registeredClientRepository.save(toRegisteredClient(application));
			log.debug("[Herodotus] |- OAuth2ApplicationService saveOrUpdate.");
			return application;
		} else {
			log.error("[Herodotus] |- OAuth2ApplicationService saveOrUpdate error, rollback data!");
			throw new NullPointerException("save or update OAuth2Application failed");
		}
	}

	@Transactional(rollbackFor = TransactionRollbackException.class)
	@Override
	public void deleteById(String id) {
		super.deleteById(id);
		herodotusRegisteredClientRepository.deleteById(id);
		log.debug("[Herodotus] |- OAuth2ApplicationService deleteById.");
	}

	public OAuth2Application authorize(String applicationId, String[] scopeIds) {

		Set<OAuth2Scope> scopes = new HashSet<>();
		for (String scopeId : scopeIds) {
			OAuth2Scope scope = new OAuth2Scope();
			scope.setScopeId(scopeId);
			scopes.add(scope);
		}

		OAuth2Application oldApplication = findById(applicationId);
		oldApplication.setScopes(scopes);

		OAuth2Application newApplication = saveOrUpdate(oldApplication);
		log.debug("[Herodotus] |- OAuth2ApplicationService assign.");
		return newApplication;
	}

	public OAuth2Application findByClientId(String clientId) {
		OAuth2Application application = applicationRepository.findByClientId(clientId);
		log.debug("[Herodotus] |- OAuth2ApplicationService findByClientId.");
		return application;
	}

	private RegisteredClient toRegisteredClient(OAuth2Application application) {

		Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(
			application.getClientAuthenticationMethods());
		Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(
			application.getAuthorizationGrantTypes());
		Set<String> redirectUris = StringUtils.commaDelimitedListToSet(
			application.getRedirectUris());
		Set<OAuth2Scope> clientScopes = application.getScopes();

		return RegisteredClient.withId(application.getApplicationId())
			// 客户端id 需要唯一
			.clientId(application.getClientId())
			// 客户端密码
			.clientSecret(application.getClientSecret())
			.clientSecretExpiresAt(DateUtil.toInstant(application.getClientSecretExpiresAt()))
			.clientAuthenticationMethods(authenticationMethods ->
				clientAuthenticationMethods.forEach(authenticationMethod ->
					authenticationMethods.add(
						OAuth2AuthorizationUtils.resolveClientAuthenticationMethod(
							authenticationMethod))))
			.authorizationGrantTypes((grantTypes) ->
				authorizationGrantTypes.forEach(grantType ->
					grantTypes.add(
						OAuth2AuthorizationUtils.resolveAuthorizationGrantType(grantType))))
			.redirectUris((uris) -> uris.addAll(redirectUris))
			.scopes((scopes) -> clientScopes.forEach(
				clientScope -> scopes.add(clientScope.getScopeCode())))
			.clientSettings(createClientSettings(application))
			.tokenSettings(createTokenSettings(application))
			.build();
	}

	private ClientSettings createClientSettings(OAuth2Application application) {
		ClientSettings.Builder clientSettingsBuilder = ClientSettings.builder();
		clientSettingsBuilder.requireAuthorizationConsent(
			application.getRequireAuthorizationConsent());
		clientSettingsBuilder.requireProofKey(application.getRequireProofKey());
		if (StringUtils.hasText(application.getJwkSetUrl())) {
			clientSettingsBuilder.jwkSetUrl(application.getJwkSetUrl());
		}
		if (ObjectUtils.isNotEmpty(application.getAuthenticationSigningAlgorithm())) {
			JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.from(
				application.getAuthenticationSigningAlgorithm().name());
			if (ObjectUtils.isNotEmpty(jwsAlgorithm)) {
				clientSettingsBuilder.tokenEndpointAuthenticationSigningAlgorithm(jwsAlgorithm);
			}
		}
		return clientSettingsBuilder.build();
	}

	private TokenSettings createTokenSettings(OAuth2Application application) {
		TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder();
		// accessToken 的有效期
		tokenSettingsBuilder.accessTokenTimeToLive(application.getAccessTokenValidity());
		// refreshToken 的有效期
		tokenSettingsBuilder.refreshTokenTimeToLive(application.getRefreshTokenValidity());
		// 是否可重用刷新令牌
		tokenSettingsBuilder.reuseRefreshTokens(application.getReuseRefreshTokens());
		tokenSettingsBuilder.accessTokenFormat(getTokenFormat());
		if (ObjectUtils.isNotEmpty(application.getIdTokenSignatureAlgorithm())) {
			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.from(
				application.getIdTokenSignatureAlgorithm().name());
			if (ObjectUtils.isNotEmpty(signatureAlgorithm)) {
				tokenSettingsBuilder.idTokenSignatureAlgorithm(signatureAlgorithm);
			}
		}
		return tokenSettingsBuilder.build();
	}

	private OAuth2TokenFormat getTokenFormat() {
		if (securityProperties.getValidate() == Target.REMOTE) {
			return new OAuth2TokenFormat("reference");
		} else {
			return new OAuth2TokenFormat("self-contained");
		}
	}
}
