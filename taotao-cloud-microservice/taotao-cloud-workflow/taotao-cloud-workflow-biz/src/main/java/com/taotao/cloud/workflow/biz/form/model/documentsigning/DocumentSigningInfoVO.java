package com.taotao.cloud.workflow.biz.form.model.documentsigning;


import lombok.Data;

/**
 * 文件签阅表
 *
 */
@Data
public class DocumentSigningInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "文件名称")
    private String fileName;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "签阅人")
    private String reader;
    @Schema(description = "文件拟办")
    private String fillPreparation;
    @Schema(description = "文件内容")
    private String documentContent;
    @Schema(description = "签阅时间")
    private Long  checkDate;
    @Schema(description = "文件编码")
    private String fillNum;
    @Schema(description = "拟稿人")
    private String draftedPerson;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "发稿日期")
    private Long  publicationDate;
    @Schema(description = "建议栏")
    private String adviceColumn;
}
