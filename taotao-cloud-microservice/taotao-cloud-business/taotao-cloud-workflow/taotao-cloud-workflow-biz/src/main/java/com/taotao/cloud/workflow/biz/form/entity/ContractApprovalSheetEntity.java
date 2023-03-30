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

/** 合同申请单表 */
@Data
@TableName("wform_contractapprovalsheet")
public class ContractApprovalSheetEntity {
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

    /** 申请日期 */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /** 编码支出 */
    @TableField("F_CONTRACTID")
    private String contractId;

    /** 合同号 */
    @TableField("F_CONTRACTNUM")
    private String contractNum;

    /** 签署方(甲方) */
    @TableField("F_FIRSTPARTY")
    private String firstParty;

    /** 乙方 */
    @TableField("F_SECONDPARTY")
    private String secondParty;

    /** 合同名称 */
    @TableField("F_CONTRACTNAME")
    private String contractName;

    /** 合同类型 */
    @TableField("F_CONTRACTTYPE")
    private String contractType;

    /** 合作负责人 */
    @TableField("F_PERSONCHARGE")
    private String personCharge;

    /** 所属部门 */
    @TableField("F_LEADDEPARTMENT")
    private String leadDepartment;

    /** 签订地区 */
    @TableField("F_SIGNAREA")
    private String signArea;

    /** 收入金额 */
    @TableField("F_INCOMEAMOUNT")
    private BigDecimal incomeAmount;

    /** 支出总额 */
    @TableField("F_TOTALEXPENDITURE")
    private BigDecimal totalExpenditure;

    /** 合同期限 */
    @TableField("F_CONTRACTPERIOD")
    private String contractPeriod;

    /** 付款方式 */
    @TableField("F_PAYMENTMETHOD")
    private String paymentMethod;

    /** 预算批付 */
    @TableField("F_BUDGETARYAPPROVAL")
    private String budgetaryApproval;

    /** 开始时间 */
    @TableField("F_STARTCONTRACTDATE")
    private Date startContractDate;

    /** 结束时间 */
    @TableField("F_ENDCONTRACTDATE")
    private Date endContractDate;

    /** 相关附件 */
    @TableField("F_FILEJSON")
    private String fileJson;

    /** 内容简要 */
    @TableField("F_CONTRACTCONTENT")
    private String contractContent;
}
