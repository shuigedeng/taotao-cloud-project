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
 * 用户Command
 *
 * @author shuigedeng
 * @since 2021-02-20
 */
@Data
@ApiModel(value = "用户", description = "用户")
public class UserCommand {

    /** id */
    @Schema(description = "用户id")
    @NotBlank(message = "用户id不能为空", groups = UpdateGroup.class)
    private String id;

    /** 用户名 */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空", groups = AddGroup.class)
    private String userName;

    /** 手机号 */
    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空", groups = AddGroup.class)
    private String mobile;

    /** 邮箱 */
    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱不能为空", groups = AddGroup.class)
    private String email;

    /** 角色列表 */
    @Schema(description = "角色列表")
    private List<String> roleIdList;
}
