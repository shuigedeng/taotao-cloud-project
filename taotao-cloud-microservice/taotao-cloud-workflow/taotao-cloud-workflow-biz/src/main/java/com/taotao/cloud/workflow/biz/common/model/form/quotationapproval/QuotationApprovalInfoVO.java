package com.taotao.cloud.workflow.biz.common.model.form.quotationapproval;


import lombok.Data;

/**
 * 报价审批表
 */
@Data
public class QuotationApprovalInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "合作人名")
    private String partnerName;
    @Schema(description = "填表日期")
    private Long  writeDate;
    @Schema(description = "情况描述")
    private String custSituation;
    @Schema(description = "填报人")
    private String writer;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "类型")
    private String quotationType;
    @Schema(description = "客户名称")
    private String customerName;
    @Schema(description = "模板参考")
    private String standardFile;
}
