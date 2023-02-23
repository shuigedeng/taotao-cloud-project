package com.taotao.cloud.promotion.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 优惠券活动实体类
 *
 * @since 2020-03-19 10:44 上午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivityItemDTO implements Serializable {

	private Long id;

	private Long activityId;

	private Long couponId;

	private Integer num;

}
