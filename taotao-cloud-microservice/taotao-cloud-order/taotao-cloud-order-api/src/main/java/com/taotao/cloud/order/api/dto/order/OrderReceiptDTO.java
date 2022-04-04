package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 订单发票
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单发票")
public class OrderReceiptDTO extends Receipt {

	@Schema(description = "订单状态")
	private String orderStatus;

}
