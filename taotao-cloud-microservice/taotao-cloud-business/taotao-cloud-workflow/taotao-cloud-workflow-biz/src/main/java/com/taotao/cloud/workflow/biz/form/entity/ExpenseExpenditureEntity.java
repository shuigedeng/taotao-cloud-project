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

/** 费用支出单 */
@Data
@TableName("wform_expenseexpenditure")
public class ExpenseExpenditureEntity {
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

    /** 申请人员 */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /** 所在部门 */
    @TableField("F_DEPARTMENT")
    private String department;

    /** 申请日期 */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /** 合同编码 */
    @TableField("F_CONTRACTNUM")
    private String contractNum;

    /** 非合同支出 */
    @TableField("F_NONCONTRACT")
    private String nonContract;

    /** 开户银行 */
    @TableField("F_ACCOUNTOPENINGBANK")
    private String accountOpeningBank;

    /** 银行账号 */
    @TableField("F_BANKACCOUNT")
    private String bankAccount;

    /** 开户姓名 */
    @TableField("F_OPENACCOUNT")
    private String openAccount;

    /** 合计费用 */
    @TableField("F_TOTAL")
    private BigDecimal total;

    /** 支付方式 */
    @TableField("F_PAYMENTMETHOD")
    private String paymentMethod;

    /** 支付金额 */
    @TableField("F_AMOUNTPAYMENT")
    private BigDecimal amountPayment;

    /** 备注 */
    @TableField("F_DESCRIPTION")
    private String description;
}
