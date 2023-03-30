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

package com.taotao.cloud.stock.api.common.util;

public enum ObjectType {
    UNKNOWN(1000),

    APPLICATION(1101),

    // keystone
    TENANT(1201),
    USER(1202),

    // customer
    CUSTOMER(1301),

    // store
    STORE(1401),
    STORE_EDITION(1402),
    STORE_ROLE(1403),
    STORE_STAFF(1404),
    STORE_MEMBER(1405);

    private final int code;

    ObjectType(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
