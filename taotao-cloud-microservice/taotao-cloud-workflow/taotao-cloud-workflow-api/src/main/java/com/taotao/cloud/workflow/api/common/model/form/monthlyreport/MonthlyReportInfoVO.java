package com.taotao.cloud.workflow.api.common.model.form.monthlyreport;


import lombok.Data;

/**
 * 月工作总结
 *
 */
@Data
public class MonthlyReportInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "次月日期")
    private Long  nPFinishTime;
    @Schema(description = "次月目标")
    private String nFinishMethod;
    @Schema(description = "所属职务")
    private String applyPost;
    @Schema(description = "总体评价")
    private String overalEvaluat;
    @Schema(description = "创建人")
    private String applyUser;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "所属部门")
    private String applyDept;
    @Schema(description = "工作事项")
    private String nPWorkMatter;
    @Schema(description = "完成时间")
    private Long  planEndTime;
    @Schema(description = "创建日期")
    private Long  applyDate;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
}
