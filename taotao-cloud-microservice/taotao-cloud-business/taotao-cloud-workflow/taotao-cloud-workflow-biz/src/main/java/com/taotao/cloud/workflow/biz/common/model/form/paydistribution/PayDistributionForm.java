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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 薪酬发放 */
@Data
public class PayDistributionForm {
    @Schema(description = "员工保险")
    private BigDecimal insurance;

    @Schema(description = "出勤天数")
    private String actualAttendance;

    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "基本薪资")
    private BigDecimal baseSalary;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "员工津贴")
    private BigDecimal allowance;

    @NotBlank(message = "必填")
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

    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "加班费用")
    private BigDecimal overtimePay;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
