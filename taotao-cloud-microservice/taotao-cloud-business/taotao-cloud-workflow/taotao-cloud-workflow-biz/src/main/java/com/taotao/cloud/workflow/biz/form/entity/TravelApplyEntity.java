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

/** 出差预支申请单 */
@Data
@TableName("wform_travelapply")
public class TravelApplyEntity {
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

    /** 出差人 */
    @TableField("F_TRAVELMAN")
    private String travelMan;

    /** 申请日期 */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /** 所属部门 */
    @TableField("F_DEPARTMENTAL")
    private String departmental;

    /** 所属职务 */
    @TableField("F_POSITION")
    private String position;

    /** 开始日期 */
    @TableField("F_STARTDATE")
    private Date startDate;

    /** 结束日期 */
    @TableField("F_ENDDATE")
    private Date endDate;

    /** 起始地点 */
    @TableField("F_STARTPLACE")
    private String startPlace;

    /** 目的地 */
    @TableField("F_DESTINATION")
    private String destination;

    /** 预支旅费 */
    @TableField("F_PREPAIDTRAVEL")
    private BigDecimal prepaidTravel;

    /** 备注 */
    @TableField("F_DESCRIPTION")
    private String description;
}
