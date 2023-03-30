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

package com.taotao.cloud.workflow.biz.common.model.engine.flowdelegate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** */
@Data
public class FlowDelegateCrForm {
    @Schema(description = "流程分类")
    @NotBlank(message = "必填")
    private String flowCategory;

    @Schema(description = "被委托人")
    @NotBlank(message = "必填")
    private String toUserName;

    @Schema(description = "被委托人id")
    @NotBlank(message = "必填")
    private String toUserId;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "开始日期")
    @NotNull(message = "必填")
    private Long startTime;

    @Schema(description = "结束日期")
    @NotNull(message = "必填")
    private Long endTime;

    @Schema(description = "委托流程id")
    @NotBlank(message = "必填")
    private String flowId;

    @Schema(description = "委托流程名称")
    @NotBlank(message = "必填")
    private String flowName;
}
