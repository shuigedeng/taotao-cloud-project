package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 订单批量发货DTO
 */
@Data
@Schema(description = "订单批量发货DTO")
public class OrderBatchDeliverDTO {

	@Schema(description = "订单SN")
	private String orderSn;

	@Schema(description = "物流公司ID")
	private String logisticsId;

	@Schema(description = "物流公司名称")
	private String logisticsName;

	@Schema(description = "发货单号")
	private String logisticsNo;

}
