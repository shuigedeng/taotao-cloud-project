package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品库存DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:36
 */
public record GoodsSkuStockDTO(
	@Schema(description = "商品skuId")
	Long skuId,

	@Schema(description = "库存")
	Integer quantity
	) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;


}
