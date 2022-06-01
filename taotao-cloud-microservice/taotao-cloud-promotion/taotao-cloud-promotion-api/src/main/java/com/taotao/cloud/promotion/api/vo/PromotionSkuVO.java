package com.taotao.cloud.promotion.api.vo;

import com.taotao.cloud.common.enums.PromotionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 促销skuVO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionSkuVO implements Serializable {

	/**
	 * 促销类型
	 *
	 * @see PromotionTypeEnum
	 */
	private String promotionType;

	/**
	 * 促销活动
	 */
	private Long activityId;

}
