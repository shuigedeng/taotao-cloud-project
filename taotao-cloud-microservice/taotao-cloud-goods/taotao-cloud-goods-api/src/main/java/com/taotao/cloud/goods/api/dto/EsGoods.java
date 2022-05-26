package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品搜索结果实体
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:18
 */
@RecordBuilder
public record EsGoods(
	@Schema(description = "skuId")
	String skuId,

	@Schema(description = "商品id")
	String goodsId,

	@Schema(description = "商品名称")
	String goodsName
	) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;


}
