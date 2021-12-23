package com.taotao.cloud.order.api.vo.trade;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * 预存款充值记录查询条件
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
@Schema(description = "预存款充值记录查询条件")
public class RechargeQueryVO implements Serializable {


	private static final long serialVersionUID = 318396158590640917L;

	/**
	 * 充值订单编号
	 */
	@Schema(description = "充值订单编号")
	private String rechargeSn;

	/**
	 * 会员ID
	 */
	@Schema(description = "会员Id")
	private String memberId;
	/**
	 * 会员名称
	 */
	@Schema(description = "会员名称")
	private String memberName;
	/**
	 * 充值时间
	 */
	@Schema(description = "充值开始时间")
	private String startDate;

	/**
	 * 充值时间
	 */
	@Schema(description = "充值结束时间")
	private String endDate;


}
