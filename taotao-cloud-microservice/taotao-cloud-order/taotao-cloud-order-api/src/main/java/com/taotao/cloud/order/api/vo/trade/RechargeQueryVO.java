package com.taotao.cloud.order.api.vo.trade;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预存款充值记录查询条件
 */
@RecordBuilder
@Schema(description = "预存款充值记录查询条件")
public record RechargeQueryVO(

	@Schema(description = "充值订单编号")
	String rechargeSn,

	@Schema(description = "会员Id")
	String memberId,

	@Schema(description = "会员名称")
	String memberName,

	@Schema(description = "充值开始时间")
	String startDate,

	@Schema(description = "充值结束时间")
	String endDate
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 318396158590640917L;


}
