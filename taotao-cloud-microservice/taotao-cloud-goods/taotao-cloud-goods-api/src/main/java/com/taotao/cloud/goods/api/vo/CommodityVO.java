package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 直播商品VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//public class CommodityVO extends Commodity {
public class CommodityVO  {

	@Schema(description = "SKU库存")
	private Integer quantity;

	@Schema(description = "店铺名称")
	private String storeName;
}
