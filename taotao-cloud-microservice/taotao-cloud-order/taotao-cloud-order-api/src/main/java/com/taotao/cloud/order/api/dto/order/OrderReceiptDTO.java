package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;


/**
 * 订单发票
 */
@Schema(description = "订单发票")
public record OrderReceiptDTO(
	@Schema(description = "订单状态")
	String orderStatus,

	Receipt receipt
) {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L,


}
