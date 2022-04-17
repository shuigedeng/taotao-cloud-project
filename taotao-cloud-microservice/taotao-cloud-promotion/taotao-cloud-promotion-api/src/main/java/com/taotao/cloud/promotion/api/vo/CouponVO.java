package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 优惠券视图对象
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "优惠券")
//public class CouponVO extends Coupon {
public class CouponVO  {

    private static final long serialVersionUID = 8372420376262437018L;

    /**
     * 促销关联的商品
     */
    @Schema(description =  "优惠券关联商品集合")
    private List<PromotionGoods> promotionGoodsList;

    public CouponVO(Coupon coupon) {
        if (coupon == null) {
            return;
        }
        BeanUtils.copyProperties(coupon, this);
    }
}
