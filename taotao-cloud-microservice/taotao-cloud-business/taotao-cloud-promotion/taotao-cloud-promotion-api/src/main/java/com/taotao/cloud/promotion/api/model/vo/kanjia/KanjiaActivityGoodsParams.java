package com.taotao.cloud.promotion.api.model.vo.kanjia;


import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 砍价活动商品查询通用类
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityGoodsParams implements Serializable {

	@Serial
	private static final long serialVersionUID = 1344104067705714289L;

	@Schema(description = "活动商品")
	private String goodsName;

	@Schema(description = "活动开始时间")
	private Long startTime;

	@Schema(description = "活动结束时间")
	private Long endTime;

	@Schema(description = "skuId")
	private String skuId;

	/**
	 * @see PromotionsStatusEnum
	 */
	@Schema(description = "活动状态")
	private String promotionStatus;

	// public <T> QueryWrapper<T> wrapper() {
	// 	QueryWrapper<T> queryWrapper = new QueryWrapper<>();
	//
	// 	if (CharSequenceUtil.isNotEmpty(goodsName)) {
	// 		queryWrapper.like("goods_name", goodsName);
	// 	}
	// 	if (promotionStatus != null) {
	// 		queryWrapper.and(
	// 			PromotionTools.queryPromotionStatus(PromotionsStatusEnum.valueOf(promotionStatus)));
	// 	}
	// 	if (startTime != null) {
	// 		queryWrapper.le("start_time", new Date(startTime));
	// 	}
	// 	if (endTime != null) {
	// 		queryWrapper.ge("end_time", new Date(endTime));
	// 	}
	// 	//if (UserContext.getCurrentUser() != null && UserContext.getCurrentUser().getRole().equals(UserEnums.MEMBER)) {
	// 	//    queryWrapper.gt("stock", 0);
	// 	//}
	// 	queryWrapper.eq("delete_flag", false);
	// 	return queryWrapper;
	// }

}
