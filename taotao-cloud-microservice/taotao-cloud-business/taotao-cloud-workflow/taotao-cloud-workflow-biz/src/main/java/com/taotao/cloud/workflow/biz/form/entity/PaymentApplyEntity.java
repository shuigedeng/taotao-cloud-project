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

/** 付款申请单 */
@Data
@TableName("wform_paymentapply")
public class PaymentApplyEntity {
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

    /** 申请人 */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /** 申请部门 */
    @TableField("F_DEPARTMENTAL")
    private String departmental;

    /** 申请日期 */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /** 用途名称 */
    @TableField("F_PURPOSENAME")
    private String purposeName;

    /** 项目类别 */
    @TableField("F_PROJECTCATEGORY")
    private String projectCategory;

    /** 项目负责人 */
    @TableField("F_PROJECTLEADER")
    private String projectLeader;

    /** 开户银行 */
    @TableField("F_OPENINGBANK")
    private String openingBank;

    /** 收款账号 */
    @TableField("F_BENEFICIARYACCOUNT")
    private String beneficiaryAccount;

    /** 联系方式 */
    @TableField("F_RECEIVABLECONTACT")
    private String receivableContact;

    /** 付款单位 */
    @TableField("F_PAYMENTUNIT")
    private String paymentUnit;

    /** 申请金额 */
    @TableField("F_APPLYAMOUNT")
    private BigDecimal applyAmount;

    /** 结算方式 */
    @TableField("F_SETTLEMENTMETHOD")
    private String settlementMethod;

    /** 付款类型 */
    @TableField("F_PAYMENTTYPE")
    private String paymentType;

    /** 付款金额 */
    @TableField("F_AMOUNTPAID")
    private BigDecimal amountPaid;

    /** 备注 */
    @TableField("F_DESCRIPTION")
    private String description;
}
