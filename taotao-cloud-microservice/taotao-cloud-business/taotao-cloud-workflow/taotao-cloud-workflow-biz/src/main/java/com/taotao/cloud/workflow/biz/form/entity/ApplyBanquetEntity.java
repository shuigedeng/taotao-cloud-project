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

/** 宴请申请 */
@Data
@TableName("wform_applybanquet")
public class ApplyBanquetEntity {
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

    /** 申请人员 */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /** 所属职务 */
    @TableField("F_POSITION")
    private String position;

    /** 申请日期 */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /** 宴请人数 */
    @TableField("F_BANQUETNUM")
    private String banquetNum;

    /** 宴请人员 */
    @TableField("F_BANQUETPEOPLE")
    private String banquetPeople;

    /** 人员总数 */
    @TableField("F_TOTAL")
    private String total;

    /** 宴请地点 */
    @TableField("F_PLACE")
    private String place;

    /** 预计费用 */
    @TableField("F_EXPECTEDCOST")
    private BigDecimal expectedCost;

    /** 备注 */
    @TableField("F_DESCRIPTION")
    private String description;
}
