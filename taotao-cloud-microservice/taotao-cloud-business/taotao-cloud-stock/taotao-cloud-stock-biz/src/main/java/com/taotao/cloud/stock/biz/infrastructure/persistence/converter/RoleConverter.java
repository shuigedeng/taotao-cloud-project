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

package com.taotao.cloud.stock.biz.infrastructure.persistence.converter;

import com.taotao.cloud.stock.api.common.domain.StatusEnum;
import com.taotao.cloud.stock.biz.domain.permission.model.vo.PermissionId;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleCode;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleId;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleName;
import com.taotao.cloud.stock.biz.infrastructure.persistence.po.SysPermissionDO;
import com.taotao.cloud.stock.biz.infrastructure.persistence.po.SysRoleDO;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色Converter
 *
 * @author shuigedeng
 * @since 2021-02-19
 */
public class RoleConverter {

    public static Role toRole(SysRoleDO sysRoleDO, List<SysPermissionDO> sysPermissionDOList) {
        if (sysRoleDO == null) {
            throw new BaseException("未找到角色");
        }
        List<PermissionId> permissionIds = null;
        if (sysPermissionDOList != null && !sysPermissionDOList.isEmpty()) {
            permissionIds = new ArrayList<>();
            for (SysPermissionDO sysPermissionDO : sysPermissionDOList) {
                permissionIds.add(new PermissionId(sysPermissionDO.getId()));
            }
        }
        Role role = new Role(
                new RoleId(sysRoleDO.getId()),
                new RoleCode(sysRoleDO.getRoleCode()),
                new RoleName(sysRoleDO.getRoleName()),
                sysRoleDO.getRemarks(),
                StatusEnum.getStatusEnum(sysRoleDO.getStatus()),
                permissionIds);
        return role;
    }

    public static SysRoleDO fromRole(Role role) {
        SysRoleDO sysRoleDO = new SysRoleDO();
        sysRoleDO.setId(role.getRoleId() == null ? null : role.getRoleId().getId());
        sysRoleDO.setRoleCode(
                role.getRoleCode() == null ? null : role.getRoleCode().getCode());
        sysRoleDO.setRoleName(
                role.getRoleName() == null ? null : role.getRoleName().getName());
        sysRoleDO.setRemarks(role.getRemarks());
        sysRoleDO.setStatus(role.getStatus() == null ? null : role.getStatus().getValue());
        return sysRoleDO;
    }
}
