package com.taotao.cloud.goods.biz.elasticsearch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品搜索结果实体
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsGoods {

	@Schema(description = "skuId")
	private String skuId;

	@Schema(description = "商品id")
	private String goodsId;

	@Schema(description = "商品名称")
	private String goodsName;

}
