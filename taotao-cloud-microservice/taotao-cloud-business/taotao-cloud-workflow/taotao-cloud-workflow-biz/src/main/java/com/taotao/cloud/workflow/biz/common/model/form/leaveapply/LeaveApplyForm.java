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

package com.taotao.cloud.workflow.biz.common.model.form.leaveapply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 请假申请 */
@Data
public class LeaveApplyForm {
    @Schema(description = "相关附件")
    private String fileJson;

    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @NotBlank(message = "必填")
    @Schema(description = "请假天数")
    private String leaveDayCount;

    @NotBlank(message = "必填")
    @Schema(description = "请假小时")
    private String leaveHour;

    @NotNull(message = "请假时间不能为空")
    @Schema(description = "请假时间")
    private Long leaveStartTime;

    @NotBlank(message = "必填")
    @Schema(description = "申请职位")
    private String applyPost;

    @NotBlank(message = "必填")
    @Schema(description = "申请人员")
    private String applyUser;

    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;

    @NotBlank(message = "必填")
    @Schema(description = "申请部门")
    private String applyDept;

    @NotBlank(message = "必填")
    @Schema(description = "请假类别")
    private String leaveType;

    @NotBlank(message = "必填")
    @Schema(description = "请假原因")
    private String leaveReason;

    @NotNull(message = "申请日期不能为空")
    @Schema(description = "申请日期")
    private Long applyDate;

    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;

    @NotNull(message = "结束时间不能为空")
    @Schema(description = "结束时间")
    private Long leaveEndTime;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
