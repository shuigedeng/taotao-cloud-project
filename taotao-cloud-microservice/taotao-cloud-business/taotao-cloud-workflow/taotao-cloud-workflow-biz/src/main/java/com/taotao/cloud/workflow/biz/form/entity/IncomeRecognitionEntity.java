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

/** 收入确认分析表 */
@Data
@TableName("wform_incomerecognition")
public class IncomeRecognitionEntity {
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

    /** 结算月份 */
    @TableField("F_SETTLEMENTMONTH")
    private String settlementMonth;

    /** 客户名称 */
    @TableField("F_CUSTOMERNAME")
    private String customerName;

    /** 合同编码 */
    @TableField("F_CONTRACTNUM")
    private String contractNum;

    /** 合同金额 */
    @TableField("F_TOTALAMOUNT")
    private BigDecimal totalAmount;

    /** 到款银行 */
    @TableField("F_MONEYBANK")
    private String moneyBank;

    /** 到款金额 */
    @TableField("F_ACTUALAMOUNT")
    private BigDecimal actualAmount;

    /** 到款日期 */
    @TableField("F_PAYMENTDATE")
    private Date paymentDate;

    /** 联系人姓名 */
    @TableField("F_CONTACTNAME")
    private String contactName;

    /** 联系电话 */
    @TableField("F_CONTACPHONE")
    private String contacPhone;

    /** 联系QQ */
    @TableField("F_CONTACTQQ")
    private String contactQQ;

    /** 未付金额 */
    @TableField("F_UNPAIDAMOUNT")
    private BigDecimal unpaidAmount;

    /** 已付金额 */
    @TableField("F_AMOUNTPAID")
    private BigDecimal amountPaid;

    /** 备注 */
    @TableField("F_DESCRIPTION")
    private String description;
}
