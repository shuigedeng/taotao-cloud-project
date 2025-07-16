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
import com.google.common.base.Objects;
import com.taotao.boot.data.jpa.tenant.AbstractEntity;
import com.taotao.boot.security.spring.constants.OAuth2Constants;
import com.taotao.cloud.auth.biz.jpa.generator.TtcAuthorizationConsentId;
import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RevisionNumber;

/**
 * <p>OAuth2 认证确认信息实体 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:12:38
 */
@Entity
@Audited
@Table(
        name = "oauth2_authorization_consent",
        indexes = {
            @Index(
                    name = "oauth2_authorization_consent_rcid_idx",
                    columnList = "registered_client_id"),
            @Index(name = "oauth2_authorization_consent_pn_idx", columnList = "principal_name")
        })
@IdClass(TtcAuthorizationConsentId.class)
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE,
        region = OAuth2Constants.REGION_OAUTH2_AUTHORIZATION_CONSENT)
public class TtcAuthorizationConsent extends AbstractEntity {

    /**
     * 注册客户端id
     */
    @Id
    @Column(name = "registered_client_id", nullable = false, length = 100)
    private String registeredClientId;

    /**
     * 主体名称
     */
    @Id
    @Column(name = "principal_name", nullable = false, length = 200)
    private String principalName;

    /**
     * 当局
     */
    @Column(name = "authorities", nullable = false, length = 1000)
    private String authorities;

    @Version
    @RevisionNumber
    @Column(name = "version", columnDefinition = "int not null default 1 comment '版本号'")
    private Long version = 1L;

    /**
     * 获取注册客户端id
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:38
     */
    public String getRegisteredClientId() {
        return registeredClientId;
    }

    /**
     * 设置注册客户端id
     *
     * @param registeredClientId 注册客户端id
     * @since 2023-07-10 17:12:38
     */
    public void setRegisteredClientId(String registeredClientId) {
        this.registeredClientId = registeredClientId;
    }

    /**
     * 获取主体名称
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:38
     */
    public String getPrincipalName() {
        return principalName;
    }

    /**
     * 设置主体名称
     *
     * @param principalName 主体名称
     * @since 2023-07-10 17:12:38
     */
    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    /**
     * 获得权威
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:38
     */
    public String getAuthorities() {
        return authorities;
    }

    /**
     * 设置权限
     *
     * @param authorities 当局
     * @since 2023-07-10 17:12:39
     */
    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    /**
     * 等于
     *
     * @param o o
     * @return boolean
     * @since 2023-07-10 17:12:39
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TtcAuthorizationConsent that = (TtcAuthorizationConsent) o;
        return Objects.equal(registeredClientId, that.registeredClientId)
                && Objects.equal(principalName, that.principalName);
    }

    /**
     * 哈希码
     *
     * @return int
     * @since 2023-07-10 17:12:40
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(registeredClientId, principalName);
    }

    /**
     * 要字符串
     *
     * @return {@link String }
     * @since 2023-07-10 17:12:40
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("registeredClientId", registeredClientId)
                .add("principalName", principalName)
                .add("authorities", authorities)
                .toString();
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
