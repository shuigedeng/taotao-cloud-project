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

package com.taotao.cloud.workflow.biz.common.model.form.staffovertime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 员工加班申请表 */
@Data
public class StaffOvertimeForm {
    @NotBlank(message = "申请人不能为空")
    @Schema(description = "申请人")
    private String applyUser;

    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;

    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "总计时间")
    private String totleTime;

    @Schema(description = "加班事由")
    private String cause;

    @NotNull(message = "开始时间不能为空")
    @Schema(description = "开始时间")
    private Long startTime;

    @NotNull(message = "结束时间不能为空")
    @Schema(description = "结束时间")
    private Long endTime;

    @NotNull(message = "申请日期不能为空")
    @Schema(description = "申请日期")
    private Long applyDate;

    @NotBlank(message = "申请部门不能为空")
    @Schema(description = "申请部门")
    private String department;

    @NotBlank(message = "记入类别不能为空")
    @Schema(description = "记入类别")
    private String category;

    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
