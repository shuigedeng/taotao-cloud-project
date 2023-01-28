package com.taotao.cloud.workflow.biz.common.model.form.rewardpunishment;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 行政赏罚单
 */
@Data
public class RewardPunishmentInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "赏罚原因")
    private String reason;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "员工姓名")
    private String fullName;
    @Schema(description = "填表日期")
    private Long fillFromDate;
    @Schema(description = "赏罚金额")
    private BigDecimal rewardPun;
    @Schema(description = "员工职位")
    private String position;
    @Schema(description = "员工部门")
    private String department;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
}
