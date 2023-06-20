
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
