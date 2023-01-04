package com.taotao.cloud.workflow.api.common.model.form.documentapproval;


import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文件签批意见表
 *
 */
@Data
public class DocumentApprovalForm {
    @Schema(description = "拟稿人")
    private String draftedPerson;
    @Schema(description = "相关附件")
    private String fileJson;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "文件名称")
    private String fileName;
    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "发文单位")
    private String serviceUnit;
    @Schema(description = "修改意见")
    private String modifyOpinion;
    @NotNull(message = "必填")
    @Schema(description = "收文日期")
    private Long receiptDate;
    @Schema(description = "文件拟办")
    private String fillPreparation;
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "文件编码")
    private String fillNum;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
