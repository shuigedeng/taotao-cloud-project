package com.taotao.cloud.workflow.biz.form.model.warehousereceipt;


import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 入库申请单
 *
 */
@Data
public class WarehouseReceiptInfoVO {
    @Schema(description = "主键")
    private String id;
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "供应商名称")
    private String supplierName;
    @Schema(description = "联系电话")
    private String contactPhone;
    @Schema(description = "入库类别")
    private String warehousCategory;
    @Schema(description = "仓库")
    private String warehouse;
    @Schema(description = "入库人")
    private String warehousesPeople;
    @Schema(description = "送货单号")
    private String deliveryNo;
    @Schema(description = "入库单号")
    private String warehouseNo;
    @NotNull(message = "必填")
    @Schema(description = "入库日期")
    private Long warehousDate;
    @Schema(description = "明细")
    List<WarehouseReceiptEntityInfoModel> entryList;

}
