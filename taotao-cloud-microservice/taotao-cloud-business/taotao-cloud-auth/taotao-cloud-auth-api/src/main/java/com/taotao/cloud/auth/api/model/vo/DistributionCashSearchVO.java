package com.taotao.cloud.auth.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 分销佣金查询信息
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分销佣金查询信息")
public class DistributionCashSearchVO {

	@Schema(description = "编号")
	private String sn;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "分销员提现状态", allowableValues = "APPLY,PASS,REFUSE")
	private String distributionCashStatus;

	// public <T> QueryWrapper<T> queryWrapper() {
	// 	QueryWrapper<T> queryWrapper = new QueryWrapper<>();
	// 	if (StringUtils.isNotEmpty(memberName)) {
	// 		queryWrapper.like("distribution_name", memberName);
	// 	}
	// 	if (StringUtils.isNotEmpty(sn)) {
	// 		queryWrapper.like("sn", sn);
	// 	}
	// 	if (StringUtils.isNotEmpty(distributionCashStatus)) {
	// 		queryWrapper.like("distribution_cash_status", distributionCashStatus);
	// 	}
	// 	return queryWrapper;
	// }


}