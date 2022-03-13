package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品库存DTO
 **/
@Data
public class GoodsSkuStockDTO {

	@Schema(description = "商品skuId")
	private String skuId;

	@Schema(description = "库存")
	private Integer quantity;


}
