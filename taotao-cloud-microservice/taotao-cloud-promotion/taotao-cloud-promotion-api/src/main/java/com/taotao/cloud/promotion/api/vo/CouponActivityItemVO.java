package com.taotao.cloud.promotion.api.vo;

import com.taotao.cloud.promotion.api.enums.CouponTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 优惠券活动的优惠券VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivityItemVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 7814832369110695758L;

	/**
	 * 优惠券活动ID
	 */
	private Long activityId;
	/**
	 * 优惠券ID
	 */
	private Long couponId;
	/**
	 * 优惠券数量
	 */
	private Integer num;

	// *************************************************************************************

	@Schema(description = "优惠券名称")
	private String couponName;

	@Schema(description = "面额")
	private BigDecimal price;

	/**
	 * POINT("打折"), PRICE("减免现金");
	 *
	 * @see CouponTypeEnum
	 */
	@Schema(description = "优惠券类型")
	private String couponType;

	@Schema(description = "折扣")
	private BigDecimal couponDiscount;
}
