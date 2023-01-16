package com.taotao.cloud.workflow.api.common.model.form.rewardpunishment;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 行政赏罚单
 *
 * @date 2021/3/15 8:46
 */
@Data
public class RewardPunishmentForm {
    @NotBlank(message = "必填")
    @Schema(description = "赏罚原因")
    private String reason;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "必填")
    @Schema(description = "员工姓名")
    private String fullName;
    @NotNull(message = "必填")
    @Schema(description = "填表日期")
    private Long fillFromDate;
    @NotBlank(message = "必填")
    @Schema(description = "赏罚金额")
    private BigDecimal rewardPun;
    @NotBlank(message = "必填")
    @Schema(description = "员工职位")
    private String position;
    @NotBlank(message = "必填")
    @Schema(description = "员工部门")
    private String department;
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
