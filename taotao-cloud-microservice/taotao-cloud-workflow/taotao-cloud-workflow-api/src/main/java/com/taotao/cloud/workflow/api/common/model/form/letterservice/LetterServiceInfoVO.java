package com.taotao.cloud.workflow.api.common.model.form.letterservice;


import lombok.Data;

/**
 * 发文单
 *
 */
@Data
public class LetterServiceInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "发文字号")
    private String issuedNum;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
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
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "份数")
    private String shareNum;

}
