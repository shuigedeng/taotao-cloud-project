package com.taotao.cloud.order.api.dto.order;

import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息队列传输订单信息实体
 */
@Schema(description = "订单批量发货DTO")
public record OrderMessage(

	/**
	 * 订单号
	 */
	String orderSn,

	/**
	 * 新状态
	 *
	 * @see OrderStatusEnum
	 */
	OrderStatusEnum newStatus,

	/**
	 * 支付方式
	 */
	String paymentMethod
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;


}
