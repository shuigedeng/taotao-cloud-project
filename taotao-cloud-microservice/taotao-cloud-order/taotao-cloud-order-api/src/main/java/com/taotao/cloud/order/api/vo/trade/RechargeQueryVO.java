package com.taotao.cloud.order.api.vo.trade;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预存款充值记录查询条件
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "预存款充值记录查询条件")
public class RechargeQueryVO implements Serializable {

	private static final long serialVersionUID = 318396158590640917L;

	@Schema(description = "充值订单编号")
	private String rechargeSn;

	@Schema(description = "会员Id")
	private String memberId;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "充值开始时间")
	private String startDate;

	@Schema(description = "充值结束时间")
	private String endDate;

}
