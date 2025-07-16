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
 * DispatchStrategy
 *
 * @author shuigedeng
 * @since 2021/2/22
 */
@Getter
@AllArgsConstructor
public enum DispatchStrategy {

    /**
     * 健康度优先
     */
    HEALTH_FIRST(1),
    /**
     * 随机
     */
    RANDOM(2);

    private final int v;

    public static DispatchStrategy of(Integer v) {
        if (v == null) {
            return HEALTH_FIRST;
        }
        for (DispatchStrategy ds : values()) {
            if (v.equals(ds.v)) {
                return ds;
            }
        }
        throw new IllegalArgumentException("unknown DispatchStrategy of " + v);
    }
}
