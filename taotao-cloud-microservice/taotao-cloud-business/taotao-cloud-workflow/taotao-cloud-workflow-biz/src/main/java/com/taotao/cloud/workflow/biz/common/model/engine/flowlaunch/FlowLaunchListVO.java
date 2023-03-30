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

package com.taotao.cloud.workflow.biz.common.model.engine.flowlaunch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** */
@Data
public class FlowLaunchListVO {
    @Schema(description = "任务编码")
    private String enCode;

    @Schema(description = "发起人员")
    private String creatorUserId;

    @Schema(description = "创建时间")
    private Long creatorTime;

    @Schema(description = "当前节点")
    private String thisStep;

    @Schema(description = "所属分类")
    private String flowCategory;

    @Schema(description = "流程标题")
    private String fullName;

    @Schema(description = "所属流程")
    private String flowName;

    @Schema(description = "流程状态", example = "1")
    private Integer status;

    @Schema(description = "发起时间")
    private Long startTime;

    @Schema(description = "主键id")
    private String id;

    @Schema(description = "结束时间")
    private Long endTime;

    @Schema(description = "完成情况")
    private Integer completion;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "流程编码")
    private String flowCode;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;

    @Schema(description = "表单数据")
    private String formData;
}
