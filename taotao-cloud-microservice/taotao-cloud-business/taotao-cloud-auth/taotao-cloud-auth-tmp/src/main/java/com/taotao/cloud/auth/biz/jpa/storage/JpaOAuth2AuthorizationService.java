/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
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
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.jpa.storage;

import com.taotao.cloud.auth.biz.jpa.entity.HerodotusAuthorization;
import com.taotao.cloud.auth.biz.jpa.jackson2.OAuth2JacksonProcessor;
import com.taotao.cloud.auth.biz.jpa.service.HerodotusAuthorizationService;
import com.taotao.cloud.common.constant.SymbolConstants;
import com.taotao.cloud.security.springsecurity.core.utils.OAuth2AuthorizationUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.hutool.core.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * <p>Description: 基于 JPA 的 OAuth2 认证服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/25 22:16
 */
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {

	private static final Logger log = LoggerFactory.getLogger(JpaOAuth2AuthorizationService.class);

	private final HerodotusAuthorizationService herodotusAuthorizationService;
	private final RegisteredClientRepository registeredClientRepository;

	private final OAuth2JacksonProcessor jacksonProcessor;

	public JpaOAuth2AuthorizationService(HerodotusAuthorizationService herodotusAuthorizationService, RegisteredClientRepository registeredClientRepository) {
		this.herodotusAuthorizationService = herodotusAuthorizationService;
		this.registeredClientRepository = registeredClientRepository;

		this.jacksonProcessor = new OAuth2JacksonProcessor();
	}

	@Override
	public void save(OAuth2Authorization authorization) {
		this.herodotusAuthorizationService.saveOrUpdate(toEntity(authorization));
		log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service save entity.");
	}

	@Transactional
	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		this.herodotusAuthorizationService.deleteById(authorization.getId());
		log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service remove entity.");
		// TODO： 后期还是考虑改为异步任务的形式，先临时放在这里。
		this.herodotusAuthorizationService.clearHistoryToken();
		log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service clear history token.");
	}

	@Override
	public OAuth2Authorization findById(String id) {
		HerodotusAuthorization herodotusAuthorization = this.herodotusAuthorizationService.findById(id);
		if (ObjectUtils.isNotEmpty(herodotusAuthorization)) {
			log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service findById.");
			return toObject(herodotusAuthorization);
		} else {
			return null;
		}
	}

	public int findAuthorizationCount(String registeredClientId, String principalName) {
		int count = this.herodotusAuthorizationService.findAuthorizationCount(registeredClientId, principalName);
		log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service findAuthorizationCount.");
		return count;
	}

	public List<OAuth2Authorization> findAvailableAuthorizations(String registeredClientId, String principalName) {
		List<HerodotusAuthorization> authorizations = this.herodotusAuthorizationService.findAvailableAuthorizations(registeredClientId, principalName);
		if (CollectionUtils.isNotEmpty(authorizations)) {
			return authorizations.stream().map(this::toObject).toList();
		}

		return new ArrayList<>();
	}

	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		Assert.hasText(token, "token cannot be empty");

		Optional<HerodotusAuthorization> result;
		if (tokenType == null) {
			result = this.herodotusAuthorizationService.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(token);
		} else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
			result = this.herodotusAuthorizationService.findByState(token);
		} else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
			result = this.herodotusAuthorizationService.findByAuthorizationCode(token);
		} else if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType.getValue())) {
			result = this.herodotusAuthorizationService.findByAccessToken(token);
		} else if (OAuth2ParameterNames.REFRESH_TOKEN.equals(tokenType.getValue())) {
			result = this.herodotusAuthorizationService.findByRefreshToken(token);
		} else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
			result = this.herodotusAuthorizationService.findByOidcIdTokenValue(token);
		} else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
			result = this.herodotusAuthorizationService.findByUserCodeValue(token);
		} else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
			result = this.herodotusAuthorizationService.findByDeviceCodeValue(token);
		} else {
			result = Optional.empty();
		}

		log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service findByToken.");
		return result.map(this::toObject).orElse(null);
	}

	private OAuth2Authorization toObject(HerodotusAuthorization entity) {
		RegisteredClient registeredClient = this.registeredClientRepository.findById(entity.getRegisteredClientId());
		if (registeredClient == null) {
			throw new DataRetrievalFailureException(
				"The RegisteredClient with id '" + entity.getRegisteredClientId() + "' was not found in the RegisteredClientRepository.");
		}

		OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
			.id(entity.getId())
			.principalName(entity.getPrincipalName())
			.authorizationGrantType(OAuth2AuthorizationUtils.resolveAuthorizationGrantType(entity.getAuthorizationGrantType()))
			.authorizedScopes(StringUtils.commaDelimitedListToSet(entity.getAuthorizedScopes()))
			.attributes(attributes -> attributes.putAll(parseMap(entity.getAttributes())));
		if (entity.getState() != null) {
			builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
		}

		if (entity.getAuthorizationCodeValue() != null) {
			OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
				entity.getAuthorizationCodeValue(),
				DateUtil.toInstant(entity.getAuthorizationCodeIssuedAt()),
				DateUtil.toInstant(entity.getAuthorizationCodeExpiresAt()));
			builder.token(authorizationCode, metadata -> metadata.putAll(parseMap(entity.getAuthorizationCodeMetadata())));
		}

		if (entity.getAccessTokenValue() != null) {
			OAuth2AccessToken accessToken = new OAuth2AccessToken(
				OAuth2AccessToken.TokenType.BEARER,
				entity.getAccessTokenValue(),
				DateUtil.toInstant(entity.getAccessTokenIssuedAt()),
				DateUtil.toInstant(entity.getAccessTokenExpiresAt()),
				StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes()));
			builder.token(accessToken, metadata -> metadata.putAll(parseMap(entity.getAccessTokenMetadata())));
		}

		if (entity.getRefreshTokenValue() != null) {
			OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
				entity.getRefreshTokenValue(),
				DateUtil.toInstant(entity.getRefreshTokenIssuedAt()),
				DateUtil.toInstant(entity.getRefreshTokenExpiresAt()));
			builder.token(refreshToken, metadata -> metadata.putAll(parseMap(entity.getRefreshTokenMetadata())));
		}

		if (entity.getOidcIdTokenValue() != null) {
			OidcIdToken idToken = new OidcIdToken(
				entity.getOidcIdTokenValue(),
				DateUtil.toInstant(entity.getOidcIdTokenIssuedAt()),
				DateUtil.toInstant(entity.getOidcIdTokenExpiresAt()),
				parseMap(entity.getOidcIdTokenClaims()));
			builder.token(idToken, metadata -> metadata.putAll(parseMap(entity.getOidcIdTokenMetadata())));
		}

		if (entity.getUserCodeValue() != null) {
			OAuth2UserCode userCode = new OAuth2UserCode(
				entity.getUserCodeValue(),
				DateUtil.toInstant(entity.getUserCodeIssuedAt()),
				DateUtil.toInstant(entity.getUserCodeExpiresAt()));
			builder.token(userCode, metadata -> metadata.putAll(parseMap(entity.getUserCodeMetadata())));
		}

		if (entity.getDeviceCodeValue() != null) {
			OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(
				entity.getDeviceCodeValue(),
				DateUtil.toInstant(entity.getDeviceCodeIssuedAt()),
				DateUtil.toInstant(entity.getDeviceCodeExpiresAt()));
			builder.token(deviceCode, metadata -> metadata.putAll(parseMap(entity.getDeviceCodeMetadata())));
		}

		return builder.build();
	}

	private HerodotusAuthorization toEntity(OAuth2Authorization authorization) {
		HerodotusAuthorization entity = new HerodotusAuthorization();
		entity.setId(authorization.getId());
		entity.setRegisteredClientId(authorization.getRegisteredClientId());
		entity.setPrincipalName(authorization.getPrincipalName());
		entity.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
		entity.setAuthorizedScopes(StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), SymbolConstants.COMMA));
		entity.setAttributes(writeMap(authorization.getAttributes()));
		entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

		OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
			authorization.getToken(OAuth2AuthorizationCode.class);
		setTokenValues(
			authorizationCode,
			entity::setAuthorizationCodeValue,
			entity::setAuthorizationCodeIssuedAt,
			entity::setAuthorizationCodeExpiresAt,
			entity::setAuthorizationCodeMetadata
		);

		OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
			authorization.getToken(OAuth2AccessToken.class);
		setTokenValues(
			accessToken,
			entity::setAccessTokenValue,
			entity::setAccessTokenIssuedAt,
			entity::setAccessTokenExpiresAt,
			entity::setAccessTokenMetadata
		);
		if (accessToken != null && accessToken.getToken().getScopes() != null) {
			entity.setAccessTokenScopes(StringUtils.collectionToCommaDelimitedString(accessToken.getToken().getScopes()));
		}

		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
			authorization.getToken(OAuth2RefreshToken.class);
		setTokenValues(
			refreshToken,
			entity::setRefreshTokenValue,
			entity::setRefreshTokenIssuedAt,
			entity::setRefreshTokenExpiresAt,
			entity::setRefreshTokenMetadata
		);

		OAuth2Authorization.Token<OidcIdToken> oidcIdToken =
			authorization.getToken(OidcIdToken.class);
		setTokenValues(
			oidcIdToken,
			entity::setOidcIdTokenValue,
			entity::setOidcIdTokenIssuedAt,
			entity::setOidcIdTokenExpiresAt,
			entity::setOidcIdTokenMetadata
		);
		if (oidcIdToken != null) {
			entity.setOidcIdTokenClaims(writeMap(oidcIdToken.getClaims()));
		}

		OAuth2Authorization.Token<OAuth2UserCode> userCode =
			authorization.getToken(OAuth2UserCode.class);
		setTokenValues(
			userCode,
			entity::setUserCodeValue,
			entity::setUserCodeIssuedAt,
			entity::setUserCodeExpiresAt,
			entity::setUserCodeMetadata
		);

		OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode =
			authorization.getToken(OAuth2DeviceCode.class);
		setTokenValues(
			deviceCode,
			entity::setDeviceCodeValue,
			entity::setDeviceCodeIssuedAt,
			entity::setDeviceCodeExpiresAt,
			entity::setDeviceCodeMetadata
		);

		return entity;
	}

	private void setTokenValues(
		OAuth2Authorization.Token<?> token,
		Consumer<String> tokenValueConsumer,
		Consumer<LocalDateTime> issuedAtConsumer,
		Consumer<LocalDateTime> expiresAtConsumer,
		Consumer<String> metadataConsumer) {
		if (token != null) {
			OAuth2Token oAuth2Token = token.getToken();
			tokenValueConsumer.accept(oAuth2Token.getTokenValue());
			issuedAtConsumer.accept(DateUtil.toLocalDateTime(oAuth2Token.getIssuedAt()));
			expiresAtConsumer.accept(DateUtil.toLocalDateTime(oAuth2Token.getExpiresAt()));
			metadataConsumer.accept(writeMap(token.getMetadata()));
		}
	}

	private Map<String, Object> parseMap(String data) {
		return jacksonProcessor.parseMap(data);
	}

	private String writeMap(Map<String, Object> data) {
		return jacksonProcessor.writeMap(data);
	}
}
