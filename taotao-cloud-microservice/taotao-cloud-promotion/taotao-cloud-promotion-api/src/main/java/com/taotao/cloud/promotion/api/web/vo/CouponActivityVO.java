package com.taotao.cloud.promotion.api.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * 优惠券活动VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivityVO extends CouponActivityBaseVO {

	@Serial
	private static final long serialVersionUID = 7814832369110695758L;

	@Schema(description = "优惠券活动下的优惠券列表")
	private List<CouponActivityItemVO> couponActivityItems;

}
