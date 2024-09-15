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

package com.taotao.cloud.auth.infrastructure.persistent.management.po;

import com.taotao.cloud.auth.infrastructure.persistent.authorization.po.AbstractRegisteredClient;
import com.taotao.boot.security.spring.enums.Signature;
import com.taotao.boot.security.spring.enums.TokenFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.time.Duration;
import java.util.Set;
import org.dromara.hutool.core.data.id.IdUtil;

/**
 * <p>应用对象转 RegisteredClient 共性属性 </p>
 *
 * @since : 2023/5/21 17:46
 */
@MappedSuperclass
public abstract class AbstractOAuth2RegisteredClient extends AbstractRegisteredClient {

	@Schema(name = "客户端Id", title = "默认为系统自动生成")
	@Column(name = "client_id", length = 100)
	private String clientId = IdUtil.fastSimpleUUID();

	@Schema(name = "客户端秘钥", title = "这里存储的客户端秘钥是明文，方便使用。默认为系统自动生成")
	@Column(name = "client_secret", length = 100)
	private String clientSecret = IdUtil.fastSimpleUUID();

	/* --- ClientSettings Begin --- */
	@Schema(name = "是否需要证明Key", title = "如果客户端在执行授权码授予流时需要提供验证密钥质询和验证器, 默认False")
	@Column(name = "require_proof_key")
	private Boolean requireProofKey = Boolean.FALSE;

	@Schema(name = "是否需要认证确认", title = "如果客户端在执行授权码授予流时需要提供验证密钥质询和验证器, 默认False")
	@Column(name = "require_authorization_consent")
	private Boolean requireAuthorizationConsent = Boolean.TRUE;

	@Schema(name = "客户端JSON Web密钥集的URL", title = "客户端JSON Web密钥集的URL")
	@Column(name = "jwk_set_url", length = 1000)
	private String jwkSetUrl;

	@Schema(name = "JWT 签名算法", title = "仅在 clientAuthenticationMethods 为 private_key_jwt 和 client_secret_jwt 方法下使用")
	@Column(name = "signing_algorithm")
	@Enumerated(EnumType.ORDINAL)
	private Signature authenticationSigningAlgorithm;
	/* --- ClientSettings End --- */

	/* --- TokenSettings Begin --- */
	@Schema(name = "授权码有效时间", title = "默认5分钟，使用 Duration 时间格式")
	@Column(name = "authorization_code_validity")
	private Duration authorizationCodeValidity = Duration.ofMinutes(5);

	@Schema(name = "激活码有效时间", title = "默认5分钟，使用 Duration 时间格式")
	@Column(name = "device_code_validity")
	private Duration deviceCodeValidity = Duration.ofMinutes(5);

	@Schema(name = "AccessToken 有效时间", title = "默认5分钟，使用 Duration 时间格式")
	@Column(name = "access_token_validity")
	private Duration accessTokenValidity = Duration.ofMinutes(5);

	@Schema(name = "RefreshToken 有效时间", title = "默认60分钟，使用 Duration 时间格式")
	@Column(name = "refresh_token_validity")
	private Duration refreshTokenValidity = Duration.ofMinutes(60);

	@Schema(name = "Access Token 格式", title = "OAuth 2.0令牌的标准数据格式")
	@Column(name = "access_token_format")
	@Enumerated(EnumType.ORDINAL)
	private TokenFormat accessTokenFormat = TokenFormat.REFERENCE;

	@Schema(name = "是否重用 Refresh Token", title = "默认值 True")
	@Column(name = "reuse_refresh_tokens")
	private Boolean reuseRefreshTokens = Boolean.TRUE;

	@Schema(name = "IdToken 签名算法", title = "JWT 算法用于签名 ID Token， 默认值 RS256")
	@Column(name = "signature_algorithm")
	@Enumerated(EnumType.ORDINAL)
	private Signature idTokenSignatureAlgorithm = Signature.RS256;
	/* --- TokenSettings End --- */

	public abstract Set<OAuth2Scope> getScopes();

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public Boolean getRequireProofKey() {
		return requireProofKey;
	}

	public void setRequireProofKey(Boolean requireProofKey) {
		this.requireProofKey = requireProofKey;
	}

	public Boolean getRequireAuthorizationConsent() {
		return requireAuthorizationConsent;
	}

	public void setRequireAuthorizationConsent(Boolean requireAuthorizationConsent) {
		this.requireAuthorizationConsent = requireAuthorizationConsent;
	}

	public String getJwkSetUrl() {
		return jwkSetUrl;
	}

	public void setJwkSetUrl(String jwkSetUrl) {
		this.jwkSetUrl = jwkSetUrl;
	}

	public Signature getAuthenticationSigningAlgorithm() {
		return authenticationSigningAlgorithm;
	}

	public void setAuthenticationSigningAlgorithm(Signature authenticationSigningAlgorithm) {
		this.authenticationSigningAlgorithm = authenticationSigningAlgorithm;
	}

	public Duration getAuthorizationCodeValidity() {
		return authorizationCodeValidity;
	}

	public void setAuthorizationCodeValidity(Duration authorizationCodeValidity) {
		this.authorizationCodeValidity = authorizationCodeValidity;
	}

	public Duration getAccessTokenValidity() {
		return accessTokenValidity;
	}

	public void setAccessTokenValidity(Duration accessTokenValidity) {
		this.accessTokenValidity = accessTokenValidity;
	}

	public Duration getDeviceCodeValidity() {
		return deviceCodeValidity;
	}

	public void setDeviceCodeValidity(Duration deviceCodeValidity) {
		this.deviceCodeValidity = deviceCodeValidity;
	}

	public Duration getRefreshTokenValidity() {
		return refreshTokenValidity;
	}

	public void setRefreshTokenValidity(Duration refreshTokenValidity) {
		this.refreshTokenValidity = refreshTokenValidity;
	}

	public TokenFormat getAccessTokenFormat() {
		return accessTokenFormat;
	}

	public void setAccessTokenFormat(TokenFormat accessTokenFormat) {
		this.accessTokenFormat = accessTokenFormat;
	}

	public Boolean getReuseRefreshTokens() {
		return reuseRefreshTokens;
	}

	public void setReuseRefreshTokens(Boolean reuseRefreshTokens) {
		this.reuseRefreshTokens = reuseRefreshTokens;
	}

	public Signature getIdTokenSignatureAlgorithm() {
		return idTokenSignatureAlgorithm;
	}

	public void setIdTokenSignatureAlgorithm(Signature idTokenSignatureAlgorithm) {
		this.idTokenSignatureAlgorithm = idTokenSignatureAlgorithm;
	}
}
