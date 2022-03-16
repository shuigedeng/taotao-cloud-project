package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 直播商品VO
 */
@Data
//public class CommodityVO extends Commodity {
public class CommodityVO  {

	@Schema(description = "SKU库存")
	private Integer quantity;

	@Schema(description = "店铺名称")
	private String storeName;
}
