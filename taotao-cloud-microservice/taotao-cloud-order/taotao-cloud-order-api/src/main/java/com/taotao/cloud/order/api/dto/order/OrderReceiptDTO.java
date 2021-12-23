package com.taotao.cloud.order.api.dto.order;

import cn.lili.modules.order.order.entity.dos.Receipt;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 订单发票
 *
 * @author lili
 * @since 2020/11/28 11:38
 */
@Schema(description = "订单发票")
public class OrderReceiptDTO extends Receipt {

	@Schema(description = "订单状态")
	private String orderStatus;

}
