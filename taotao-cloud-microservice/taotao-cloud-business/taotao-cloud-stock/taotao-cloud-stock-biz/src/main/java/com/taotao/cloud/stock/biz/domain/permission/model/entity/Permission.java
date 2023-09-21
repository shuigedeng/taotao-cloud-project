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

package com.taotao.cloud.stock.biz.domain.permission.model.entity;

import com.taotao.cloud.stock.biz.domain.model.permission.MenuUrl;
import com.taotao.cloud.stock.biz.domain.model.permission.PermissionCodes;
import com.taotao.cloud.stock.biz.domain.model.permission.PermissionId;
import com.taotao.cloud.stock.biz.domain.model.permission.PermissionLevelEnum;
import com.taotao.cloud.stock.biz.domain.model.permission.PermissionName;
import com.taotao.cloud.stock.biz.domain.model.permission.PermissionTypeEnum;
import com.taotao.cloud.stock.biz.domain.permission.model.enums.PermissionLevelEnum;
import com.taotao.cloud.stock.biz.domain.permission.model.enums.PermissionTypeEnum;
import com.taotao.cloud.stock.biz.domain.permission.model.vo.MenuUrl;
import com.taotao.cloud.stock.biz.domain.permission.model.vo.PermissionCodes;
import com.taotao.cloud.stock.biz.domain.permission.model.vo.PermissionId;
import com.taotao.cloud.stock.biz.domain.permission.model.vo.PermissionName;
import java.util.List;

/**
 * 权限
 *
 * @author shuigedeng
 * @since 2021-02-08
 */
public class Permission implements Entity<Permission> {

    public static final String ROOT_ID = "0";

    /** id */
    private PermissionId permissionId;

    /** 权限名称 */
    private PermissionName permissionName;

    /** 权限类型 */
    private PermissionTypeEnum permissionType;

    /** 权限级别 */
    private PermissionLevelEnum permissionLevel;

    /** 菜单图标 */
    private String menuIcon;

    /** 权限编码 */
    private PermissionCodes permissionCodes;

    /** 排序 */
    private int orderNum;

    /** 菜单URL */
    private com.taotao.cloud.stock.biz.domain.model.permission.MenuUrl menuUrl;

    /** 父权限 */
    private Permission parent;

    /** 状态 */
    private StatusEnum status;

    /** 子权限 */
    private List<Permission> subList;

    public Permission(PermissionId permissionId) {
        this.permissionId = permissionId;
    }

    public Permission(
            PermissionId permissionId,
            PermissionName permissionName,
            PermissionTypeEnum permissionType,
            PermissionLevelEnum permissionLevel,
            String menuIcon,
            PermissionCodes permissionCodes,
            int orderNum,
            com.taotao.cloud.stock.biz.domain.model.permission.MenuUrl menuUrl,
            Permission parent,
            StatusEnum status,
            List<Permission> subList) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.permissionType = permissionType;
        this.permissionLevel = permissionLevel;
        this.menuIcon = menuIcon;
        this.permissionCodes = permissionCodes;
        this.orderNum = orderNum;
        this.menuUrl = menuUrl;
        this.parent = parent;
        this.status = status;
        this.subList = subList;
    }

    @Override
    public boolean sameIdentityAs(Permission other) {
        return other != null && permissionId.sameValueAs(other.permissionId);
    }

    /**
     * 是否是菜单
     *
     * @return
     */
    public boolean isMenu() {
        return permissionType == PermissionTypeEnum.CATALOG || permissionType == PermissionTypeEnum.MENU;
    }

    /**
     * 是否有子权限
     *
     * @return
     */
    public boolean hasSub() {
        return subList != null && !subList.isEmpty();
    }

    /** 禁用 */
    public void disable() {
        if (this.permissionType == PermissionTypeEnum.CATALOG) {
            throw new IllegalArgumentException("目录无法启用或禁用");
        }
        StatusEnum status = this.status == StatusEnum.DISABLE ? StatusEnum.ENABLE : StatusEnum.DISABLE;
        this.status = status;
        if (subList != null && !subList.isEmpty()) {
            for (Permission subPermission : subList) {
                subPermission.status = status;
            }
        }
    }

    public PermissionId getPermissionId() {
        return permissionId;
    }

    public PermissionName getPermissionName() {
        return permissionName;
    }

    public PermissionTypeEnum getPermissionType() {
        return permissionType;
    }

    public PermissionLevelEnum getPermissionLevel() {
        return permissionLevel;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public PermissionCodes getPermissionCodes() {
        return permissionCodes;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public MenuUrl getMenuUrl() {
        return menuUrl;
    }

    public Permission getParent() {
        return parent;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public List<Permission> getSubList() {
        return subList;
    }
}
