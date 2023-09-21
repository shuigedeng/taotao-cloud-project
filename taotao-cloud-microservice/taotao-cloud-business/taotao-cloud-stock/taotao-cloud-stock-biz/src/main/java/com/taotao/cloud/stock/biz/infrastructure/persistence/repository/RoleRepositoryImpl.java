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

package com.taotao.cloud.stock.biz.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 角色-Repository实现类
 *
 * @author shuigedeng
 * @since 2021-02-18
 */
@Repository
public class RoleRepositoryImpl extends ServiceImpl<SysRoleMapper, SysRoleDO>
        implements RoleRepository, IService<SysRoleDO> {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public Role find(RoleId roleId) {
        SysRoleDO sysRoleDO = this.getById(roleId.getId());
        if (sysRoleDO == null) {
            return null;
        }
        Role role = RoleConverter.toRole(sysRoleDO, getRolePermission(sysRoleDO.getRoleCode(), sysRoleDO.getId()));
        return role;
    }

    @Override
    public Role find(RoleName roleName) {
        SysRoleDO sysRoleDO = this.getOne(new QueryWrapper<SysRoleDO>().eq("role_name", roleName.getName()));
        if (sysRoleDO == null) {
            return null;
        }
        Role role = RoleConverter.toRole(sysRoleDO, getRolePermission(sysRoleDO.getRoleCode(), sysRoleDO.getId()));
        return role;
    }

    @Override
    public Role find(RoleCode roleCode) {
        SysRoleDO sysRoleDO = this.getOne(new QueryWrapper<SysRoleDO>().eq("role_code", roleCode.getCode()));
        if (sysRoleDO == null) {
            return null;
        }
        Role role = RoleConverter.toRole(sysRoleDO, getRolePermission(sysRoleDO.getRoleCode(), sysRoleDO.getId()));
        return role;
    }

    private List<SysPermissionDO> getRolePermission(String roleCode, String roleId) {
        List<SysPermissionDO> sysPermissionDOList;
        sysPermissionDOList = sysPermissionMapper.queryPermissionByRoleId(roleId);
        return sysPermissionDOList;
    }

    @Override
    public RoleId store(Role role) {
        SysRoleDO sysRoleDO = RoleConverter.fromRole(role);
        this.saveOrUpdate(sysRoleDO);
        String roleId = sysRoleDO.getId();
        // 先删除角色与菜单关系
        List<String> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        sysRolePermissionMapper.deleteByRoleIds(roleIds);
        List<PermissionId> permissionIds = role.getPermissionIds();
        if (permissionIds != null && !permissionIds.isEmpty()) {
            // 保存角色与菜单关系
            for (PermissionId permissionId : permissionIds) {
                SysRolePermissionDO sysRolePermissionDO = new SysRolePermissionDO();
                sysRolePermissionDO.setPermissionId(permissionId.getId());
                sysRolePermissionDO.setRoleId(roleId);
                sysRolePermissionMapper.insert(sysRolePermissionDO);
            }
        }
        return new RoleId(sysRoleDO.getId());
    }

    @Override
    public void remove(List<RoleId> roleIds) {
        List<String> ids = new ArrayList<>();
        roleIds.forEach(roleId -> {
            ids.add(roleId.getId());
        });
        // 删除角色
        this.removeByIds(ids);
        // 删除角色与菜单关联
        sysRolePermissionMapper.deleteByRoleIds(ids);
        // 删除角色与用户关联
        sysUserRoleMapper.deleteByRoleIds(ids);
    }
}
