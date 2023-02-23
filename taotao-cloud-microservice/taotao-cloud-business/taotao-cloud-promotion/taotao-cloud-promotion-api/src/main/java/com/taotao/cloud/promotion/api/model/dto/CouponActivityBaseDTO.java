package com.taotao.cloud.promotion.api.model.dto;

import com.taotao.cloud.promotion.api.enums.CouponActivitySendTypeEnum;
import com.taotao.cloud.promotion.api.enums.CouponActivityTypeEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 优惠券活动实体类
 *
 * @since 2020-03-19 10:44 上午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivityBaseDTO extends BasePromotionsDTO {

	/**
	 * @see CouponActivityTypeEnum
	 */
	private String couponActivityType;

	/**
	 * @see CouponActivitySendTypeEnum
	 */
	private String activityScope;

	private String activityScopeInfo;
}
