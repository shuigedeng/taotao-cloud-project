/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.enums;

/**
 * 促销分类枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-22 10:47:43
 */
public enum PromotionTypeEnum {
    /**
     * 促销枚举
     */
    PINTUAN("拼团"),
    SECKILL("秒杀"),
    COUPON("优惠券"),
    PLATFORM_COUPON("平台优惠券"),
    FULL_DISCOUNT("满减"),
    POINTS_GOODS("积分商品"),
    KANJIA("砍价"),
    COUPON_ACTIVITY("优惠券活动");

    /**
     * 有促销库存的活动类型
     */
    static final PromotionTypeEnum[] haveStockPromotion = new PromotionTypeEnum[]{PINTUAN, SECKILL, KANJIA, POINTS_GOODS};

    private final String description;

    PromotionTypeEnum(String description) {
        this.description = description;
    }

    /**
     * 是否拥有库存
     */
    public static boolean haveStock(String promotionType) {
        for (PromotionTypeEnum promotionTypeEnum : haveStockPromotion) {
            if (promotionTypeEnum.name().equals(promotionType)) {
                return true;
            }
        }
        return false;
    }

    public String description() {
        return description;
    }

}
