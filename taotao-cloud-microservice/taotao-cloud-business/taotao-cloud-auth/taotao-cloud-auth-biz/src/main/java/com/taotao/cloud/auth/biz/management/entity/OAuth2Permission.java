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
import com.taotao.boot.data.jpa.tenant.BaseSysEntity;
import com.taotao.boot.security.spring.constants.OAuth2Constants;
import com.taotao.cloud.auth.biz.management.generator.OAuth2PermissionUuid;
import jakarta.persistence.*;
import java.util.Objects;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * <p>客户端权限 </p>
 *
 *
 * @since : 2022/4/1 13:39
 */
@Entity
@Table(
        name = "oauth2_permission",
        indexes = {@Index(name = "oauth2_permission_id_idx", columnList = "permission_id")})
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE,
        region = OAuth2Constants.REGION_OAUTH2_PERMISSION)
public class OAuth2Permission extends BaseSysEntity {

    @Id
    @OAuth2PermissionUuid
    @Column(name = "permission_id", length = 64)
    private String permissionId;

    @Column(name = "permission_code", length = 128)
    private String permissionCode;

    @Column(name = "permission_name", length = 128)
    private String permissionName;

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("permissionId", permissionId)
                .add("permissionCode", permissionCode)
                .add("permissionName", permissionName)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OAuth2Permission that = (OAuth2Permission) o;
        return Objects.equals(permissionId, that.permissionId)
                && Objects.equals(permissionCode, that.permissionCode)
                && Objects.equals(permissionName, that.permissionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionId, permissionCode, permissionName);
    }
}
