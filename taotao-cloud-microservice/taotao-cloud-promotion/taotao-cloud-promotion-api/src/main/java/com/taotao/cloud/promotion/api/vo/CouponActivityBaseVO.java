package com.taotao.cloud.promotion.api.vo;

import com.taotao.cloud.promotion.api.enums.CouponActivitySendTypeEnum;
import com.taotao.cloud.promotion.api.enums.CouponActivityTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 优惠券活动实体类
 *
 * @since 2020-03-19 10:44 上午
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivityBaseVO {

	private Long id;
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
