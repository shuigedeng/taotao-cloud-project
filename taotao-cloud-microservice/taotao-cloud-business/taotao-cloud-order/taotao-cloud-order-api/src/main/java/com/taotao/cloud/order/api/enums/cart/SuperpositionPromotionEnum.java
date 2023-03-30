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

package com.taotao.cloud.order.api.enums.cart;

/**
 * 活动叠加
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:21:46
 */
public enum SuperpositionPromotionEnum {

    /** 商品促销放在商品属性，这里只负责可叠加的其他促销 叠加促销枚举，每一个商品，以下每个参数都只能参加一个 */
    SELLER_COUPON("店铺优惠券"),
    PLATFORM_COUPON("平台优惠券"),
    FULL_DISCOUNT("满优惠");

    private final String description;

    SuperpositionPromotionEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
