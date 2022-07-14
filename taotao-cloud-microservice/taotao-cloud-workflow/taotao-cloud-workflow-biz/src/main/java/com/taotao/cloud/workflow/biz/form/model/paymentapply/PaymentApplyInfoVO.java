package com.taotao.cloud.workflow.biz.form.model.paymentapply;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 付款申请单
 *
 */
@Data
public class PaymentApplyInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "用途名称")
    private String purposeName;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "结算方式")
    private String settlementMethod;
    @Schema(description = "开户银行")
    private String openingBank;
    @Schema(description = "申请金额")
    private BigDecimal applyAmount;
    @Schema(description = "付款类型")
    private String paymentType;
    @Schema(description = "申请人")
    private String applyUser;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "联系方式")
    private String receivableContact;
    @Schema(description = "付款金额")
    private BigDecimal amountPaid;
    @Schema(description = "项目类别")
    private String projectCategory;
    @Schema(description = "申请部门")
    private String departmental;
    @Schema(description = "项目负责人")
    private String projectLeader;
    @Schema(description = "收款账号")
    private String beneficiaryAccount;
    @Schema(description = "付款单位")
    private String paymentUnit;
    @Schema(description = "申请时间")
    private Long applyDate;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
}
