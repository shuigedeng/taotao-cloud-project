package com.taotao.cloud.workflow.api.common.model.form.receiptsign;


import lombok.Data;

/**
 * 收文签呈单
 */
@Data
public class ReceiptSignInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "收文标题")
    private String receiptTitle;
    @Schema(description = "收文日期")
    private Long receiptDate;
    @Schema(description = "收文部门")
    private String department;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "收文简述")
    private String receiptPaper;
    @Schema(description = "收文人")
    private String collector;
}
