package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 优惠券活动VO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivityVO extends CouponActivityBaseVO {

    @Schema(description =  "优惠券活动下的优惠券列表")
    private List<CouponActivityItemVO> couponActivityItems;

}
