package com.taotao.cloud.promotion.api.enums;

/**
 * 优惠券活动类型枚举
 */
public enum CouponActivityTypeEnum {

    /**
     * "新人赠券"
     */
    REGISTERED("新人赠券"),
    /**
     * "精确发券"
     */
    SPECIFY("精确发券");

    private final String description;

    CouponActivityTypeEnum(String str) {
        this.description = str;
    }

    public String description() {
        return description;
    }
}
