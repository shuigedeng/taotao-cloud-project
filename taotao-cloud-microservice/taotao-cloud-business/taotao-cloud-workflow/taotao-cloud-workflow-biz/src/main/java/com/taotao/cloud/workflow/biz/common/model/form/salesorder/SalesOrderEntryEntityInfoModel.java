package com.taotao.cloud.workflow.biz.common.model.form.salesorder;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 销售订单
 *
 */
@Data
public class SalesOrderEntryEntityInfoModel {
    @Schema(description = "发货明细主键")
    private String id;
    @Schema(description = "订单主键")
    private String salesOrderId;
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
    @Schema(description = "描述")
    private String description;
    @Schema(description = "排序码")
    private Long sortCode;
}
