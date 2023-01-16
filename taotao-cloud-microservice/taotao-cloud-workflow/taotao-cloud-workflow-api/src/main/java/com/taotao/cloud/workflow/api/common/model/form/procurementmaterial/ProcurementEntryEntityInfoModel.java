package com.taotao.cloud.workflow.api.common.model.form.procurementmaterial;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 采购原材料
 *
 */
@Data
public class ProcurementEntryEntityInfoModel {
    @Schema(description = "采购明细主键")
    private String id;
    @Schema(description = "采购主键")
    private String procurementId;
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
    private Long  sortCode;
}
