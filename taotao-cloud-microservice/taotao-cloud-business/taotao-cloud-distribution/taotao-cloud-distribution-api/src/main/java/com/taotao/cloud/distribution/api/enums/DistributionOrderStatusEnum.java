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

package com.taotao.cloud.distribution.api.enums;

/** 分销员订单状态 */
public enum DistributionOrderStatusEnum {
    // 待结算（冻结）
    WAIT_BILL("待结算"),
    // 待提现
    WAIT_CASH("待提现"),
    // 已提现
    COMPLETE_CASH("提现完成"),
    // 订单取消
    CANCEL("订单取消"),
    // 订单取消
    REFUND("退款");

    private final String description;

    DistributionOrderStatusEnum(String description) {
        this.description = description;
    }
}
