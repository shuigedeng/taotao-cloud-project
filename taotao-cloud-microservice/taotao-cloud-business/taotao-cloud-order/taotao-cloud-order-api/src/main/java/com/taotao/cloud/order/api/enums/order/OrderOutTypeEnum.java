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

import java.util.ArrayList;
import java.util.List;

/**
 * 订单出库的类型枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:22:18
 */
public enum OrderOutTypeEnum {

    /** 出库类型枚举 */
    GOODS("商品"),
    SECKILL_GOODS("秒杀活动商品");

    private final String description;

    OrderOutTypeEnum(String description) {
        this.description = description;
    }

    public String description() {
        return this.description;
    }

    public static List<String> getAll() {
        List<String> all = new ArrayList<>();
        all.add(OrderOutTypeEnum.GOODS.name());
        all.add(OrderOutTypeEnum.SECKILL_GOODS.name());
        return all;
    }
}
