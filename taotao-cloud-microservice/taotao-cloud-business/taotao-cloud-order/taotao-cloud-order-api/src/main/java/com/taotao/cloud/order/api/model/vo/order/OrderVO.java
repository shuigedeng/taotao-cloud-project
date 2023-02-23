package com.taotao.cloud.order.api.model.vo.order;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 订单vo
 */
@RecordBuilder
@Schema(description = "订单vo")
public record OrderVO(

	@Schema(description = "订单商品项目")
	List<OrderItemVO> orderItems,

	@Schema(description = "订单vo")
	OrderBaseVO orderBase
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	// public OrderVO(OrderBaseVO order, List<OrderItem> orderItems) {
	// 	BeanUtil.copyProperties(order, this);
	// 	this.setOrderItems(orderItems);
	// }
}
