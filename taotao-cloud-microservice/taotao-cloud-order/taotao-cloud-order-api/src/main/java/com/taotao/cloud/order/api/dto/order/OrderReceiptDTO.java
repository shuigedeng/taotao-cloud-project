package com.taotao.cloud.order.api.dto.order;

import cn.lili.modules.order.order.entity.dos.Receipt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 订单发票
 */
@Data
@Schema(description = "订单发票")
public class OrderReceiptDTO extends Receipt {

	@Schema(description = "订单状态")
	private String orderStatus;

}
