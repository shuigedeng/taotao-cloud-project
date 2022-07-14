package com.taotao.cloud.workflow.biz.form.model.outgoingapply;


import lombok.Data;

/**
 * 外出申请单
 *
 */
@Data
public class OutgoingApplyInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "目的地")
    private String destination;
    @Schema(description = "申请人")
    private String applyUser;
    @Schema(description = "发文标题")
    private String flowTitle;
    @Schema(description = "外出事由")
    private String outgoingCause;
    @Schema(description = "开始时间")
    private Long startTime;
    @Schema(description = "结束时间")
    private Long endTime;
    @Schema(description = "申请时间")
    private Long applyDate;
    @Schema(description = "所在部门")
    private String department;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "外出总计")
    private String outgoingTotle;
}
