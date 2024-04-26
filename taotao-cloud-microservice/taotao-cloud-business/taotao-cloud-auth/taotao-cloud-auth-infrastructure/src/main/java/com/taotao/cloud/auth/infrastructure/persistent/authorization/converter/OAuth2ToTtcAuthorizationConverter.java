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

package com.taotao.cloud.auth.infrastructure.persistent.authorization.converter;

import static org.dromara.hutool.core.text.StrPool.COMMA;

import com.taotao.cloud.auth.infrastructure.persistent.authorization.jackson2.OAuth2JacksonProcessor;
import com.taotao.cloud.auth.infrastructure.persistent.authorization.po.TtcAuthorization;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import org.dromara.hutool.core.date.DateUtil;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.util.StringUtils;

/**
 * <p>OAuth2Authorization 转 TtcAuthorization 转换器 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:13:42
 */
public class OAuth2ToTtcAuthorizationConverter
	extends AbstractOAuth2EntityConverter<OAuth2Authorization, TtcAuthorization> {

	/**
	 * oauth2到希罗多德授权转换器
	 *
	 * @param jacksonProcessor 杰克逊处理器
	 * @return
	 * @since 2023-07-10 17:13:42
	 */
	public OAuth2ToTtcAuthorizationConverter(OAuth2JacksonProcessor jacksonProcessor) {
		super(jacksonProcessor);
	}

	/**
	 * 转换
	 *
	 * @param authorization 授权
	 * @return {@link TtcAuthorization }
	 * @since 2023-07-10 17:13:42
	 */
	@Override
	public TtcAuthorization convert(OAuth2Authorization authorization) {
		TtcAuthorization entity = new TtcAuthorization();
		entity.setId(authorization.getId());
		entity.setRegisteredClientId(authorization.getRegisteredClientId());
		entity.setPrincipalName(authorization.getPrincipalName());
		entity.setAuthorizationGrantType(
			authorization.getAuthorizationGrantType().getValue());
		entity.setAuthorizedScopes(
			StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), COMMA));
		entity.setAttributes(writeMap(authorization.getAttributes()));
		entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

		OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
			authorization.getToken(OAuth2AuthorizationCode.class);
		setTokenValues(
			authorizationCode,
			entity::setAuthorizationCodeValue,
			entity::setAuthorizationCodeIssuedAt,
			entity::setAuthorizationCodeExpiresAt,
			entity::setAuthorizationCodeMetadata);

		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(
			OAuth2AccessToken.class);
		setTokenValues(
			accessToken,
			entity::setAccessTokenValue,
			entity::setAccessTokenIssuedAt,
			entity::setAccessTokenExpiresAt,
			entity::setAccessTokenMetadata);
		if (accessToken != null && accessToken.getToken().getScopes() != null) {
			entity.setAccessTokenScopes(StringUtils.collectionToCommaDelimitedString(
				accessToken.getToken().getScopes()));
		}

		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getToken(
			OAuth2RefreshToken.class);
		setTokenValues(
			refreshToken,
			entity::setRefreshTokenValue,
			entity::setRefreshTokenIssuedAt,
			entity::setRefreshTokenExpiresAt,
			entity::setRefreshTokenMetadata);

		OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(
			OidcIdToken.class);
		setTokenValues(
			oidcIdToken,
			entity::setOidcIdTokenValue,
			entity::setOidcIdTokenIssuedAt,
			entity::setOidcIdTokenExpiresAt,
			entity::setOidcIdTokenMetadata);
		if (oidcIdToken != null) {
			entity.setOidcIdTokenClaims(writeMap(oidcIdToken.getClaims()));
		}

		OAuth2Authorization.Token<OAuth2UserCode> userCode = authorization.getToken(
			OAuth2UserCode.class);
		setTokenValues(
			userCode,
			entity::setUserCodeValue,
			entity::setUserCodeIssuedAt,
			entity::setUserCodeExpiresAt,
			entity::setUserCodeMetadata);

		OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode = authorization.getToken(
			OAuth2DeviceCode.class);
		setTokenValues(
			deviceCode,
			entity::setDeviceCodeValue,
			entity::setDeviceCodeIssuedAt,
			entity::setDeviceCodeExpiresAt,
			entity::setDeviceCodeMetadata);

		return entity;
	}

	/**
	 * 设置令牌值
	 *
	 * @param token              令牌
	 * @param tokenValueConsumer 令牌价值消费者
	 * @param issuedAtConsumer   在消费者处发行
	 * @param expiresAtConsumer  在消费者处到期
	 * @param metadataConsumer   元数据使用者
	 * @since 2023-07-10 17:13:44
	 */
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
}
