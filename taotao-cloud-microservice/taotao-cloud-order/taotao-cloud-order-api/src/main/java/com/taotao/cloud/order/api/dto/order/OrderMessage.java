package com.taotao.cloud.order.api.dto.order;

import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息队列传输订单信息实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单批量发货DTO")
public class OrderMessage {


	/**
	 * 订单号
	 */
	private String orderSn;

	/**
	 * 新状态
	 *
	 * @see OrderStatusEnum
	 */
	private OrderStatusEnum newStatus;

	/**
	 * 支付方式
	 */
	private String paymentMethod;

}
