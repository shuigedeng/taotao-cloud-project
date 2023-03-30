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
 * 售后状态
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:23:36
 */
public enum AfterSaleStatusEnum {

    /** 售后服务类型枚举 */
    APPLY("申请中"),
    PASS("已通过"),
    REFUSE("已拒绝"),
    BUYER_RETURN("待卖家收货"),
    SELLER_CONFIRM("卖家确认收货"),
    SELLER_TERMINATION("卖家终止售后"),
    BUYER_CANCEL("买家取消售后"),
    WAIT_REFUND("等待平台退款"),
    COMPLETE("完成");

    private final String description;

    AfterSaleStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
