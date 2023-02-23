package com.taotao.cloud.workflow.biz.common.model.form.paydistribution;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 薪酬发放
 */
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
