package com.taotao.cloud.order.api.model.query.recharge;

import com.taotao.cloud.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 预存款充值记录查询条件
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "预存款充值记录查询条件")
public class RechargePageQuery extends PageQuery implements Serializable {
	@Serial
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
