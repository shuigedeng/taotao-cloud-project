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

package com.taotao.cloud.sys.api.shortlink.java.com.taotao.cloud.shorlink.api.api.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 短链域名类型 - 枚举
 *
 * @since 2022/05/03
 */
public enum ShortLinkDomainTypeEnum {
    ORIGIN(0, "原生"),

    CUSTOMER(1, "用户自建");

    ShortLinkDomainTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Getter
    private final Integer code;

    @Getter
    private final String msg;

    public static Optional<ShortLinkDomainTypeEnum> findEnum(Integer code) {
        return Arrays.stream(ShortLinkDomainTypeEnum.values())
                .filter(itemEnum -> itemEnum.code.equals(code))
                .findFirst();
    }
}
