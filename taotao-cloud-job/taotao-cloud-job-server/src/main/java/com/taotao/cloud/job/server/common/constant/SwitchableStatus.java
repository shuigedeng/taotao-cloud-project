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

package com.taotao.cloud.job.server.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支持开/关的状态，如 任务状态（JobStatus）和工作流状态（WorkflowStatus）
 *
 * @author shuigedeng
 * @since 2020/4/6
 */
@Getter
@AllArgsConstructor
public enum SwitchableStatus {
    /**
     *
     */
    ENABLE(1),
    DISABLE(2),
    DELETED(99);

    private final int v;

    public static SwitchableStatus of(int v) {
        for (SwitchableStatus type : values()) {
            if (type.v == v) {
                return type;
            }
        }
        throw new IllegalArgumentException("unknown SwitchableStatus of " + v);
    }
}
