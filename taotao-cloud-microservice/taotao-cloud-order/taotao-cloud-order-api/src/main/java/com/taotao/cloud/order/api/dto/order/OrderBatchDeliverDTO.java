package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单批量发货DTO
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
