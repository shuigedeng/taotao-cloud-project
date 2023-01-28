package com.taotao.cloud.workflow.biz.common.model.form.receiptprocessing;


import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 收文处理表
 */
@Data
public class ReceiptProcessingForm {
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "来文单位")
    private String communicationUnit;
    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "文件标题")
    private String fileTitle;
    @NotNull(message = "收文日期不能为空")
    @Schema(description = "收文日期")
    private Long  receiptDate;
    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "来文字号")
    private String letterNum;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
