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

import lombok.Data;

/** 月工作总结 */
@Data
public class MonthlyReportInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "相关附件")
    private String fileJson;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "次月日期")
    private Long nPFinishTime;

    @Schema(description = "次月目标")
    private String nFinishMethod;

    @Schema(description = "所属职务")
    private String applyPost;

    @Schema(description = "总体评价")
    private String overalEvaluat;

    @Schema(description = "创建人")
    private String applyUser;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "所属部门")
    private String applyDept;

    @Schema(description = "工作事项")
    private String nPWorkMatter;

    @Schema(description = "完成时间")
    private Long planEndTime;

    @Schema(description = "创建日期")
    private Long applyDate;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;
}
