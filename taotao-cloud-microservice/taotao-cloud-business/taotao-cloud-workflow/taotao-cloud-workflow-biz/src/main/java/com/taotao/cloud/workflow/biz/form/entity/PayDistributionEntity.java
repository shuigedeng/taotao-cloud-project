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
import lombok.Data;

/** 薪酬发放 */
@Data
@TableName("wform_paydistribution")
public class PayDistributionEntity {
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

    /** 所属月份 */
    @TableField("F_MONTH")
    private String month;

    /** 发放单位 */
    @TableField("F_ISSUINGUNIT")
    private String issuingUnit;

    /** 员工部门 */
    @TableField("F_DEPARTMENT")
    private String department;

    /** 员工职位 */
    @TableField("F_POSITION")
    private String position;

    /** 基本薪资 */
    @TableField("F_BASESALARY")
    private BigDecimal baseSalary;

    /** 出勤天数 */
    @TableField("F_ACTUALATTENDANCE")
    private String actualAttendance;

    /** 员工津贴 */
    @TableField("F_ALLOWANCE")
    private BigDecimal allowance;

    /** 所得税 */
    @TableField("F_INCOMETAX")
    private BigDecimal incomeTax;

    /** 员工保险 */
    @TableField("F_INSURANCE")
    private BigDecimal insurance;

    /** 员工绩效 */
    @TableField("F_PERFORMANCE")
    private BigDecimal performance;

    /** 加班费用 */
    @TableField("F_OVERTIMEPAY")
    private BigDecimal overtimePay;

    /** 应发工资 */
    @TableField("F_GROSSPAY")
    private BigDecimal grossPay;

    /** 实发工资 */
    @TableField("F_PAYROLL")
    private BigDecimal payroll;

    /** 备注 */
    @TableField("F_DESCRIPTION")
    private String description;
}
