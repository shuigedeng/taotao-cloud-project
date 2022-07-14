package com.taotao.cloud.workflow.biz.form.model.vehicleapply;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 车辆申请
 *
 */
@Data
public class VehicleApplyInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "结束时间")
    private Long  endDate;
    @Schema(description = "目的地")
    private String destination;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "公里数")
    private String kilometreNum;
    @Schema(description = "车牌号")
    private String plateNum;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "路费金额")
    private BigDecimal roadFee;
    @Schema(description = "所在部门")
    private String department;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "用车人")
    private String carMan;
    @Schema(description = "随行人数")
    private String entourage;
    @Schema(description = "开始日期")
    private Long  startDate;
}
