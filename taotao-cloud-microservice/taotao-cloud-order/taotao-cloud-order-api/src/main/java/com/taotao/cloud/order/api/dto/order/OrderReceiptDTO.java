package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;


/**
 * 订单发票
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单发票")
public class OrderReceiptDTO extends Receipt {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	@Schema(description = "订单状态")
	private String orderStatus;

}
