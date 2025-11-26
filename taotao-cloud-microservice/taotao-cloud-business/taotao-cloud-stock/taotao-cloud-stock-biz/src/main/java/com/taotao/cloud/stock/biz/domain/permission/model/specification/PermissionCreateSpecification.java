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

package com.taotao.cloud.stock.biz.domain.permission.model.specification;

import com.taotao.cloud.stock.biz.domain.permission.model.enums.PermissionTypeEnum;
import com.taotao.cloud.stock.biz.domain.permission.repository.PermissionRepository;
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * 权限创建Specification
 *
 * @author shuigedeng
 * @since 2021-02-20
 */
public class PermissionCreateSpecification extends AbstractSpecification<Permission> {

    private PermissionRepository permissionRepository;

    public PermissionCreateSpecification(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean isSatisfiedBy(Permission permission) {
        if (permission.getPermissionName() != null) {
            Permission existPermission = permissionRepository.find(permission.getPermissionName());
            if (existPermission != null
                    && !existPermission.getPermissionId().sameValueAs(permission.getPermissionId())) {
                throw new IllegalArgumentException("权限名已存在");
            }
        }
        if (permission.getParent() == null) {
            throw new IllegalArgumentException("上级菜单不能为空");
        }

        if (permission.getPermissionType() == null) {
            throw new IllegalArgumentException("菜单类型不能为空");
        }

        // 菜单
        if (permission.getPermissionType() == PermissionTypeEnum.MENU) {
            if (permission.getMenuUrl() == null
                    || StringUtils.isBlank(permission.getMenuUrl().getUrl())) {
                throw new IllegalArgumentException("菜单URL不能为空");
            }
        }

        // 上级菜单类型
        PermissionTypeEnum parentType = permission.getParent().getPermissionType();

        // 目录、菜单
        if (permission.getPermissionType() == PermissionTypeEnum.CATALOG
                || permission.getPermissionType() == PermissionTypeEnum.MENU) {
            if (parentType != PermissionTypeEnum.CATALOG) {
                throw new IllegalArgumentException("上级菜单只能为目录类型");
            }
        }

        // 按钮
        if (permission.getPermissionType() == PermissionTypeEnum.BUTTON) {
            if (parentType != PermissionTypeEnum.MENU) {
                throw new IllegalArgumentException("上级菜单只能为菜单类型");
            }
        }
        return true;
    }
}
