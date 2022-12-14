package com.taotao.cloud.workflow.biz.form.model.expenseexpenditure;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 费用支出单
 *
 */
@Data
public class ExpenseExpenditureForm {
    @Schema(description = "银行账号")
    private String bankAccount;
    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "支付金额")
    private BigDecimal amountPayment;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "非合同支出")
    private String nonContract;
    @NotBlank(message = "必填")
    @Schema(description = "申请人员")
    private String applyUser;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "合计费用")
    private BigDecimal total;
    @Schema(description = "开户银行")
    private String accountOpeningBank;
    @NotBlank(message = "必填")
    @Schema(description = "支付方式")
    private String paymentMethod;
    @Schema(description = "合同编码")
    private String contractNum;
    @Schema(description = "开户姓名")
    private String openAccount;
    @NotNull(message = "必填")
    @Schema(description = "申请日期")
    private Long applyDate;
    @NotBlank(message = "必填")
    @Schema(description = "申请部门")
    private String department;
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;

}
