package com.taotao.cloud.member.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员商品收藏VO
 */
@Schema(description = "会员商品收藏VO")
public record GoodsCollectionVO(@Schema(description = "id") String id,
                                @Schema(description = "商品ID") String goodsId,
                                @Schema(description = "规格ID") String skuId,
                                @Schema(description = "商品名称") String goodsName,
                                @Schema(description = "商品图片") String image,
                                @Schema(description = "商品价格") BigDecimal price,
                                @Schema(description = "已失效") String marketEnable) implements
	Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

}
