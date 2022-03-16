package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 优惠券活动VO
 *
 */
@Data
@NoArgsConstructor
//public class CouponActivityVO extends CouponActivity {
public class CouponActivityVO  {

    @Schema(description =  "优惠券活动下的优惠券列表")
    private List<CouponActivityItemVO> couponActivityItems;

    //public CouponActivityVO(CouponActivity couponActivity, List<CouponActivityItemVO> couponActivityItemVOS) {
    //    BeanUtil.copyProperties(couponActivity, this);
    //    this.couponActivityItems = couponActivityItemVOS;
    //}
}
