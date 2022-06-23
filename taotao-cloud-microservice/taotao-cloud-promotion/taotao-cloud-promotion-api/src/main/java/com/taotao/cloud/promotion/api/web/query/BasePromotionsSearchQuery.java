package com.taotao.cloud.promotion.api.web.query;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.tools.PromotionTools;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BasePromotionsSearchQuery extends PageParam implements Serializable {

	@Schema(description = "活动id")
	private String id;

	@Schema(description = "活动开始时间")
	private Long startTime;

	@Schema(description = "活动结束时间")
	private Long endTime;

	/**
	 * @see PromotionsStatusEnum
	 */
	@Schema(description = "活动状态")
	private String promotionStatus;

	/**
	 * @see PromotionsScopeTypeEnum
	 */
	@Schema(description = "关联范围类型")
	private String scopeType;

	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();

		if (CharSequenceUtil.isNotEmpty(id)) {
			queryWrapper.eq("id", id);
		}
		if (startTime != null) {
			queryWrapper.ge("start_time", new Date(startTime));
		}
		if (endTime != null) {
			queryWrapper.le("end_time", new Date(endTime));
		}
		if (CharSequenceUtil.isNotEmpty(promotionStatus)) {
			queryWrapper.and(
				PromotionTools.queryPromotionStatus(PromotionsStatusEnum.valueOf(promotionStatus)));
		}
		if (CharSequenceUtil.isNotEmpty(scopeType)) {
			queryWrapper.eq("scope_type", scopeType);
		}
		queryWrapper.eq("delete_flag", false);
		return queryWrapper;
	}


}
