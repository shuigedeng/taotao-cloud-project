package com.taotao.cloud.promotion.api.dto.search;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.tools.PromotionTools;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasePromotionsSearchParams {

	@Schema(description = "活动id")
	private String id;

	@Schema(description = "活动开始时间")
	private Long startTime;

	@Schema(description = "活动结束时间")
	private Long endTime;

	/**
	 * @see PromotionsStatusEnum
	 */
	@Schema(description = "活动状态 如需同时判断多个活动状态','分割")
	private String promotionStatus;

	/**
	 * @see PromotionsScopeTypeEnum
	 */
	@Schema(description = "关联范围类型")
	private String scopeType;

	@Schema(description = "店铺编号 如有多个','分割")
	private String storeId;

	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = this.baseQueryWrapper();

		if (CharSequenceUtil.isNotEmpty(promotionStatus)) {
			queryWrapper.and(i -> {
				for (String status : promotionStatus.split(",")) {
					i.or(PromotionTools.queryPromotionStatus(PromotionsStatusEnum.valueOf(status)));
				}
			});
		}
		return queryWrapper;
	}

	public <T> QueryWrapper<T> baseQueryWrapper() {
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
		if (CharSequenceUtil.isNotEmpty(scopeType)) {
			queryWrapper.eq("scope_type", scopeType);
		}
		if (CharSequenceUtil.isNotEmpty(storeId)) {
			queryWrapper.in("store_id", Arrays.asList(storeId.split(",")));
		}
		queryWrapper.eq("delete_flag", false);
		return queryWrapper;
	}


}
