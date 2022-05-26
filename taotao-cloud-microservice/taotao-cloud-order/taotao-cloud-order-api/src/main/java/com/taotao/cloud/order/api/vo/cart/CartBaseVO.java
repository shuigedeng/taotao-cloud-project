package com.taotao.cloud.order.api.vo.cart;

import com.taotao.cloud.order.api.dto.order.PriceDetailDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 购物车基础
 */
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
			return new PriceDetailVO(priceDetailDTO);
		}
		return new PriceDetailVO();
	}
}
