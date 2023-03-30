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

package com.taotao.cloud.member.api.enums;

/**
 * 预存款变动日志业务类型
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:37:08
 */
public enum DepositServiceTypeEnum {
    /** 预存款变动日志业务类型枚举 */
    WALLET_WITHDRAWAL("余额提现"),
    WALLET_PAY("余额支付"),
    WALLET_REFUND("余额退款"),
    WALLET_RECHARGE("余额充值"),
    WALLET_COMMISSION("佣金提成");

    private final String description;

    DepositServiceTypeEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
