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

package com.taotao.cloud.workflow.biz.common.model.form.vehicleapply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 车辆申请 */
@Data
public class VehicleApplyForm {
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @NotNull(message = "结束时间不能为空")
    @Schema(description = "结束时间")
    private Long endDate;

    @Schema(description = "目的地")
    private String destination;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "公里数")
    private String kilometreNum;

    @Schema(description = "车牌号")
    private String plateNum;

    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "路费金额")
    private BigDecimal roadFee;

    @NotBlank(message = "所在部门不能为空")
    @Schema(description = "所在部门")
    private String department;

    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;

    @NotBlank(message = "用车人不能为空")
    @Schema(description = "用车人")
    private String carMan;

    @Schema(description = "随行人数")
    private String entourage;

    @NotNull(message = "用车日期不能为空")
    @Schema(description = "用车日期")
    private Long startDate;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
