package com.taotao.cloud.distribution.api.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.utils.lang.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分销查询参数
 */
@Data
@Schema(description = "分销查询参数")
public class DistributionSearchParams {

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "分销员状态", allowableValues = "APPLY,RETREAT,REFUSE,PASS")
	private String distributionStatus;

	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(StringUtil.isNotEmpty(memberName), "member_name", memberName);
		queryWrapper.eq(StringUtil.isNotEmpty(distributionStatus), "distribution_status",
			distributionStatus);
		return queryWrapper;
	}
}
