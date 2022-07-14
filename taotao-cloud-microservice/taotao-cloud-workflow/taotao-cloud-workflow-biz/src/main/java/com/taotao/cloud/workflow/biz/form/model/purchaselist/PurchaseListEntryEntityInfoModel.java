package com.taotao.cloud.workflow.biz.form.model.purchaselist;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 日常物品采购清单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class PurchaseListEntryEntityInfoModel {
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "采购主键")
    private String purchaseId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "规格型号")
    private String specifications;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "数量")
    private String qty;
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "排序码")
    private Long sortCode;
}
