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

package com.taotao.cloud.workflow.biz.common.model.app;

import lombok.Data;

/** */
@Data
public class AppInfoModel {
    @Schema(description = "用户id")
    private String id;

    @Schema(description = "用户账号")
    private String account;

    @Schema(description = "用户姓名")
    private String realName;

    @Schema(description = "用户头像")
    private String headIcon;

    @Schema(description = "组织名称")
    private String organizeName;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "岗位名称")
    private String positionName;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "生日")
    private Long birthday;

    @Schema(description = "手机号码")
    private String mobilePhone;

    @Schema(description = "邮箱")
    private String email;
}
