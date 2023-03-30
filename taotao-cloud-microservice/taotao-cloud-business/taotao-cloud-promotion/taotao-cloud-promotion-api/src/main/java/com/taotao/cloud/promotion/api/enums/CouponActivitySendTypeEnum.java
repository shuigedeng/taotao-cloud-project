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

/** 优惠券活动发送类型枚举 */
public enum CouponActivitySendTypeEnum {

    /** "全部会员" */
    ALL("全部会员"),
    /** "指定会员" */
    DESIGNATED("指定会员");

    private final String description;

    CouponActivitySendTypeEnum(String str) {
        this.description = str;
    }

    public String description() {
        return description;
    }
}
