package com.taotao.cloud.distribution.api.vo;

import cn.lili.common.utils.StringUtils;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分销佣金查询信息
 */
@Data
@Schema(description = "分销佣金查询信息")
public class DistributionCashSearchParams extends PageVO {

	/**
	 * 编号
	 */
	@Schema(description = "编号")
	private String sn;
	/**
	 * 会员名称
	 */
	@Schema(description = "会员名称")
	private String memberName;

	/**
	 * 分销员提现状态
	 */
	@Schema(description = "分销员提现状态", allowableValues = "APPLY,PASS,REFUSE")
	private String distributionCashStatus;

	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(memberName)) {
			queryWrapper.like("distribution_name", memberName);
		}
		if (StringUtils.isNotEmpty(sn)) {
			queryWrapper.like("sn", sn);
		}
		if (StringUtils.isNotEmpty(distributionCashStatus)) {
			queryWrapper.like("distribution_cash_status", distributionCashStatus);
		}
		return queryWrapper;
	}


}
