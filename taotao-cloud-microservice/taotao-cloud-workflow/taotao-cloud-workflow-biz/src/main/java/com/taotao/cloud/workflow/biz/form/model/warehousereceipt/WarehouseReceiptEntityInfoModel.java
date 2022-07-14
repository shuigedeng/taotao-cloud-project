package com.taotao.cloud.workflow.biz.form.model.warehousereceipt;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 入库申请单
 *
 */
@Data
public class WarehouseReceiptEntityInfoModel {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "入库主键")
    private String warehouseId;
    @Schema(description = "商品名称")
    private String goodsName;
    @Schema(description = "规格型号")
    private String specifications;
    @Schema(description = "单位")
    private String unit;
    @Schema(description = "数量")
    private BigDecimal qty;
    @Schema(description = "单价")
    private BigDecimal price;
    @Schema(description = "金额")
    private BigDecimal amount;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "排序码")
    private Long sortCode;
}
