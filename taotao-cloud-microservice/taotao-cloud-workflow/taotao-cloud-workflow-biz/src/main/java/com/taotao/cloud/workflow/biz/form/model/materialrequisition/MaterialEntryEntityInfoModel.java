package com.taotao.cloud.workflow.biz.form.model.materialrequisition;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 领料单
 *
 */
@Data
public class MaterialEntryEntityInfoModel {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "领料主键")
    private String leadeId;
    @Schema(description = "商品名称")
    private String goodsName;
    @Schema(description = "规格型号")
    private String specifications;
    @Schema(description = "单位")
    private String unit;
    @Schema(description = "需数量")
    private String materialDemand;
    @Schema(description = "配数量")
    private String proportioning;
    @Schema(description = "单价")
    private BigDecimal price;
    @Schema(description = "金额")
    private BigDecimal amount;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "排序码")
    private Long sortCode;
}
