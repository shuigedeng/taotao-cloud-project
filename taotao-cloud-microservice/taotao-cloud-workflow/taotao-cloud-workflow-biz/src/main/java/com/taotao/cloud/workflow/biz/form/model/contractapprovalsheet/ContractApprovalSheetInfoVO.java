package com.taotao.cloud.workflow.biz.form.model.contractapprovalsheet;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 合同申请单表
 *
 */
@Data
public class ContractApprovalSheetInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "支出总额")
    private BigDecimal totalExpenditure;
    @Schema(description = "结束时间")
    private Long endContractDate;
    @Schema(description = "预算批付")
    private String budgetaryApproval;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "乙方")
    private String secondParty;
    @Schema(description = "合同类型")
    private String contractType;
    @Schema(description = "所属部门")
    private String leadDepartment;
    @Schema(description = "收入金额")
    private BigDecimal incomeAmount;
    @Schema(description = "内容简要")
    private String contractContent;
    @Schema(description = "签订地区")
    private String signArea;
    @Schema(description = "合同期限")
    private String contractPeriod;
    @Schema(description = "申请人")
    private String applyUser;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "编码支出")
    private String contractId;
    @Schema(description = "签署方(甲方)")
    private String firstParty;
    @Schema(description = "合作负责人")
    private String personCharge;
    @Schema(description = "付款方式")
    private String paymentMethod;
    @Schema(description = "开始时间")
    private Long startContractDate;
    @Schema(description = "合同号")
    private String contractNum;
    @Schema(description = "合同名称")
    private String contractName;
    @Schema(description = "申请日期")
    private Long applyDate;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
}
