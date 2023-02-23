package com.taotao.cloud.order.api.model.vo.cart;

import com.taotao.cloud.order.api.model.dto.order.PriceDetailDTO;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * 购物车基础
 */
@RecordBuilder
@Schema(description = "购物车基础")
public record CartBaseVO(
	@Schema(description = "卖家id")
	String storeId,

	@Schema(description = "卖家姓名")
	String storeName,

	@Schema(description = "此商品价格流水计算")
	PriceDetailDTO priceDetailDTO,

	@Schema(description = "此商品价格展示")
	PriceDetailVO priceDetailVO

) implements Serializable {

	@Serial
	private static final long serialVersionUID = -5172752506920017597L;

	public PriceDetailVO getPriceDetailVO() {
		if (this.priceDetailDTO != null) {
			return PriceDetailVO.priceDetailVO(priceDetailDTO);
		}
		return PriceDetailVOBuilder.builder().build();
	}
}
