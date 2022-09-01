package com.taotao.cloud.order.api.model.vo.cart;

import com.taotao.cloud.order.api.model.dto.order.PriceDetailDTO;
import com.taotao.cloud.order.api.vo.cart.PriceDetailVOBuilder;
import com.taotao.cloud.order.api.web.vo.cart.PriceDetailVOBuilder;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单价格详情
 */
@RecordBuilder
@Schema(description = "订单价格详情")
public record PriceDetailVO(
	@Schema(description = "商品原价")
	BigDecimal originalPrice,

	@Schema(description = "配送费")
	BigDecimal freight,

	@Schema(description = "优惠金额")
	BigDecimal discountPrice,

	@Schema(description = "支付积分")
	Long payPoint,

	@Schema(description = "最终成交金额")
	BigDecimal finalePrice

) implements Serializable {

	@Serial
	private static final long serialVersionUID = -960537582096338500L;


	/**
	 * 初始化默认值
	 *
	 * @param dto dto
	 * @return {@link PriceDetailVO }
	 * @since 2022-05-30 13:58:30
	 */
	public static PriceDetailVO priceDetailVO(PriceDetailDTO dto) {
		return PriceDetailVOBuilder.builder()
			.freight(dto.freightPrice())
			.finalePrice(dto.flowPrice())
			.discountPrice(dto.discountPrice())
			.payPoint(dto.payPoint())
			.originalPrice(dto.goodsPrice())
			.build();
	}

}
