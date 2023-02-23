package com.taotao.cloud.workflow.biz.common.model.form.outboundorder;


import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 出库单
 */
@Data
public class OutboundOrderInfoVO {
    @Schema(description = "主键")
    private String id;
    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "客户名称")
    private String customerName;
    @Schema(description = "仓库")
    private String warehouse;
    @Schema(description = "仓库人")
    private String outStorage;
    @Schema(description = "业务人员")
    private String businessPeople;
    @Schema(description = "业务类型")
    private String businessType;
    @NotNull(message = "出库日期不能为空")
    @Schema(description = "出库日期")
    private Long outboundDate;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "明细")
    List<OutboundEntryEntityInfoModel> entryList;
}
