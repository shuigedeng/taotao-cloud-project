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

package com.taotao.cloud.workflow.biz.common.model.form.materialrequisition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.*;

/** 领料单 */
@Data
public class MaterialRequisitionForm {
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;

    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;

    @NotBlank(message = "必填")
    @Schema(description = "领料人")
    private String leadPeople;

    @NotBlank(message = "必填")
    @Schema(description = "领料部门")
    private String leadDepartment;

    @NotNull(message = "必填")
    @Schema(description = "领料日期")
    private Long leadDate;

    @Schema(description = "仓库")
    private String warehouse;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "明细")
    List<MaterialEntryEntityInfoModel> entryList;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
