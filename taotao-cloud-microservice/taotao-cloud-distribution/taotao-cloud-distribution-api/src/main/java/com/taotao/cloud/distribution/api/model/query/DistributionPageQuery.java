package com.taotao.cloud.distribution.api.model.query;

import com.taotao.cloud.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 分销查询参数
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分销查询参数")
public class DistributionPageQuery extends PageQuery {

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "分销员状态", allowableValues = "APPLY,RETREAT,REFUSE,PASS")
	private String distributionStatus;

	// public <T> QueryWrapper<T> queryWrapper() {
	// 	QueryWrapper<T> queryWrapper = new QueryWrapper<>();
	// 	queryWrapper.like(StringUtils.isNotEmpty(memberName), "member_name", memberName);
	// 	queryWrapper.eq(StringUtils.isNotEmpty(distributionStatus), "distribution_status",
	// 		distributionStatus);
	// 	return queryWrapper;
	// }
}
