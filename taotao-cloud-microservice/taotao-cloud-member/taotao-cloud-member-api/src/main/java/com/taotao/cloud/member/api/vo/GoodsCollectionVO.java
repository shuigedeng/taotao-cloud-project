package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 会员商品收藏VO
 */
@Data
@Schema(description = "会员商品收藏VO")
public class GoodsCollectionVO implements Serializable {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "id")
	private String id;

	@Schema(description = "商品ID")
	private String goodsId;

	@Schema(description = "规格ID")
	private String skuId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品图片")
	private String image;

	@Schema(description = "商品价格")
	private BigDecimal price;

	@Schema(description = "已失效")
	private String marketEnable;
}
