package com.taotao.cloud.promotion.api.web.vo;

import com.taotao.cloud.promotion.api.enums.CouponActivitySendTypeEnum;
import com.taotao.cloud.promotion.api.enums.CouponActivityTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * 优惠券活动实体类
 *
 * @since 2020-03-19 10:44 上午
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivityBaseVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 7814832369110695758L;

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
