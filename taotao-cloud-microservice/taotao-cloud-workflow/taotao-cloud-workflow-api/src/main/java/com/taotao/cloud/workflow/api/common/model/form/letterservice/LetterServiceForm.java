package com.taotao.cloud.workflow.api.common.model.form.letterservice;


import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发文单
 */
@Data
public class LetterServiceForm {
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "发文字号")
    private String issuedNum;
    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @NotNull(message = "发文日期不能为空")
    @Schema(description = "发文日期")
    private Long  writingDate;
    @Schema(description = "主办单位")
    private String hostUnit;
    @Schema(description = "抄送")
    private String copy;
    @Schema(description = "发文标题")
    private String title;
    @Schema(description = "主送")
    private String mainDelivery;
    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "份数")
    private String shareNum;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
