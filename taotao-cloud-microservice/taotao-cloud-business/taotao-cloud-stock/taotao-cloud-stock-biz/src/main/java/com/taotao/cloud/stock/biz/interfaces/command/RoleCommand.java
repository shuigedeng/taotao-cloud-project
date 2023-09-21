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
import java.util.List;
import lombok.Data;

/**
 * 角色Command
 *
 * @author shuigedeng
 * @since 2021-02-18
 */
@Data
@ApiModel(value = "角色", description = "角色")
public class RoleCommand {

    /** id */
    @Schema(description = "角色id")
    @NotBlank(message = "角色id不能为空", groups = UpdateGroup.class)
    private String id;

    /** 角色编码 */
    @Schema(description = "角色编码")
    @NotBlank(message = "角色编码不能为空", groups = AddGroup.class)
    private String roleCode;

    /** 角色名称 */
    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不能为空", groups = AddGroup.class)
    private String roleName;

    /** 备注 */
    @Schema(description = "备注")
    private String remarks;

    /** 权限 */
    @Schema(description = "权限")
    private List<String> permissionIdList;
}
