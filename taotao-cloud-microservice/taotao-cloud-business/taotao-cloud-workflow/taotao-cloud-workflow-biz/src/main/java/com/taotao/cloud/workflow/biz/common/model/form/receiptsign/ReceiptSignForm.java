package com.taotao.cloud.workflow.biz.common.model.form.receiptsign;


import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 收文签呈单
 */
@Data
public class ReceiptSignForm {
    @Schema(description = "相关附件")
    private String fileJson;
    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "收文标题")
    private String receiptTitle;
    @NotNull(message = "收文日期不能为空")
    @Schema(description = "收文日期")
    private Long receiptDate;
    @Schema(description = "收文部门")
    private String department;
    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "收文简述")
    private String receiptPaper;
    @Schema(description = "收文人")
    private String collector;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;

}
