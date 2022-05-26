package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 商品规格VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:34:17
 */
@RecordBuilder
public record GoodsSkuSpecVO(

	@Schema(description = "商品skuId")
	Long skuId,

	@Schema(description = "商品sku所包含规格")
	List<SpecValueVO> specValues,

	@Schema(description = "库存")
	Integer quantity
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;


}
