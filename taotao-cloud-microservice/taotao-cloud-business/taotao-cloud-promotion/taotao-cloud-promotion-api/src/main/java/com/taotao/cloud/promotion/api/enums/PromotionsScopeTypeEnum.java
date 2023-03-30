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

package com.taotao.cloud.promotion.api.enums;

/** 促销适用范围类型枚举 */
public enum PromotionsScopeTypeEnum {

    /** 枚举 */
    ALL("全品类"),
    PORTION_GOODS_CATEGORY("部分商品分类"),
    PORTION_SHOP_CATEGORY("部分店铺分类"),
    PORTION_GOODS("指定商品");

    private final String description;

    PromotionsScopeTypeEnum(String str) {
        this.description = str;
    }

    public String description() {
        return description;
    }
}
