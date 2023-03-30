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

package com.taotao.cloud.auth.biz.demo.jpa.entity;

import cn.herodotus.engine.assistant.core.definition.domain.AbstractEntity;
import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import cn.herodotus.engine.oauth2.data.jpa.generator.HerodotusAuthorizationUuid;
import com.google.common.base.MoreObjects;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Description: OAuth2 认证信息
 *
 * @author : gengwei.zheng
 * @date : 2022/1/22 18:08
 */
@Entity
@Table(
        name = "oauth2_authorization",
        indexes = {
            @Index(name = "oauth2_authorization_id_idx", columnList = "id"),
            @Index(name = "oauth2_authorization_rci_idx", columnList = "registered_client_id"),
            @Index(name = "oauth2_authorization_pn_idx", columnList = "principal_name"),
            @Index(name = "oauth2_authorization_atia_idx", columnList = "access_token_issued_at"),
            @Index(name = "oauth2_authorization_atea_idx", columnList = "access_token_expires_at"),
            @Index(name = "oauth2_authorization_rtia_idx", columnList = "refresh_token_issued_at"),
            @Index(name = "oauth2_authorization_rtea_idx", columnList = "refresh_token_expires_at")
        })
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE,
        region = OAuth2Constants.REGION_OAUTH2_AUTHORIZATION)
public class HerodotusAuthorization extends AbstractEntity {

    @Id
    @HerodotusAuthorizationUuid
    @Column(name = "id", nullable = false, length = 100)
    private String id;

    @Column(name = "registered_client_id", nullable = false, length = 100)
    private String registeredClientId;

    @Column(name = "principal_name", nullable = false, length = 200)
    private String principalName;

    @Column(name = "authorization_grant_type", nullable = false, length = 100)
    private String authorizationGrantType;

    @Column(name = "attributes", columnDefinition = "TEXT")
    private String attributes;

    @Column(name = "state", length = 500)
    private String state;

    @Column(name = "authorization_code_value", columnDefinition = "TEXT")
    private String authorizationCode;

    @Column(name = "authorization_code_issued_at")
    private LocalDateTime authorizationCodeIssuedAt;

    @Column(name = "authorization_code_expires_at")
    private LocalDateTime authorizationCodeExpiresAt;

    @Column(name = "authorization_code_metadata", columnDefinition = "TEXT")
    private String authorizationCodeMetadata;

    @Column(name = "access_token_value", columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "access_token_issued_at")
    private LocalDateTime accessTokenIssuedAt;

    @Column(name = "access_token_expires_at")
    private LocalDateTime accessTokenExpiresAt;

    @Column(name = "access_token_metadata", columnDefinition = "TEXT")
    private String accessTokenMetadata;

    @Column(name = "access_token_type", length = 100)
    private String accessTokenType;

    @Column(name = "access_token_scopes", length = 1000)
    private String accessTokenScopes;

    @Column(name = "oidc_id_token_value", columnDefinition = "TEXT")
    private String oidcIdToken;

    @Column(name = "oidc_id_token_issued_at")
    private LocalDateTime oidcIdTokenIssuedAt;

    @Column(name = "oidc_id_token_expires_at")
    private LocalDateTime oidcIdTokenExpiresAt;

    @Column(name = "oidc_id_token_metadata", columnDefinition = "TEXT")
    private String oidcIdTokenMetadata;

