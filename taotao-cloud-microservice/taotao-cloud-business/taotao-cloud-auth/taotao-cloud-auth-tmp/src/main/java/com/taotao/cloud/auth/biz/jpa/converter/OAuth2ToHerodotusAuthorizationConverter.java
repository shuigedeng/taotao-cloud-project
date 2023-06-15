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

package com.taotao.cloud.auth.biz.jpa.converter;

import com.taotao.cloud.auth.biz.jpa.definition.converter.AbstractOAuth2EntityConverter;
import com.taotao.cloud.auth.biz.jpa.entity.HerodotusAuthorization;
import com.taotao.cloud.auth.biz.jpa.jackson2.OAuth2JacksonProcessor;
import org.dromara.hutool.core.date.DateUtil;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import static org.dromara.hutool.core.text.StrPool.COMMA;

/**
 * <p>Description: OAuth2Authorization 转 HerodotusAuthorization 转换器 </p>
 *
 *
 * @date : 2023/5/21 20:57
 */
public class OAuth2ToHerodotusAuthorizationConverter extends AbstractOAuth2EntityConverter<OAuth2Authorization, HerodotusAuthorization> {

	public OAuth2ToHerodotusAuthorizationConverter(OAuth2JacksonProcessor jacksonProcessor) {
		super(jacksonProcessor);
	}

	@Override
	public HerodotusAuthorization convert(OAuth2Authorization authorization) {
		HerodotusAuthorization entity = new HerodotusAuthorization();
		entity.setId(authorization.getId());
		entity.setRegisteredClientId(authorization.getRegisteredClientId());
		entity.setPrincipalName(authorization.getPrincipalName());
		entity.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
		entity.setAuthorizedScopes(StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), COMMA));
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
}
