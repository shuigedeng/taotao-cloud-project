package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品库存DTO
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-18 21:59:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSkuStockDTO {

	@Schema(description = "商品skuId")
	private Long skuId;

	@Schema(description = "库存")
	private Integer quantity;


}
