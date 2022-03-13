package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品购买完成信息
 **/
@Data
public class GoodsCompleteMessage {


	@Schema(description = "商品id")
	private String goodsId;

	@Schema(description = "商品skuId")
	private String skuId;

	@Schema(description = "购买会员sn")
	private String memberId;

	@Schema(description = "购买数量")
	private Integer buyNum;

}
