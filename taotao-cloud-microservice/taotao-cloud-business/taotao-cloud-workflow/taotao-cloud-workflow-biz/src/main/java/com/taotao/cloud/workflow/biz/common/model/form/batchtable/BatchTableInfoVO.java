package com.taotao.cloud.workflow.biz.common.model.form.batchtable;


import lombok.Data;

/**
 * 行文呈批表
 *
 * @date 2021/3/15 8:46
 */
@Data
public class BatchTableInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
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
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "文件标题")
    private String fileTitle;
    @Schema(description = "发往单位")
    private String sendUnit;
    @Schema(description = "打字")
    private String typing;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
}
