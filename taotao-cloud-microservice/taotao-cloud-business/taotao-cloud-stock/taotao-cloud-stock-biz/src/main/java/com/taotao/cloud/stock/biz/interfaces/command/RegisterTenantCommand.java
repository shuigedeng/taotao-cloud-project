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
 * 注册租户Command
 *
 * @author shuigedeng
 * @since 2021-02-14
 */
@Data
@ApiModel(value = "注册租户", description = "注册租户")
public class RegisterTenantCommand {

    /** 租户名 */
    @Schema(description = "租户名")
    @NotBlank(message = "租户名不能为空")
    private String tenantName;

    /** 租户编码 */
    @Schema(description = "租户编码")
    @NotBlank(message = "租户编码不能为空")
    private String tenantCode;

    /** 用户名 */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /** 手机号 */
    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空", groups = AddGroup.class)
    private String mobile;

    /** 密码 */
    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空", groups = AddGroup.class)
    private String password;

    /** 验证码 */
    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    /** uuid */
    @Schema(description = "uuid")
    @NotBlank(message = "uuid不能为空")
    private String uuid;
}
