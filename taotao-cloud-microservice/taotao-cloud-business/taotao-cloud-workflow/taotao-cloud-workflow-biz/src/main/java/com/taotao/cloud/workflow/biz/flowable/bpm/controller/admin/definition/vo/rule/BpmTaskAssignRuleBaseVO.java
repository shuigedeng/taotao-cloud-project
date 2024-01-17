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

package com.taotao.cloud.workflow.biz.flowable.bpm.controller.admin.definition.vo.rule;

import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 流程任务分配规则 Base VO，提供给添加、修改、详细的子 VO 使用 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成 */
@Data
public class BpmTaskAssignRuleBaseVO {

    @ApiModelProperty(value = "规则类型", required = true, example = "bpm_task_assign_rule_type")
    @NotNull(message = "规则类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "规则值数组", required = true, example = "1,2,3")
    @NotNull(message = "规则值数组不能为空")
    private Set<Long> options;
}
