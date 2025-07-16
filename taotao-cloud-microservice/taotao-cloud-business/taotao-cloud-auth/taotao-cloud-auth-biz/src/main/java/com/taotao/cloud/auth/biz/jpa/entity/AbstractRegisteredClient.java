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
import com.taotao.boot.data.jpa.tenant.BaseSysEntity;
import com.taotao.boot.security.spring.constants.DefaultConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

/**
 * <p>多实例共用 RegisteredClient属性 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:11:33
 */
@MappedSuperclass
public abstract class AbstractRegisteredClient extends BaseSysEntity
        implements RegisteredClientDetails {

    /**
     * 客户端id发布于
     */
    @Schema(name = "客户端ID发布日期", title = "客户端发布日期")
    @JsonFormat(
            pattern = DefaultConstants.DATE_TIME_FORMAT,
            locale = "GMT+8",
            shape = JsonFormat.Shape.STRING)
    @Column(name = "client_id_issued_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime clientIdIssuedAt;

    /**
     * 客户端秘密在
     */
    @Schema(name = "客户端秘钥过期时间", title = "客户端秘钥过期时间")
    @JsonFormat(
            pattern = DefaultConstants.DATE_TIME_FORMAT,
            locale = "GMT+8",
            shape = JsonFormat.Shape.STRING)
    @Column(name = "client_secret_expires_at")
    private LocalDateTime clientSecretExpiresAt;

    /**
     * 客户端身份验证方法
     */
    @Schema(name = "客户端认证模式", title = "支持多个值，以逗号分隔", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "client_authentication_methods", nullable = false, length = 1000)
    //    @JsonDeserialize(using = SetToCommaDelimitedStringDeserializer.class)
    //    @JsonSerialize(using = CommaDelimitedStringToSetSerializer.class)
    private String clientAuthenticationMethods;

    /**
     * 授权授权类型
     */
    @Schema(name = "认证模式", title = "支持多个值，以逗号分隔", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "authorization_grant_types", nullable = false, length = 1000)
    //    @JsonDeserialize(using = SetToCommaDelimitedStringDeserializer.class)
    //    @JsonSerialize(using = CommaDelimitedStringToSetSerializer.class)
    private String authorizationGrantTypes;

    /**
     * 重定向uri
     */
    @Schema(name = "回调地址", title = "支持多个值，以逗号分隔")
    @Column(name = "redirect_uris", length = 1000)
    private String redirectUris;

    /**
     * 发布注销重定向uri
     */
    @Schema(name = "OIDC Logout 回调地址", title = "支持多个值，以逗号分隔")
    @Column(name = "post_logout_redirect_uris", length = 1000)
    private String postLogoutRedirectUris;

    /**
     * 获取客户端id
     *
     * @return {@link LocalDateTime }
     * @since 2023-07-10 17:11:34
     */
    @Override
    public LocalDateTime getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    /**
     * 设置在
     *
     * @param clientIdIssuedAt 客户端id发布于
     * @since 2023-07-10 17:11:34
     */
    public void setClientIdIssuedAt(LocalDateTime clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
    }

    /**
     * 获取客户端秘密到期时间为
     *
     * @return {@link LocalDateTime }
     * @since 2023-07-10 17:11:35
     */
    @Override
    public LocalDateTime getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    /**
     * 设置客户端密钥过期时间为
     *
     * @param clientSecretExpiresAt 客户端秘密在
     * @since 2023-07-10 17:11:35
     */
    public void setClientSecretExpiresAt(LocalDateTime clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    /**
     * 获取客户端身份验证方法
     *
     * @return {@link String }
     * @since 2023-07-10 17:11:36
     */
    @Override
    public String getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    /**
     * 设置客户端身份验证方法
     *
     * @param clientAuthenticationMethods 客户端身份验证方法
     * @since 2023-07-10 17:11:36
     */
    public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    /**
     * 获取授权授权类型
     *
     * @return {@link String }
     * @since 2023-07-10 17:11:37
     */
    @Override
    public String getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    /**
     * 设置授权授权类型
     *
     * @param authorizationGrantTypes 授权授权类型
     * @since 2023-07-10 17:11:38
     */
    public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }

    /**
     * 获取重定向uri
     *
     * @return {@link String }
     * @since 2023-07-10 17:11:38
     */
    @Override
    public String getRedirectUris() {
        return redirectUris;
    }

    /**
     * 设置重定向uri
     *
     * @param redirectUris 重定向uri
     * @since 2023-07-10 17:11:39
     */
    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    /**
     * 获取登录后重定向uri
     *
     * @return {@link String }
     * @since 2023-07-10 17:11:39
     */
    @Override
    public String getPostLogoutRedirectUris() {
        return postLogoutRedirectUris;
    }

    /**
     * 设置登录后重定向uri
     *
     * @param postLogoutRedirectUris 发布注销重定向uri
     * @since 2023-07-10 17:11:40
     */
    public void setPostLogoutRedirectUris(String postLogoutRedirectUris) {
        this.postLogoutRedirectUris = postLogoutRedirectUris;
    }
}
