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

/** 差旅报销申请表 */
@Data
@TableName("wform_travelreimbursement")
public class TravelReimbursementEntity {
    /** 主键 */
    @TableId("F_ID")
    private String id;

    /** 流程主键 */
    @TableField("F_FLOWID")
    private String flowId;

    /** 流程标题 */
    @TableField("F_FLOWTITLE")
    private String flowTitle;

    /** 流程等级 */
    @TableField("F_FLOWURGENT")
    private Integer flowUrgent;

    /** 流程单据 */
    @TableField("F_BILLNO")
    private String billNo;

    /** 申请人 */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /** 申请部门 */
    @TableField("F_DEPARTMENTAL")
    private String departmental;

    /** 票据数 */
    @TableField("F_BILLSNUM")
    private String billsNum;

    /** 出差任务 */
    @TableField("F_BUSINESSMISSION")
    private String businessMission;

    /** 出发日期 */
    @TableField("F_SETOUTDATE")
    private Date setOutDate;

    /** 回归日期 */
    @TableField("F_RETURNDATE")
    private Date returnDate;

    /** 到达地 */
    @TableField("F_DESTINATION")
    private String destination;

    /** 机票费 */
    @TableField("F_PLANETICKET")
    private BigDecimal planeTicket;

    /** 车船费 */
    @TableField("F_FARE")
    private BigDecimal fare;

    /** 住宿费用 */
    @TableField("F_GETACCOMMODATION")
    private BigDecimal getAccommodation;

    /** 出差补助 */
    @TableField("F_TRAVELALLOWANCE")
    private BigDecimal travelAllowance;

    /** 其他费用 */
    @TableField("F_OTHER")
    private BigDecimal other;

    /** 合计 */
    @TableField("F_TOTAL")
    private BigDecimal total;

    /** 报销金额 */
    @TableField("F_REIMBURSEMENTAMOUNT")
    private BigDecimal reimbursementAmount;

    /** 借款金额 */
    @TableField("F_LOANAMOUNT")
    private BigDecimal loanAmount;

    /** 补找金额 */
    @TableField("F_SUMOFMONEY")
    private BigDecimal sumOfMoney;

    @TableField("F_TRAVELERUSER")
    private String travelerUser;

    /** 车辆里程 */
    @TableField("F_VEHICLEMILEAGE")
    private BigDecimal vehicleMileage;

    /** 过路费 */
    @TableField("F_ROADFEE")
    private BigDecimal roadFee;

    /** 停车费 */
    @TableField("F_PARKINGRATE")
    private BigDecimal parkingRate;

    /** 餐补费用 */
    @TableField("F_MEALALLOWANCE")
    private BigDecimal mealAllowance;

    /** 故障报修费 */
    @TableField("F_BREAKDOWNFEE")
    private BigDecimal breakdownFee;

    /** 报销编码 */
    @TableField("F_REIMBURSEMENTID")
    private String reimbursementId;

    /** 轨道交通费 */
    @TableField("F_RAILTRANSIT")
    private BigDecimal railTransit;

    /** 申请时间 */
    @TableField("F_APPLYDATE")
    private Date applyDate;
}
