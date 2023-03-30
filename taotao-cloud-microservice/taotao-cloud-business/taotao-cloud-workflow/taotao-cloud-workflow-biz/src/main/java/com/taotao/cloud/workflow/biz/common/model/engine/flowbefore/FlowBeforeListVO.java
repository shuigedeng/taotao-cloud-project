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

package com.taotao.cloud.workflow.biz.common.model.engine.flowbefore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** */
@Data
public class FlowBeforeListVO {

    @Schema(description = "流程编码")
    private String enCode;

    @Schema(description = "发起人员")
    private Long creatorUserId;

    @Schema(description = "接收时间")
    private Long creatorTime;

    @Schema(description = "经办节点")
    private String thisStep;

    @Schema(description = "节点id")
    private String thisStepId;

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

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "流程编码")
    private String flowCode;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "实例进程")
    private String processId;

    @Schema(description = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "节点")
    private String nodeName;

    @Schema(description = "节点对象")
    private String approversProperties;

    @Schema(description = "版本")
    private String flowVersion;
}
