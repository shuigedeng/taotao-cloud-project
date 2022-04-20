package com.taotao.cloud.promotion.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import lombok.experimental.SuperBuilder;

/**
 * 砍价活动商品实体类
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityGoodsBaseDTO {


	@Schema(description = "结算价格")
	@NotEmpty(message = "结算价格不能为空")
	private BigDecimal settlementPrice;

	@Schema(description = "商品原价")
	private BigDecimal originalPrice;

	@Schema(description = "最低购买金额")
	@NotEmpty(message = "最低购买金额不能为空")
	private BigDecimal purchasePrice;

	@Schema(description = "货品id")
	@NotEmpty(message = "货品id不能为空")
	private String goodsId;

	@Schema(description = "货品SkuId")
	@NotEmpty(message = "货品SkuId不能为空")
	private String skuId;

	@Schema(description = "货品名称")
	private String goodsName;

	@Schema(description = "缩略图")
	private String thumbnail;

	@Schema(description = "活动库存")
	@NotEmpty(message = "活动库存不能为空")
	private Integer stock;

	@Schema(description = "每人最低砍价金额")
	@NotEmpty(message = "每人最低砍价金额不能为空")
	private BigDecimal lowestPrice;

	@Schema(description = "每人最高砍价金额")
	@NotEmpty(message = "每人最高砍价金额不能为空")
	private BigDecimal highestPrice;
}
