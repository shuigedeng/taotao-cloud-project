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
import cn.herodotus.engine.oauth2.data.jpa.generator.HerodotusRegisteredClientUuid;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Description: OAuth2 客户端实体
 *
 * @author : gengwei.zheng
 * @date : 2022/1/22 17:18
 */
@Entity
@Table(
        name = "oauth2_registered_client",
        indexes = {
            @Index(name = "oauth2_registered_client_id_idx", columnList = "id"),
            @Index(name = "oauth2_registered_client_cid_idx", columnList = "client_id")
        })
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE,
        region = OAuth2Constants.REGION_OAUTH2_REGISTERED_CLIENT)
public class HerodotusRegisteredClient extends AbstractEntity {

    @Id
    @HerodotusRegisteredClientUuid
    @Column(name = "id", nullable = false, length = 100)
    private String id;

    @Column(name = "client_id", nullable = false, length = 100)
    private String clientId;

    @Column(name = "client_id_issued_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime clientIdIssuedAt;

    @Column(name = "client_secret", length = 200)
    private String clientSecret;

    @Column(name = "client_secret_expires_at")
    private LocalDateTime clientSecretExpiresAt;

    @Column(name = "client_name", nullable = false, length = 200)
    private String clientName;

    @Column(name = "client_authentication_methods", nullable = false, length = 1000)
    private String clientAuthenticationMethods;

    @Column(name = "authorization_grant_types", nullable = false, length = 1000)
    private String authorizationGrantTypes;

    @Column(name = "redirect_uris", length = 1000)
    private String redirectUris;

    @Column(name = "scopes", nullable = false, length = 1000)
    private String scopes;

    @Column(name = "client_settings", nullable = false, length = 2000)
    private String clientSettings;

    @Column(name = "token_settings", nullable = false, length = 2000)
    private String tokenSettings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    public void setClientIdIssuedAt(LocalDateTime clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public LocalDateTime getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(LocalDateTime clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    public String getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(String authorizationGantTypes) {
        this.authorizationGrantTypes = authorizationGantTypes;
    }

    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
    }

    public String getTokenSettings() {
        return tokenSettings;
    }

    public void setTokenSettings(String tokenSettings) {
        this.tokenSettings = tokenSettings;
    }
}
