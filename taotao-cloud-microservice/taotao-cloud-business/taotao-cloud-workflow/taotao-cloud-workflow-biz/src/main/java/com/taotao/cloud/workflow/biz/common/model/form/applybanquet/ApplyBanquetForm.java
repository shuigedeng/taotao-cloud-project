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

package com.taotao.cloud.workflow.biz.common.model.form.applybanquet;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 宴请申请 */
@Data
public class ApplyBanquetForm {
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "预计费用", example = "1")
    private BigDecimal expectedCost;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "宴请人员")
    private String banquetPeople;

    @NotBlank(message = "申请人员不能为空")
    @Schema(description = "申请人员")
    private String applyUser;

    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "人员总数")
    private String total;

    @NotBlank(message = "所属职务不能为空")
    @Schema(description = "所属职务")
    private String position;

    @Schema(description = "宴请地点")
    private String place;

    @NotNull(message = "申请日期不能为空")
    @Schema(description = "申请日期")
    private Long applyDate;

    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "宴请人数")
    private String banquetNum;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
