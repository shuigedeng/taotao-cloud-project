package com.taotao.cloud.distribution.api.vo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.utils.lang.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分销佣金查询信息
 */
@Data
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

	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (StringUtil.isNotEmpty(memberName)) {
			queryWrapper.like("distribution_name", memberName);
		}
		if (StringUtil.isNotEmpty(sn)) {
			queryWrapper.like("sn", sn);
		}
		if (StringUtil.isNotEmpty(distributionCashStatus)) {
			queryWrapper.like("distribution_cash_status", distributionCashStatus);
		}
		return queryWrapper;
	}


}
