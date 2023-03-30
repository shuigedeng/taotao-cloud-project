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

package com.taotao.cloud.workflow.biz.common.model.form.paydistribution;

import java.math.BigDecimal;
import lombok.Data;

/** 薪酬发放 */
@Data
public class PayDistributionInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "员工保险")
    private BigDecimal insurance;

    @Schema(description = "出勤天数")
    private String actualAttendance;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "基本薪资")
    private BigDecimal baseSalary;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "员工津贴")
    private BigDecimal allowance;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "发放单位")
    private String issuingUnit;

    @Schema(description = "员工绩效")
    private BigDecimal performance;

    @Schema(description = "所属月份")
    private String month;

    @Schema(description = "所得税")
    private BigDecimal incomeTax;

    @Schema(description = "应发工资")
    private BigDecimal grossPay;

    @Schema(description = "实发工资")
    private BigDecimal payroll;

    @Schema(description = "员工职位")
    private String position;

    @Schema(description = "员工部门")
    private String department;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "加班费用")
    private BigDecimal overtimePay;
}
