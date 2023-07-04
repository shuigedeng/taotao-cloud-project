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

package com.taotao.cloud.auth.biz.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.data.jpa.tenant.BaseSysEntity;
import com.taotao.cloud.security.springsecurity.core.constants.DefaultConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

/**
 * <p>Description: 多实例共用 RegisteredClient属性 </p>
 *
 *
 * @date : 2023/5/21 17:30
 */
@MappedSuperclass
public abstract class AbstractRegisteredClient extends BaseSysEntity implements RegisteredClientDetails {

    @Schema(name = "客户端ID发布日期", title = "客户端发布日期")
    @JsonFormat(pattern = DefaultConstants.DATE_TIME_FORMAT, locale = "GMT+8", shape = JsonFormat.Shape.STRING)
    @Column(name = "client_id_issued_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime clientIdIssuedAt;

    @Schema(name = "客户端秘钥过期时间", title = "客户端秘钥过期时间")
    @JsonFormat(pattern = DefaultConstants.DATE_TIME_FORMAT, locale = "GMT+8", shape = JsonFormat.Shape.STRING)
    @Column(name = "client_secret_expires_at")
    private LocalDateTime clientSecretExpiresAt;

    @Schema(name = "客户端认证模式", title = "支持多个值，以逗号分隔", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "client_authentication_methods", nullable = false, length = 1000)
    //    @JsonDeserialize(using = SetToCommaDelimitedStringDeserializer.class)
    //    @JsonSerialize(using = CommaDelimitedStringToSetSerializer.class)
    private String clientAuthenticationMethods;

    @Schema(name = "认证模式", title = "支持多个值，以逗号分隔", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "authorization_grant_types", nullable = false, length = 1000)
    //    @JsonDeserialize(using = SetToCommaDelimitedStringDeserializer.class)
    //    @JsonSerialize(using = CommaDelimitedStringToSetSerializer.class)
    private String authorizationGrantTypes;

    @Schema(name = "回调地址", title = "支持多个值，以逗号分隔")
    @Column(name = "redirect_uris", length = 1000)
    private String redirectUris;

    @Schema(name = "OIDC Logout 回调地址", title = "支持多个值，以逗号分隔")
    @Column(name = "post_logout_redirect_uris", length = 1000)
    private String postLogoutRedirectUris;

    @Override
    public LocalDateTime getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    public void setClientIdIssuedAt(LocalDateTime clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
    }

    @Override
    public LocalDateTime getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(LocalDateTime clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    @Override
    public String getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    @Override
    public String getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }

    @Override
    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    @Override
    public String getPostLogoutRedirectUris() {
        return postLogoutRedirectUris;
    }

    public void setPostLogoutRedirectUris(String postLogoutRedirectUris) {
        this.postLogoutRedirectUris = postLogoutRedirectUris;
    }
}
