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

import lombok.Data;

/** 请假申请 */
@Data
public class LeaveApplyInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "相关附件")
    private String fileJson;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "请假天数")
    private String leaveDayCount;

    @Schema(description = "请假小时")
    private String leaveHour;

    @Schema(description = "请假时间")
    private Long leaveStartTime;

    @Schema(description = "申请职位")
    private String applyPost;

    @Schema(description = "申请人员")
    private String applyUser;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "申请部门")
    private String applyDept;

    @Schema(description = "请假类别")
    private String leaveType;

    @Schema(description = "请假原因")
    private String leaveReason;

    @Schema(description = "申请日期")
    private Long applyDate;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "结束时间")
    private Long leaveEndTime;
}
