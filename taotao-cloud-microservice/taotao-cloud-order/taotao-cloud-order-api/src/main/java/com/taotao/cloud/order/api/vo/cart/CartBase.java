package com.taotao.cloud.order.api.vo.cart;

import com.taotao.cloud.order.api.dto.order.PriceDetailDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车基础
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "购物车基础")
public class CartBase implements Serializable {

	private static final long serialVersionUID = -5172752506920017597L;

	@Schema(description = "卖家id")
	private String storeId;

	@Schema(description = "卖家姓名")
	private String storeName;

	@Schema(description = "此商品价格流水计算")
	private PriceDetailDTO priceDetailDTO;

	@Schema(description = "此商品价格展示")
	private PriceDetailVO priceDetailVO;

	public CartBase() {
		priceDetailDTO = new PriceDetailDTO();
	}

	public PriceDetailVO getPriceDetailVO() {
		if (this.priceDetailDTO != null) {
			return new PriceDetailVO(priceDetailDTO);
		}
		return new PriceDetailVO();
	}
}
