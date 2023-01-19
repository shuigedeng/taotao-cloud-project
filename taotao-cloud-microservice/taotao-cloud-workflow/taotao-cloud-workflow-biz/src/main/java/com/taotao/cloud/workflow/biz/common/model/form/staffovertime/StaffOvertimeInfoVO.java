package com.taotao.cloud.workflow.biz.common.model.form.staffovertime;


import lombok.Data;

/**
 * 员工加班申请表
 *
 */
@Data
public class StaffOvertimeInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "申请人")
    private String applyUser;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "总计时间")
    private String totleTime;
    @Schema(description = "加班事由")
    private String cause;
    @Schema(description = "开始时间")
    private Long startTime;
    @Schema(description = "结束时间")
    private Long endTime;
    @Schema(description = "申请日期")
    private Long applyDate;
    @Schema(description = "申请部门")
    private String department;
    @Schema(description = "记入类别")
    private String category;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;

}
