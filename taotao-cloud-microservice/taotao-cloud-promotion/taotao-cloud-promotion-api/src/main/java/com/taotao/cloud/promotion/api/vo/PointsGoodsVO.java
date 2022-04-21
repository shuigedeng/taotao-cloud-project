package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 积分商品视图对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PointsGoodsVO extends PointsGoodsBaseVO {

	private static final long serialVersionUID = -5163709626742905057L;

	@Schema(description = "商品规格详细信息")
	private GoodsSkuVO goodsSku;

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class GoodsSkuVO {
		private Long id;


	}

}
