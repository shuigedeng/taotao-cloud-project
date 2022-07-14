package com.taotao.cloud.workflow.biz.form.model.postbatchtab;


import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发文呈批表
 *
 */
@Data
public class PostBatchTabForm {

    private String draftedPerson;
    @Schema(description = "相关附件")
    private String fileJson;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @NotNull(message = "必填")
    @Schema(description = "发文日期")
    private Long writingDate;
    @Schema(description = "文件标题")
    private String fileTitle;
    @Schema(description = "发往单位")
    private String sendUnit;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "发文编码")
    private String writingNum;
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "份数")
    private String shareNum;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
