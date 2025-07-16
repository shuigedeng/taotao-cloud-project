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

import com.google.common.base.MoreObjects;
import com.taotao.boot.security.spring.constants.OAuth2Constants;
import com.taotao.cloud.auth.biz.jpa.generator.TtcRegisteredClientUuidGenerator;
import jakarta.persistence.*;
import java.util.Objects;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * <p>OAuth2 客户端实体 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:12:44
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
public class TtcRegisteredClient extends AbstractRegisteredClient {

    /**
     * id
     */
    @Id
    @TtcRegisteredClientUuidGenerator
    @Column(name = "id", nullable = false, length = 100)
    private String id;

    /**
     * 客户端id
     */
    @Column(name = "client_id", nullable = false, length = 100)
    private String clientId;

    /**
     * 客户秘密
     */
    @Column(name = "client_secret", length = 200)
    private String clientSecret;

    /**
     * 客户名称
     */
    @Column(name = "client_name", nullable = false, length = 200)
    private String clientName;

    /**
     * 范围
     */
    @Column(name = "scopes", nullable = false, length = 1000)
    private String scopes;

    /**
     * 客户端设置
     */
    @Column(name = "client_settings", nullable = false, length = 2000)
    private String clientSettings;

    /**
     * 令牌设置
     */
    @Column(name = "token_settings", nullable = false, length = 2000)
    private String tokenSettings;

    /**
     * 获取id
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:44
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     * @since 2023-07-10 17:12:44
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取客户端id
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:45
     */
    @Override
    public String getClientId() {
        return clientId;
    }

    /**
     * 设置客户端id
     *
     * @param clientId 客户端id
     * @since 2023-07-10 17:12:45
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * 获取客户秘密
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:45
     */
    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * 设置客户端秘密
     *
     * @param clientSecret 客户秘密
     * @since 2023-07-10 17:12:46
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * 获取客户端名称
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:46
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 设置客户端名称
     *
     * @param clientName 客户名称
     * @since 2023-07-10 17:12:47
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * 获取范围
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:47
     */
    public String getScopes() {
        return scopes;
    }

    /**
     * 设置范围
     *
     * @param scopes 范围
     * @since 2023-07-10 17:12:48
     */
    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    /**
     * 获取客户端设置
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:48
     */
    public String getClientSettings() {
        return clientSettings;
    }

    /**
     * 设置客户端设置
     *
     * @param clientSettings 客户端设置
     * @since 2023-07-10 17:12:49
     */
    public void setClientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
    }

    /**
     * 获取令牌设置
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:50
     */
    public String getTokenSettings() {
        return tokenSettings;
    }

    /**
     * 设置令牌设置
     *
     * @param tokenSettings 令牌设置
     * @since 2023-07-10 17:12:50
     */
    public void setTokenSettings(String tokenSettings) {
        this.tokenSettings = tokenSettings;
    }

    /**
     * 等于
     *
     * @param o o
     * @return boolean
     * @since 2023-07-10 17:12:50
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TtcRegisteredClient that = (TtcRegisteredClient) o;
        return Objects.equals(id, that.id);
    }

    /**
     * 哈希码
     *
     * @return int
     * @since 2023-07-10 17:12:50
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * 要字符串
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:50
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("clientId", clientId)
                .add("clientSecret", clientSecret)
                .add("clientName", clientName)
                .add("scopes", scopes)
                .add("clientSettings", clientSettings)
                .add("tokenSettings", tokenSettings)
                .toString();
    }
}
