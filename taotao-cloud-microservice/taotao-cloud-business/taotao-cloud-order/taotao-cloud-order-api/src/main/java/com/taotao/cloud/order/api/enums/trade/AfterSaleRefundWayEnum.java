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

package com.taotao.cloud.order.api.enums.trade;

/**
 * 退款方式
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:23:33
 */
public enum AfterSaleRefundWayEnum {

    /** "原路退回" */
    ORIGINAL("原路退回"),
    /** "线下支付" */
    OFFLINE("线下支付");

    private final String description;

    AfterSaleRefundWayEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
