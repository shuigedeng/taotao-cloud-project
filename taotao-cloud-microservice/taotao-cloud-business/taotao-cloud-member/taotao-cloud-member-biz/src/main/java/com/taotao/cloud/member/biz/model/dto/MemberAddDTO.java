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

package com.taotao.cloud.member.biz.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 添加会员DTO
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-14 11:24:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "添加会员DTO")
public class MemberAddDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7605952923416404638L;

    @NotEmpty(message = "会员用户名必填")
    @Size(max = 30, message = "会员用户名最长30位")
    @Schema(description = "会员用户名")
    private String username;

    @Schema(description = "会员密码")
    private String password;

    @NotEmpty(message = "手机号码不能为空")
    @Schema(description = "手机号码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String mobile;
}
