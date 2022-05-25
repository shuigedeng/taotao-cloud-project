package com.taotao.cloud.distribution.api.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.utils.lang.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分销查询参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分销查询参数")
public class DistributionPageQuery {

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
