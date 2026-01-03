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

package com.taotao.cloud.sys.biz.model.dto.app;

import com.taotao.boot.common.model.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/**
 * AppDTO
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "系统应用")
public class AppDTO implements Serializable {

    private static final long serialVersionUID = -1L;

    @Schema(description = "主键")
    @NotNull(message = "主键不能为空!", groups = ValidationGroups.Update.class)
    private Long id;

    @Schema(description = "应用名称")
    @Size(min = 1, max = 10, message = "应用名称长度在1-10之间!", groups = ValidationGroups.Create.class)
    @NotNull(message = "应用名称不能为空!", groups = ValidationGroups.Create.class)
    private String name;

    @Schema(description = "应用编码")
    @Size(min = 1, max = 10, message = "应用编码长度在1-10之间!", groups = ValidationGroups.Create.class)
    @NotNull(message = "应用编码不能为空!", groups = ValidationGroups.Create.class)
    private String code;

    @Schema(description = "图标")
    @NotNull(message = "图标不能为空!", groups = ValidationGroups.Create.class)
    private String icon;

    @Schema(description = "排序")
    @NotNull(message = "排序不能为空!", groups = ValidationGroups.Create.class)
    private Integer sort;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
