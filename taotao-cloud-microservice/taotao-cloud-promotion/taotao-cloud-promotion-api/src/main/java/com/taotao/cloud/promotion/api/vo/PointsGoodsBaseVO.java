package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * 积分商品实体类
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointsGoodsBaseVO {
	private Long id;


	@Schema(description = "商品编号")
	private String goodsId;

	@Schema(description = "商品sku编号")
	private String skuId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品原价")
	private BigDecimal originalPrice;

	@Schema(description = "结算价格")
	private BigDecimal settlementPrice;

	@Schema(description = "积分商品分类编号")
	private String pointsGoodsCategoryId;

	@Schema(description = "分类名称")
	private String pointsGoodsCategoryName;

	@Schema(description = "缩略图")
	private String thumbnail;

	@Schema(description = "活动库存数量")
	private Integer activeStock;

	@Schema(description = "兑换积分")
	private Long points;

}
