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
 * 购物车渲染枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:21:43
 */
public enum RenderStepEnum {

    /** 购物车渲染枚举 */
    CHECK_DATA("校验商品"),
    CHECKED_FILTER("选择商品过滤"),
    COUPON("优惠券价格渲染"),
    SKU_PROMOTION("商品促销计算"),
    FULL_DISCOUNT("满减计算"),
    SKU_FREIGHT("运费计算"),
    DISTRIBUTION("分配需要分配的促销金额"),
    PLATFORM_COMMISSION("平台佣金"),
    CART_PRICE("购物车金额计算"),
    CART_SN("交易编号创建");

    private final String distribution;

    public String getDistribution() {
        return distribution;
    }

    RenderStepEnum(String distribution) {
        this.distribution = distribution;
    }
}
