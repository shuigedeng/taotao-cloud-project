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

package com.taotao.cloud.job.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Task Processor Type
 *
 * @author shuigedeng
 * @since 2020/3/23
 */
@Getter
@AllArgsConstructor
public enum ProcessorType {
    BUILT_IN(1, "内建处理器"),
    EXTERNAL(4, "外部处理器（动态加载）"),

    @Deprecated
    SHELL(2, "SHELL脚本"),
    @Deprecated
    PYTHON(3, "Python脚本");

    private final int v;
    private final String des;

    public static ProcessorType of(int v) {
        for (ProcessorType type : values()) {
            if (type.v == v) {
                return type;
            }
        }
        throw new IllegalArgumentException("unknown ProcessorType of " + v);
    }
}
