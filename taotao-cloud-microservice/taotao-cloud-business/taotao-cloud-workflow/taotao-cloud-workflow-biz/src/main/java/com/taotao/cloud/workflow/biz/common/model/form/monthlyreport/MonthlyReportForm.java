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

package com.taotao.cloud.workflow.biz.common.model.form.monthlyreport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 月工作总结 */
@Data
public class MonthlyReportForm {
    @Schema(description = "相关附件")
    private String fileJson;

    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "次月日期")
    private String nPFinishTime;

    @Schema(description = "次月目标")
    private String nFinishMethod;

    @NotBlank(message = "必填")
    @Schema(description = "所属职务")
    private String applyPost;

    @Schema(description = "总体评价")
    private String overalEvaluat;

    @NotBlank(message = "必填")
    @Schema(description = "创建人")
    private String applyUser;

    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;

    @NotBlank(message = "必填")
    @Schema(description = "所属部门")
    private String applyDept;

    @Schema(description = "工作事项")
    private String nPWorkMatter;

    @Schema(description = "完成时间")
    private Long planEndTime;

    @NotNull(message = "必填")
    @Schema(description = "创建日期")
    private Long applyDate;

    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
