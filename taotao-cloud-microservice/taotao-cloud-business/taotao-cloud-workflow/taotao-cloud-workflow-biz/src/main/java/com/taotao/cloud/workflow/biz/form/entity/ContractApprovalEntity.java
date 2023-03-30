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

/** 合同审批 */
@Data
@TableName("wform_contractapproval")
public class ContractApprovalEntity {
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

    /** 甲方单位 */
    @TableField("F_FIRSTPARTYUNIT")
    private String firstPartyUnit;

    /** 乙方单位 */
    @TableField("F_SECONDPARTYUNIT")
    private String secondPartyUnit;

    /** 甲方负责人 */
    @TableField("F_FIRSTPARTYPERSON")
    private String firstPartyPerson;

    /** 乙方负责人 */
    @TableField("F_SECONDPARTYPERSON")
    private String secondPartyPerson;

    /** 甲方联系方式 */
    @TableField("F_FIRSTPARTYCONTACT")
    private String firstPartyContact;

    /** 乙方联系方式 */
    @TableField("F_SECONDPARTYCONTACT")
    private String secondPartyContact;

    /** 合同名称 */
    @TableField("F_CONTRACTNAME")
    private String contractName;

    /** 合同分类 */
    @TableField("F_CONTRACTCLASS")
    private String contractClass;

    /** 合同类型 */
    @TableField("F_CONTRACTTYPE")
    private String contractType;

    /** 合同编码 */
    @TableField("F_CONTRACTID")
    private String contractId;

    /** 业务人员 */
    @TableField("F_BUSINESSPERSON")
    private String businessPerson;

    /** 签约时间 */
    @TableField("F_SIGNINGDATE")
    private Date signingDate;

    /** 开始时间 */
    @TableField("F_STARTDATE")
    private Date startDate;

    /** 结束时间 */
    @TableField("F_ENDDATE")
    private Date endDate;

    /** 收入金额 */
    @TableField("F_INCOMEAMOUNT")
    private BigDecimal incomeAmount;

    /** 填写人员 */
    @TableField("F_INPUTPERSON")
    private String inputPerson;

    /** 相关附件 */
    @TableField("F_FILEJSON")
    private String fileJson;

    /** 主要内容 */
    @TableField("F_PRIMARYCOVERAGE")
    private String primaryCoverage;

    /** 备注 */
    @TableField("F_DESCRIPTION")
    private String description;
}
