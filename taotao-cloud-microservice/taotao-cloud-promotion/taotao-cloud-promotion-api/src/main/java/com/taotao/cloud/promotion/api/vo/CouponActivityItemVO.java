package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 优惠券活动的优惠券VO
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//public class CouponActivityItemVO extends CouponActivityItem {
public class CouponActivityItemVO  {

    @Schema(description =  "优惠券名称")
    private String couponName;

    @Schema(description =  "面额")
    private Double price;

    /**
     * POINT("打折"), PRICE("减免现金");
     *
     * @see CouponTypeEnum
     */
    @Schema(description =  "优惠券类型")
    private String couponType;

    @Schema(description =  "折扣")
    private Double couponDiscount;
}
