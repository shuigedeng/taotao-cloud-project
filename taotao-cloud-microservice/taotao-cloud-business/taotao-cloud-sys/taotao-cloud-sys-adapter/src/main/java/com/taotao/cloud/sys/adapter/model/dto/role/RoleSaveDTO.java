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

package com.taotao.cloud.sys.adapter.model.dto.role;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 角色添加对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:25:01
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "角色添加对象")
public class RoleSaveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4132785717179910025L;

    @Schema(description = "角色名称", requiredMode = REQUIRED)
    @NotBlank(message = "角色名称不能超过为空")
    @Length(max = 20, message = "角色名称不能超过20个字符")
    private String name;

    @Schema(description = "角色标识", requiredMode = REQUIRED)
    @NotBlank(message = "角色标识不能超过为空")
    @Length(max = 20, message = "角色标识不能超过20个字符")
    @Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "角色标识格式错误：最多20字符，只能包含字母或者下划线")
    private String code;

    @Schema(description = "备注")
    private String remark;
}
