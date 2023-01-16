package com.taotao.cloud.workflow.api.common.model.form.expenseexpenditure;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 费用支出单
 *
 */
@Data
public class ExpenseExpenditureInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "银行账号")
    private String bankAccount;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "支付金额")
    private BigDecimal amountPayment;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "非合同支出")
    private String nonContract;
    @Schema(description = "申请人员")
    private String applyUser;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "合计费用")
    private BigDecimal total;
    @Schema(description = "开户银行")
    private String accountOpeningBank;
    @Schema(description = "支付方式")
    private String paymentMethod;
    @Schema(description = "合同编码")
    private String contractNum;
    @Schema(description = "开户姓名")
    private String openAccount;
    @Schema(description = "申请日期")
    private Long  applyDate;
    @Schema(description = "所在部门")
    private String department;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
}
