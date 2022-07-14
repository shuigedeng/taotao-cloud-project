package com.taotao.cloud.workflow.biz.form.model.paydistribution;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 薪酬发放
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class PayDistributionInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "员工保险")
    private BigDecimal insurance;
    @ApiModelProperty(value = "出勤天数")
    private String actualAttendance;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "基本薪资")
    private BigDecimal baseSalary;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "员工津贴")
    private BigDecimal allowance;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "发放单位")
    private String issuingUnit;
    @ApiModelProperty(value = "员工绩效")
    private BigDecimal performance;
    @ApiModelProperty(value = "所属月份")
    private String month;
    @ApiModelProperty(value = "所得税")
    private BigDecimal incomeTax;
    @ApiModelProperty(value = "应发工资")
    private BigDecimal grossPay;
    @ApiModelProperty(value = "实发工资")
    private BigDecimal payroll;
    @ApiModelProperty(value = "员工职位")
    private String position;
    @ApiModelProperty(value = "员工部门")
    private String department;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "加班费用")
    private BigDecimal overtimePay;
}
