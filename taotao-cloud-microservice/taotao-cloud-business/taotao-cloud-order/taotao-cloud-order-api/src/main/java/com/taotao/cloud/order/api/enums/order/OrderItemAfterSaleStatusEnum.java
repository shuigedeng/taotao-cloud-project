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
 * 订单可申请售后状态枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:22:06
 */
public enum OrderItemAfterSaleStatusEnum {

    /** 订单申请售后状态 */
    NEW("新订单，不能申请售后"),
    NOT_APPLIED("未申请"),
    ALREADY_APPLIED("已申请"),
    EXPIRED("已失效不允许申请售后"),
    PART_AFTER_SALE("部分售后");

    private final String description;

    OrderItemAfterSaleStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return this.description;
    }
}
