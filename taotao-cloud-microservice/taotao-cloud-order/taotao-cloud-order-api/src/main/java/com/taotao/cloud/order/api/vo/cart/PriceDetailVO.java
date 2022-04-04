package com.taotao.cloud.order.api.vo.cart;

import com.taotao.cloud.order.api.dto.order.PriceDetailDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单价格详情
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单价格详情")
public class PriceDetailVO implements Serializable {

	private static final long serialVersionUID = -960537582096338500L;

	@Schema(description = "商品原价")
	private BigDecimal originalPrice;

	@Schema(description = "配送费")
	private BigDecimal freight;

	@Schema(description = "优惠金额")
	private BigDecimal discountPrice;

	@Schema(description = "支付积分")
	private Long payPoint;

	@Schema(description = "最终成交金额")
	private BigDecimal finalePrice;


	/**
	 * 构造器，初始化默认值
	 */
	public PriceDetailVO(PriceDetailDTO dto) {
		this.freight = dto.getFreightPrice();
		this.finalePrice = dto.getFlowPrice();
		this.discountPrice = dto.getDiscountPrice();
		this.payPoint = dto.getPayPoint();
		this.originalPrice = dto.getGoodsPrice();
	}

}
