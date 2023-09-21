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

package com.taotao.cloud.stock.biz.interfaces.assembler;

import com.taotao.cloud.stock.api.model.dto.RoleDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;

/**
 * 角色Assembler
 *
 * @author shuigedeng
 * @since 2021-02-18
 */
public class RoleDTOAssembler {

    public static RoleDTO fromRole(final Role role) {
        final RoleDTO dto = new RoleDTO();
        dto.setId(role.getRoleId() == null ? null : role.getRoleId().getId());
        dto.setRoleCode(role.getRoleCode() == null ? null : role.getRoleCode().getCode());
        dto.setRoleName(role.getRoleName() == null ? null : role.getRoleName().getName());
        dto.setRemarks(role.getRemarks());
        if (role.getPermissionIds() != null) {
            List<String> permissionIdList = new ArrayList<>();
            role.getPermissionIds().forEach(permissionId -> {
                permissionIdList.add(permissionId.getId());
            });
            dto.setPermissionIdList(permissionIdList);
        }
        dto.setStatus(role.getStatus() == null ? null : role.getStatus().getValue());
        return dto;
    }

    public static Role toRole(final RoleCommand roleCommand) {
        RoleId roleId = null;
        if (roleCommand.getId() != null) {
            roleId = new RoleId(roleCommand.getId());
        }
        RoleCode roleCode = null;
        if (roleCommand.getRoleCode() != null) {
            roleCode = new RoleCode(roleCommand.getRoleCode());
        }
        RoleName roleName = null;
        if (roleCommand.getRoleName() != null) {
            roleName = new RoleName(roleCommand.getRoleName());
        }
        List<PermissionId> permissionIdList = null;
        if (roleCommand.getPermissionIdList() != null) {
            permissionIdList = new ArrayList<>();
            for (String permissionId : roleCommand.getPermissionIdList()) {
                permissionIdList.add(new PermissionId(permissionId));
            }
        }
        Role Role = new Role(roleId, roleCode, roleName, roleCommand.getRemarks(), null, permissionIdList);
        return Role;
    }

    public static RoleDTO getRoleDTO(final SysRoleDO sysRoleDO) {
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(sysRoleDO, roleDTO);
        return roleDTO;
    }

    public static List<RoleDTO> getRoleDTOList(final List<SysRoleDO> roles) {
        if (roles == null) {
            return null;
        }
        final List<RoleDTO> List = new ArrayList<>();
        for (SysRoleDO sysRoleDO : roles) {
            List.add(getRoleDTO(sysRoleDO));
        }
        return List;
    }
}
