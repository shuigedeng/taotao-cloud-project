package com.taotao.cloud.order.api.web.dto.order;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;


/**
 * 订单发票
 */
@RecordBuilder
@Schema(description = "订单发票")
public record OrderReceiptDTO(
	@Schema(description = "订单状态")
	String orderStatus,

	@Schema(description = "发票子内容")
	ReceiptDTO receipt
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

}
