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
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/** 车辆申请 */
@Data
@TableName("wform_vehicleapply")
public class VehicleApplyEntity {
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

    /** 流程单据 */
    @TableField("F_BILLNO")
    private String billNo;

    /** 用车人 */
    @TableField("F_CARMAN")
    private String carMan;

    /** 所在部门 */
    @TableField("F_DEPARTMENT")
    private String department;

    /** 车牌号 */
    @TableField("F_PLATENUM")
    private String plateNum;

    /** 开始日期 */
    @TableField("F_STARTDATE")
    private Date startDate;

    /** 结束日期 */
    @TableField("F_ENDDATE")
    private Date endDate;

    /** 目的地 */
    @TableField("F_DESTINATION")
    private String destination;

    /** 路费金额 */
    @TableField("F_ROADFEE")
    private BigDecimal roadFee;

    /** 公里数 */
    @TableField("F_KILOMETRENUM")
    private String kilometreNum;

    /** 随行人数 */
    @TableField("F_ENTOURAGE")
    private String entourage;

    /** 备注 */
    @TableField("F_DESCRIPTION")
    private String description;
}
