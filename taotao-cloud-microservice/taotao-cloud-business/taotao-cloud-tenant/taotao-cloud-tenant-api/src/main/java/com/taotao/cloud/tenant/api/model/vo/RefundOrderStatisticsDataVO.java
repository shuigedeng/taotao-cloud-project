package com.taotao.cloud.tenant.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 退款统计VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundOrderStatisticsDataVO {

	@Schema(description = "售后SN")
	private String refundSn;

	@Schema(description = "商家名称 ")
	private String storeName;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "商品名称")
	private String name;

	@Schema(description = "规格内容")
	private String specs;

	@Schema(description = "实际退款金额")
	private BigDecimal finalPrice;
}
