package com.taotao.cloud.order.api.model.page.order;

import com.taotao.cloud.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 店铺流水查询DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:19:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺流水查询DTO")
public class StoreFlowQuery {

	@Schema(description = "类型")
	private String type;

	@Schema(description = "售后编号")
	private String refundSn;

	@Schema(description = "售后编号")
	private String orderSn;

	@Schema(description = "过滤只看分销订单")
	private Boolean justDistribution;

	@Schema(description = "结算单")
	private BillDTO bill;

	@Data
	public static class BillDTO{
		private LocalDateTime startTime;

		private LocalDateTime endTime;

		private Long storeId;
	}
}