    @Column(name = "oidc_id_token_claims", length = 2000)
    private String oidcIdTokenClaims;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "refresh_token_issued_at")
    private LocalDateTime refreshTokenIssuedAt;

    @Column(name = "refresh_token_expires_at")
    private LocalDateTime refreshTokenExpiresAt;

    @Column(name = "refresh_token_metadata", columnDefinition = "TEXT")
    private String refreshTokenMetadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegisteredClientId() {
        return registeredClientId;
    }

    public void setRegisteredClientId(String registeredClientId) {
        this.registeredClientId = registeredClientId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getAuthorizationGrantType() {
        return authorizationGrantType;
    }

    public void setAuthorizationGrantType(String authorizationGrantType) {
        this.authorizationGrantType = authorizationGrantType;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCodeValue) {
        this.authorizationCode = authorizationCodeValue;
    }

    public LocalDateTime getAuthorizationCodeIssuedAt() {
        return authorizationCodeIssuedAt;
    }

    public void setAuthorizationCodeIssuedAt(LocalDateTime authorizationCodeIssuedAt) {
        this.authorizationCodeIssuedAt = authorizationCodeIssuedAt;
    }

    public LocalDateTime getAuthorizationCodeExpiresAt() {
        return authorizationCodeExpiresAt;
    }

    public void setAuthorizationCodeExpiresAt(LocalDateTime authorizationCodeExpiresAt) {
        this.authorizationCodeExpiresAt = authorizationCodeExpiresAt;
    }

    public String getAuthorizationCodeMetadata() {
        return authorizationCodeMetadata;
    }

    public void setAuthorizationCodeMetadata(String authorizationCodeMetadata) {
        this.authorizationCodeMetadata = authorizationCodeMetadata;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessTokenValue) {
        this.accessToken = accessTokenValue;
    }

    public LocalDateTime getAccessTokenIssuedAt() {
        return accessTokenIssuedAt;
    }

    public void setAccessTokenIssuedAt(LocalDateTime accessTokenIssuedAt) {
        this.accessTokenIssuedAt = accessTokenIssuedAt;
    }

    public LocalDateTime getAccessTokenExpiresAt() {
        return accessTokenExpiresAt;
    }

    public void setAccessTokenExpiresAt(LocalDateTime accessTokenExpiresAt) {
        this.accessTokenExpiresAt = accessTokenExpiresAt;
    }

    public String getAccessTokenMetadata() {
        return accessTokenMetadata;
    }

    public void setAccessTokenMetadata(String accessTokenMetadata) {
        this.accessTokenMetadata = accessTokenMetadata;
    }

    public String getAccessTokenType() {
        return accessTokenType;
    }

    public void setAccessTokenType(String accessTokenType) {
        this.accessTokenType = accessTokenType;
    }

    public String getAccessTokenScopes() {
        return accessTokenScopes;
    }

    public void setAccessTokenScopes(String accessTokenScopes) {
        this.accessTokenScopes = accessTokenScopes;
    }

    public String getOidcIdToken() {
        return oidcIdToken;
    }

    public void setOidcIdToken(String oidcIdTokenValue) {
        this.oidcIdToken = oidcIdTokenValue;
    }

    public LocalDateTime getOidcIdTokenIssuedAt() {
        return oidcIdTokenIssuedAt;
    }

    public void setOidcIdTokenIssuedAt(LocalDateTime oidcIdTokenIssuedAt) {
        this.oidcIdTokenIssuedAt = oidcIdTokenIssuedAt;
    }

    public LocalDateTime getOidcIdTokenExpiresAt() {
        return oidcIdTokenExpiresAt;
    }

    public void setOidcIdTokenExpiresAt(LocalDateTime oidcIdTokenExpiresAt) {
        this.oidcIdTokenExpiresAt = oidcIdTokenExpiresAt;
    }

    public String getOidcIdTokenMetadata() {
        return oidcIdTokenMetadata;
    }

    public void setOidcIdTokenMetadata(String oidcIdTokenMetadata) {
        this.oidcIdTokenMetadata = oidcIdTokenMetadata;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshTokenValue) {
        this.refreshToken = refreshTokenValue;
    }

    public LocalDateTime getRefreshTokenIssuedAt() {
        return refreshTokenIssuedAt;
    }

    public void setRefreshTokenIssuedAt(LocalDateTime refreshTokenIssuedAt) {
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
    }

    public LocalDateTime getRefreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }

    public void setRefreshTokenExpiresAt(LocalDateTime refreshTokenExpiresAt) {
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public String getRefreshTokenMetadata() {
        return refreshTokenMetadata;
    }

    public void setRefreshTokenMetadata(String refreshTokenMetadata) {
        this.refreshTokenMetadata = refreshTokenMetadata;
    }

    public String getOidcIdTokenClaims() {
        return oidcIdTokenClaims;
    }

    public void setOidcIdTokenClaims(String oidcIdTokenClaims) {
        this.oidcIdTokenClaims = oidcIdTokenClaims;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("registeredClientId", registeredClientId)
                .add("principalName", principalName)
                .add("attributes", attributes)
                .add("state", state)
                .toString();
    }
}
