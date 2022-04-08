package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 直播商品VO
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommodityVO extends CommodityBaseVO {

	@Schema(description = "SKU库存")
	private Integer quantity;

	@Schema(description = "店铺名称")
	private String storeName;
}
