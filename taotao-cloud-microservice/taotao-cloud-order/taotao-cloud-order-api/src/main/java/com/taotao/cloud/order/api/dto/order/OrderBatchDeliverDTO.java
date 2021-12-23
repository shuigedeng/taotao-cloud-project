package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 订单批量发货DTO
 *
 * @since 2021/5/26 4:21 下午
 */
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
