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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * Assembler class for the Permission.
 *
 * @author shuigedeng
 * @since 2021-02-17
 */
public class PermissionDTOAssembler {

    public static PermissionDTO fromPermission(final Permission permission) {
        final PermissionDTO dto = new PermissionDTO();
        dto.setId(
                permission.getPermissionId() == null
                        ? null
                        : permission.getPermissionId().getId());
        dto.setPermissionName(
                permission.getPermissionName() == null
                        ? null
                        : permission.getPermissionName().getName());
        dto.setPermissionType(
                permission.getPermissionType() == null
                        ? null
                        : permission.getPermissionType().getValue());
        dto.setMenuIcon(permission.getMenuIcon());
        dto.setMenuUrl(
                permission.getMenuUrl() == null ? null : permission.getMenuUrl().getUrl());
        dto.setOrderNum(permission.getOrderNum());
        dto.setParentId(
                permission.getParent() == null
                        ? null
                        : permission.getParent().getPermissionId().getId());
        dto.setParentName(
                permission.getParent() == null
                        ? null
                        : permission.getParent().getPermissionName().getName());
        dto.setPermissionCodes(
                permission.getPermissionCodes() == null
                        ? null
                        : permission.getPermissionCodes().getCodesString());
        dto.setPermissionLevel(
                permission.getPermissionLevel() == null
                        ? null
                        : permission.getPermissionLevel().getValue());
        return dto;
    }

    public static Permission toPermission(final PermissionCommand permissionCommand, Permission parent) {
        PermissionId permissionId = null;
        if (permissionCommand.getId() != null) {
            permissionId = new PermissionId(permissionCommand.getId());
        }
        PermissionName permissionName = null;
        if (permissionCommand.getPermissionName() != null) {
            permissionName = new PermissionName(permissionCommand.getPermissionName());
        }
        PermissionTypeEnum permissionType = null;
        if (permissionCommand.getPermissionType() != null) {
            permissionType = PermissionTypeEnum.getMenuTypeEnum(permissionCommand.getPermissionType());
        }
        PermissionLevelEnum permissionLevel = null;
        if (permissionCommand.getPermissionLevel() != null) {
            permissionLevel = PermissionLevelEnum.getMenuLevelEnum(permissionCommand.getPermissionLevel());
        }
        PermissionCodes permissionCodes = null;
        if (permissionCommand.getPermissionCodes() != null) {
            Set<String> permsSet = new HashSet<>();
            permsSet.addAll(
                    Arrays.asList(permissionCommand.getPermissionCodes().trim().split(",")));
            permissionCodes = new PermissionCodes(permsSet);
        }
        MenuUrl menuUrl = null;
        if (!StringUtils.isEmpty(permissionCommand.getMenuUrl())) {
            menuUrl = new MenuUrl(permissionCommand.getMenuUrl());
        }
        Permission permission = new Permission(
                permissionId,
                permissionName,
                permissionType,
                permissionLevel,
                permissionCommand.getMenuIcon(),
                permissionCodes,
                permissionCommand.getOrderNum(),
                menuUrl,
                parent,
                null,
                null);
        return permission;
    }

    public static List<PermissionDTO> getPermissionList(final List<Permission> permissionList) {
        if (permissionList == null) {
            return null;
        }
        final List<PermissionDTO> List = new ArrayList<>();
        for (Permission permission : permissionList) {
            List.add(fromPermission(permission));
        }
        return List;
    }

    public static List<PermissionDTO> getMenuList(final List<Permission> permissionList) {
        if (permissionList == null) {
            return null;
        }
        final List<PermissionDTO> List = new ArrayList<>();
        for (Permission permission : permissionList) {
            if (permission.isMenu()) {
                List.add(fromPermission(permission));
            }
        }
        return List;
    }
}
