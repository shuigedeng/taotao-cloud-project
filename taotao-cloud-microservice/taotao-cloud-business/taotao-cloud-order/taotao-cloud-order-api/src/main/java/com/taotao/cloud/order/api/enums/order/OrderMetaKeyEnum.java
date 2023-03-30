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

package com.taotao.cloud.order.api.enums.order;

/**
 * 订单元Key枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:22:08
 */
public enum OrderMetaKeyEnum {

    /** 订单属性 */
    POINT("使用的积分"),
    DISCOUNT_PRICE("优惠金额"),
    GIFT_POINT("赠送的积分"),
    GIFT_COUPON("赠送的优惠券"),
    GIFT_SKU("赠品");

    private final String description;

    OrderMetaKeyEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String description() {
        return this.description;
    }
}
