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

/** 借支单 */
@Data
@TableName("wform_debitbill")
public class DebitBillEntity {
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

    /** 工作部门 */
    @TableField("F_DEPARTMENTAL")
    private String departmental;

    /** 申请日期 */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /** 员工姓名 */
    @TableField("F_STAFFNAME")
    private String staffName;

    /** 员工职务 */
    @TableField("F_STAFFPOST")
    private String staffPost;

    /** 员工编码 */
    @TableField("F_STAFFID")
    private String staffId;

    /** 借款方式 */
    @TableField("F_LOANMODE")
    private String loanMode;

    /** 借支金额 */
    @TableField("F_AMOUNTDEBIT")
    private BigDecimal amountDebit;

    /** 转账账户 */
    @TableField("F_TRANSFERACCOUNT")
    private String transferAccount;

    /** 还款票据 */
    @TableField("F_REPAYMENTBILL")
    private String repaymentBill;

    /** 还款日期 */
    @TableField("F_TEACHINGDATE")
    private Date teachingDate;

    /** 支付方式 */
    @TableField("F_PAYMENTMETHOD")
    private String paymentMethod;

    /** 借款原因 */
    @TableField("F_REASON")
    private String reason;
}
