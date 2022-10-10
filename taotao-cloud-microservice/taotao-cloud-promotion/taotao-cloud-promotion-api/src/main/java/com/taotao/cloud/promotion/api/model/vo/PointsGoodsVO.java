package com.taotao.cloud.promotion.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 积分商品视图对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PointsGoodsVO extends PointsGoodsBaseVO {

	@Serial
	private static final long serialVersionUID = -5163709626742905057L;

	private String test;

	@Schema(description = "商品规格详细信息")
	private GoodsSkuVO goodsSku;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class GoodsSkuVO {
		private Long id;

	}

}
