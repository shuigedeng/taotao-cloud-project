package com.taotao.cloud.workflow.biz.common.model.form.batchtable;


import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 行文呈批表
 */
@Data
public class BatchTableForm {
    @Schema(description = "相关附件")
    private String fileJson;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @NotNull(message = "发文日期不能为空")
    @Schema(description = "发文日期")
    private Long writingDate;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "份数")
    private String shareNum;
    @Schema(description = "文件编码")
    private String fillNum;
    @Schema(description = "主办单位")
    private String draftedPerson;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "文件标题")
    private String fileTitle;
    @Schema(description = "发往单位")
    private String sendUnit;
    @Schema(description = "打字")
    private String typing;
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
