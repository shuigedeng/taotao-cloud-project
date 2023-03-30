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

package com.taotao.cloud.workflow.biz.common.model.form.travelapply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 出差预支申请单 */
@Data
public class TravelApplyForm {
    @NotBlank(message = "起始地点不能为空")
    @Schema(description = "起始地点")
    private String startPlace;

    @NotBlank(message = "出差人不能为空")
    @Schema(description = "出差人")
    private String travelMan;

    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @NotNull(message = "结束日期不能为空")
    @Schema(description = "结束日期")
    private Long endDate;

    @Schema(description = "预支旅费")
    private BigDecimal prepaidTravel;

    @NotBlank(message = "目的地不能为空")
    @Schema(description = "目的地")
    private String destination;

    @Schema(description = "备注")
    private String description;

    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;

    @NotBlank(message = "所属部门不能为空")
    @Schema(description = "所属部门")
    private String departmental;

    @NotBlank(message = "所属职务不能为空")
    @Schema(description = "所属职务")
    private String position;

    @NotNull(message = "申请日期不能为空")
    @Schema(description = "申请日期")
    private Long applyDate;

    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;

    @NotNull(message = "开始日期不能为空")
    @Schema(description = "开始日期")
    private Long startDate;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
