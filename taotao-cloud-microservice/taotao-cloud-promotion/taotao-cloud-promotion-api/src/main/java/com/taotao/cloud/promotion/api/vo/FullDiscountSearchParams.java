package com.taotao.cloud.promotion.api.vo;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Arrays;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 满优惠查询通用类
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullDiscountSearchParams extends BasePromotionsSearchParams implements Serializable {

    private static final long serialVersionUID = -4052716630253333681L;


    @Schema(description =  "活动名称")
    private String promotionName;

    @Schema(description =  "店铺编号 如有多个','分割")
    private String storeId;

    @Schema(description =  "是否赠优惠券")
    private Boolean isCoupon;

    @Schema(description =  "优惠券id")
    private String couponId;

    @Override
    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = super.queryWrapper();
        if (CharSequenceUtil.isNotEmpty(promotionName)) {
            queryWrapper.like("title", promotionName);
        }
        if (storeId != null) {
            queryWrapper.in("store_id", Arrays.asList(storeId.split(",")));
        }
        if (isCoupon != null) {
            queryWrapper.eq("is_coupon", isCoupon);
        }
        if (CharSequenceUtil.isNotEmpty(couponId)) {
            queryWrapper.eq("coupon_id", couponId);
        }
        return queryWrapper;
    }

}
