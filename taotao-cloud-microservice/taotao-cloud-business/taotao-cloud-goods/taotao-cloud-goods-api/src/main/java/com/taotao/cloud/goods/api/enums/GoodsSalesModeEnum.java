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

package com.taotao.cloud.goods.api.enums;

/**
 * 商品审核
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:32:17
 */
public enum GoodsSalesModeEnum {
    /** 需要审核 并且待审核 */
    RETAIL("零售"),
    /** 审核通过 */
    WHOLESALE("批发");

    private final String description;

    GoodsSalesModeEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
