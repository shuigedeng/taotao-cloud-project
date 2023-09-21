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

package com.taotao.cloud.stock.biz.domain.role.model.entity;

import com.taotao.cloud.stock.biz.domain.model.role.RoleCode;
import com.taotao.cloud.stock.biz.domain.model.role.RoleId;
import com.taotao.cloud.stock.biz.domain.model.role.RoleName;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleCode;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleId;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleName;
import java.util.List;

/**
 * 角色
 *
 * @author shuigedeng
 * @since 2021-02-08
 */
public class Role implements Entity<Role> {

    /** roleId */
    private RoleId roleId;

    /** 角色编码 */
    private RoleCode roleCode;

    /** 角色名称 */
    private RoleName roleName;

    /** 备注 */
    private String remarks;

    /** 状态 */
    private StatusEnum status;

    /** 权限列表 */
    private List<PermissionId> permissionIds;

    public Role(
            RoleId roleId,
            RoleCode roleCode,
            RoleName roleName,
            String remarks,
            StatusEnum status,
            List<PermissionId> permissionIds) {
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.remarks = remarks;
        this.status = status;
        this.permissionIds = permissionIds;
    }

    public Role(RoleCode roleCode, RoleName roleName, List<PermissionId> permissionIds) {
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.permissionIds = permissionIds;
    }

    /** 禁用 */
    public void disable() {
        StatusEnum status = this.status == StatusEnum.DISABLE ? StatusEnum.ENABLE : StatusEnum.DISABLE;
        this.status = status;
    }

    @Override
    public boolean sameIdentityAs(Role other) {
        return other != null && roleId.sameValueAs(other.roleId);
    }

    public RoleId getRoleId() {
        return roleId;
    }

    public RoleCode getRoleCode() {
        return roleCode;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public String getRemarks() {
        return remarks;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public List<PermissionId> getPermissionIds() {
        return permissionIds;
    }
}
