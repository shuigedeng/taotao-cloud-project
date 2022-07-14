package com.taotao.cloud.workflow.biz.form.model.outboundorder;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 出库单
 *
 */
@Data
public class OutboundEntryEntityInfoModel {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "出库单号")
    private String outboundId;
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
    @Schema(description = "备注")
    private String description;
    @Schema(description = "排序码")
    private Long sortCode;
}
