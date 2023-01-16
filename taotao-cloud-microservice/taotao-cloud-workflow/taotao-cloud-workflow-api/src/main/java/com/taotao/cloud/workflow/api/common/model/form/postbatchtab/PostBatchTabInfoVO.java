package com.taotao.cloud.workflow.api.common.model.form.postbatchtab;


import lombok.Data;

/**
 * 发文呈批表
 *
 */
@Data
public class PostBatchTabInfoVO {
    @Schema(description = "主键id")
    private String id;
    private String draftedPerson;
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
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
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "份数")
    private String shareNum;
}
