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

package com.taotao.cloud.stock.biz.interfaces.command;

import io.swagger.annotations.ApiModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 权限Command
 *
 * @author shuigedeng
 * @since 2021-02-18
 */
@Data
@ApiModel(value = "权限", description = "权限")
public class PermissionCommand {

    /** id */
    @Schema(description = "ID")
    @NotBlank(message = "ID不能为空", groups = UpdateGroup.class)
    private String id;

    /** 父级ID */
    @Schema(description = "父级ID")
    @NotBlank(
            message = "父级ID不能为空",
            groups = {AddGroup.class, UpdateGroup.class})
    private String parentId;

    /** 权限名称 */
    @Schema(description = "权限名称")
    @NotBlank(
            message = "权限名称不能为空",
            groups = {AddGroup.class})
    private String permissionName;

    /** 权限类型 */
    @Schema(description = "权限类型")
    @NotBlank(
            message = "权限类型不能为空",
            groups = {AddGroup.class})
    private String permissionType;

    /** 权限级别 */
    @Schema(description = "权限级别")
    @NotBlank(
            message = "权限级别不能为空",
            groups = {AddGroup.class})
    private String permissionLevel;

    /** 权限编码 */
    @Schema(description = "权限编码")
    private String permissionCodes;

    /** 菜单图标 */
    @Schema(description = "菜单图标")
    private String menuIcon;

    /** 排序 */
    @Schema(description = "排序")
    private int orderNum;

    /** 菜单url */
    @Schema(description = "菜单url")
    private String menuUrl;
}
