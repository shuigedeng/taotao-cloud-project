package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品库存DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:36
 */
@RecordBuilder
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSkuStockDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "商品skuId")
	private Long skuId;

	@Schema(description = "库存")
	private Integer quantity;


}
