package com.taotao.cloud.workflow.api.common.model.form.applybanquet;


import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 宴请申请
 */
@Data
public class ApplyBanquetInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "预计费用")
    private BigDecimal expectedCost;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "宴请人员")
    private String banquetPeople;
    @Schema(description = "申请人员")
    private String applyUser;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "人员总数")
    private String total;
    @Schema(description = "所属职务")
    private String position;
    @Schema(description = "宴请地点")
    private String place;
    @Schema(description = "申请日期")
    private Long applyDate;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "宴请人数")
    private String banquetNum;

}
