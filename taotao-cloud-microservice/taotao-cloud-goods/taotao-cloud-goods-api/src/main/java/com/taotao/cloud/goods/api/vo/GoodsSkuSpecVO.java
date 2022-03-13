package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 商品规格VO
 */
@Data
public class GoodsSkuSpecVO {


	@Schema(description = "商品skuId")
	private String skuId;

	@Schema(description = "商品sku所包含规格")
	private List<SpecValueVO> specValues;

	@Schema(description = "库存")
	private Integer quantity;

}
