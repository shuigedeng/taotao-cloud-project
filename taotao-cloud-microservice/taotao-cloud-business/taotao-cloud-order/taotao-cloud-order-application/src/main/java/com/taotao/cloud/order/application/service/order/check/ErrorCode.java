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

package com.taotao.cloud.order.application.service.order.check;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /** 公共 */
    SUCCESS("0000", "操作成功"),
    FAIL("0001", "失败"),
    ERROR("0002", "异常"),
    PARAM_NULL_ERROR("0003", "参数为空"),
    PARAM_SKU_NULL_ERROR("0004", "SKU参数为空"),
    PARAM_PRICE_NULL_ERROR("0005", "价格参数为空"),
    PARAM_STOCK_NULL_ERROR("0006", "库存参数为空"),
    PARAM_PRICE_ILLEGAL_ERROR("0007", "不合法的价格参数"),
    PARAM_STOCK_ILLEGAL_ERROR("0008", "不合法的库存参数"),
    ;

    private String code;

    private String desc;

    ErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
