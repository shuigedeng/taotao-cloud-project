package com.taotao.cloud.order.api.vo.trade;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 预存款充值记录查询条件
 */
@Data
@Schema(description = "预存款充值记录查询条件")
public class DepositQueryVO implements Serializable {


	private static final long serialVersionUID = -6413611244037073693L;

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

	@Schema(description = "起始日期")
	private String startDate;

	@Schema(description = "结束日期")
	private String endDate;


}
