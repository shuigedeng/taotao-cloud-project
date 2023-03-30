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

package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/** 流程表单【请假申请】 */
@Data
@TableName("wform_leaveapply")
public class LeaveApplyEntity {
    /** 主键 */
    @TableId("F_ID")
    private String id;

    /** 流程主键 */
    @TableField("F_FLOWID")
    private String flowId;

    /** 流程标题 */
    @TableField("F_FLOWTITLE")
    private String flowTitle;

    /** 紧急程度 */
    @TableField("F_FLOWURGENT")
    private Integer flowUrgent;

    /** 单据编码 */
    @TableField("F_BILLNO")
    private String billNo;

    /** 申请人员 */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /** 申请日期 */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /** 申请部门 */
    @TableField("F_APPLYDEPT")
    private String applyDept;

    /** 申请职位 */
    @TableField("F_APPLYPOST")
    private String applyPost;

    /** 请假类别 */
    @TableField("F_LEAVETYPE")
    private String leaveType;

    /** 请假原因 */
    @TableField("F_LEAVEREASON")
    private String leaveReason;

    /** 请假时间 */
    @TableField("F_LEAVESTARTTIME")
    private Date leaveStartTime;

    /** 结束时间 */
    @TableField("F_LEAVEENDTIME")
    private Date leaveEndTime;

    /** 请假天数 */
    @TableField("F_LEAVEDAYCOUNT")
    private String leaveDayCount;

    /** 请假小时 */
    @TableField("F_LEAVEHOUR")
    private String leaveHour;

    /** 相关附件 */
    @TableField("F_FILEJSON")
    private String fileJson;

    /** 备注 */
    @TableField("F_DESCRIPTION")
    private String description;
}
