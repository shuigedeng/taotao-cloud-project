package com.taotao.cloud.workflow.biz.common.model.form.applydelivergoods;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 发货申请单
 */
@Data
public class ApplyDeliverGoodsEntryInfoModel {
    @Schema(description = "发货明细主键")
    private String id;
    @Schema(description = "发货主键")
    private String invoiceId;
    @Schema(description = "商品名称")
    private String goodsName;
    @Schema(description = "规格型号")
    private String specifications;
    @Schema(description = "单位")
    private String unit;
    @Schema(description = "数量")
    private String qty;
    @Schema(description = "单价")
    private BigDecimal price;
    @Schema(description = "金额")
    private BigDecimal amount;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "排序码",example = "1")
    private Long sortCode;
}
