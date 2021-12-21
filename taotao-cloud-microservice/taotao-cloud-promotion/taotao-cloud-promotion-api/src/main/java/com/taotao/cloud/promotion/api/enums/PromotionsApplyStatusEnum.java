package com.taotao.cloud.promotion.api.enums;

/**
 * 促销活动申请状态枚举
 *
 * 
 * @since 2020-03-19 9:36 上午
 */
public enum PromotionsApplyStatusEnum {

    /**
     * 枚举
     */
    APPLY("申请"), PASS("通过"), REFUSE("拒绝");

    private final String description;

    PromotionsApplyStatusEnum(String str) {
        this.description = str;
    }

    public String description() {
        return description;
    }
}
