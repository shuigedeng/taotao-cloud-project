package com.taotao.cloud.workflow.api.common.model.form.paydistribution;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 薪酬发放
 *
 */
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
