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

package com.taotao.cloud.auth.biz.management.entity;

import com.google.common.base.MoreObjects;
import com.taotao.boot.security.spring.constants.OAuth2Constants;
import com.taotao.cloud.auth.biz.management.definition.AbstractOAuth2RegisteredClient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UuidGenerator;

/**
 * <p>物联网设备管理 </p>
 *
 *
 * @since : 2023/5/15 14:26
 */
@Schema(name = "物联网设备")
@Entity
@Table(
        name = "oauth2_device",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"device_name"})},
        indexes = {
            @Index(name = "oauth2_device_id_idx", columnList = "device_id"),
            @Index(name = "oauth2_device_ipk_idx", columnList = "device_name"),
            @Index(name = "oauth2_device_pid_idx", columnList = "product_id")
        })
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE,
        region = OAuth2Constants.REGION_OAUTH2_IOT_DEVICE)
public class OAuth2Device extends AbstractOAuth2RegisteredClient {

    @Schema(name = "设备ID")
    @Id
    @UuidGenerator
    @Column(name = "device_id", length = 64)
    private String deviceId;

    @Schema(name = "设备名称")
    @Column(name = "device_name", length = 64, unique = true)
    private String deviceName;

    @Schema(name = "产品ID")
    @Column(name = "product_id", length = 64)
    private String productId;

    @Schema(name = "是否已激活", title = "设备是否已经激活状态标记，默认值false，即未激活")
    @Column(name = "is_activated")
    private Boolean activated = Boolean.FALSE;

    @Schema(name = "设备对应Scope", title = "传递设备对应Scope ID数组")
    @org.hibernate.annotations.Cache(
            usage = CacheConcurrencyStrategy.READ_WRITE,
            region = OAuth2Constants.REGION_OAUTH2_APPLICATION_SCOPE)
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "oauth2_device_scope",
            joinColumns = {@JoinColumn(name = "device_id")},
            inverseJoinColumns = {@JoinColumn(name = "scope_id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"device_id", "scope_id"})},
            indexes = {
                @Index(name = "oauth2_device_scope_aid_idx", columnList = "device_id"),
                @Index(name = "oauth2_device_scope_sid_idx", columnList = "scope_id")
            })
    private Set<OAuth2Scope> scopes = new HashSet<>();

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    @Override
    public Set<OAuth2Scope> getScopes() {
        return scopes;
    }

    public void setScopes(Set<OAuth2Scope> scopes) {
        this.scopes = scopes;
    }

    @Override
    public String getId() {
        return getDeviceId();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("deviceId", deviceId)
                .add("deviceName", deviceName)
                .add("productId", productId)
                .add("activated", activated)
                .toString();
    }
}
