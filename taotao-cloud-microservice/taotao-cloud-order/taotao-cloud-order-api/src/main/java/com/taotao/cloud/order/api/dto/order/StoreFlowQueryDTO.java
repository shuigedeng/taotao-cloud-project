package com.taotao.cloud.order.api.dto.order;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 店铺流水查询DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺流水查询DTO")
public class StoreFlowQueryDTO {

	@Schema(description = "类型")
	private String type;

	@Schema(description = "售后编号")
	private String refundSn;

	@Schema(description = "售后编号")
	private String orderSn;

	@Schema(description = "过滤只看分销订单")
	private Boolean justDistribution;

	@ApiModelProperty("结算单")
	private Bill bill;

	@Schema(description = "分页")
	private PageVO pageVO;

}
