package com.taotao.cloud.order.api.vo.trade;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预存款充值记录查询条件
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "预存款充值记录查询条件")
public class DepositQueryVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -6413611244037073693L;

	@Schema(description = "会员Id")
	private String memberId;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "起始日期")
	private String startDate;

	@Schema(description = "结束日期")
	private String endDate;

}
