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
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 权限-Repository实现类
 *
 * @author shuigedeng
 * @since 2021-02-14
 */
@Repository
public class PermissionRepositoryImpl extends ServiceImpl<SysPermissionMapper, SysPermissionDO>
        implements PermissionRepository, IService<SysPermissionDO> {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public List<Permission> queryList(Map<String, Object> params) {
        List<Permission> permissions = new ArrayList<>();
        List<SysPermissionDO> list = getBaseMapper().queryList(params);
        for (SysPermissionDO sysPermissionDO : list) {
            Permission permission = PermissionConverter.toPermission(
                    sysPermissionDO, getParentPermission(sysPermissionDO.getParentId()), null);
            permissions.add(permission);
        }
        return permissions;
    }

    @Override
    public List<Permission> queryList(RoleCode rolecode) {
        List<Permission> permissions = new ArrayList<>();
        List<SysPermissionDO> list = getBaseMapper().queryPermissionByRoleCode(rolecode.getCode());
        for (SysPermissionDO sysPermissionDO : list) {
            Permission permission = PermissionConverter.toPermission(
                    sysPermissionDO, getParentPermission(sysPermissionDO.getParentId()), null);
            permissions.add(permission);
        }
        return permissions;
    }

    @Override
    public Permission find(PermissionId permissionId) {
        SysPermissionDO sysPermissionDO = this.getById(permissionId.getId());
        if (sysPermissionDO == null) {
            return null;
        }
        Permission permission = PermissionConverter.toPermission(
                sysPermissionDO,
                getParentPermission(sysPermissionDO.getParentId()),
                getSubPermission(sysPermissionDO.getId()));
        return permission;
    }

    @Override
    public Permission find(PermissionName permissionName) {
        SysPermissionDO sysPermissionDO =
                this.getOne(new QueryWrapper<SysPermissionDO>().eq("permission_name", permissionName.getName()));
        if (sysPermissionDO == null) {
            return null;
        }
        Permission permission = PermissionConverter.toPermission(
                sysPermissionDO,
                getParentPermission(sysPermissionDO.getParentId()),
                getSubPermission(sysPermissionDO.getId()));
        return permission;
    }

    /**
     * 设置父权限
     *
     * @param parentId
     */
    private SysPermissionDO getParentPermission(String parentId) {
        SysPermissionDO parent = null;
        if (parentId != null) {
            parent = this.getById(parentId);
        }
        return parent;
    }

    /**
     * 设置子权限
     *
     * @param permissionId
     */
    private List<SysPermissionDO> getSubPermission(String permissionId) {
        List<SysPermissionDO> list = this.list(new QueryWrapper<SysPermissionDO>().eq("parent_id", permissionId));
        return list;
    }

    @Override
    public PermissionId store(Permission permission) {
        SysPermissionDO sysPermissionDO = PermissionConverter.fromPermission(permission);
        this.saveOrUpdate(sysPermissionDO);
        return new PermissionId(sysPermissionDO.getId());
    }

    @Override
    public void remove(PermissionId permissionId) {
        this.removeById(permissionId.getId());
        List<String> permissionIds = new ArrayList<>();
        permissionIds.add(permissionId.getId());
        sysRolePermissionMapper.deleteByPermissionIds(permissionIds);
    }
}
