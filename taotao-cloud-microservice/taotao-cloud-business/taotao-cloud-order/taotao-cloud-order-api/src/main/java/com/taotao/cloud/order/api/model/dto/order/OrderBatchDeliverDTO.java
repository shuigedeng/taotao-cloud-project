package com.taotao.cloud.order.api.model.dto.order;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 订单批量发货DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@RecordBuilder
@Schema(description = "订单批量发货DTO")
public record OrderBatchDeliverDTO(
	@Schema(description = "订单SN")
	String orderSn,

	@Schema(description = "物流公司ID")
	Long logisticsId,

	@Schema(description = "物流公司名称")
	String logisticsName,

	@Schema(description = "发货单号")
	String logisticsNo
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;


}
