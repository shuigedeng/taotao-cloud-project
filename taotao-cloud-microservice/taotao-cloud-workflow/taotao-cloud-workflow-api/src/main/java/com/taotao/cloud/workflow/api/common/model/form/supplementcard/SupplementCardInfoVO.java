package com.taotao.cloud.workflow.api.common.model.form.supplementcard;


import lombok.Data;

/**
 * 补卡申请
 */
@Data
public class SupplementCardInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "员工姓名")
    private String fullName;
    @Schema(description = "流程主键")
    private String description;
    @Schema(description = "证明人")
    private String witness;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "开始时间")
    private Long  startTime;
    @Schema(description = "所在职务")
    private String position;
    @Schema(description = "补卡次数")
    private String supplementNum;
    @Schema(description = "结束时间")
    private Long  endTime;
    @Schema(description = "申请日期")
    private Long  applyDate;
    @Schema(description = "所在部门")
    private String department;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;

}
