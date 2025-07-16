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

package com.taotao.cloud.rpc.client.support.fail.impl;

import com.taotao.cloud.rpc.client.support.fail.FailStrategy;
import com.taotao.cloud.rpc.client.support.fail.enums.FailTypeEnum;

/**
 * 快速失败策略工厂
 *
 * @author shuigedeng
 * @since 0.1.1
 */
public final class FailStrategyFactory {

    private FailStrategyFactory() {}

    /**
     * 失败策略
     *
     * @param failTypeEnum 失败策略枚举
     * @return 失败策略实现
     * @since 0.1.1
     */
    public static FailStrategy failStrategy(final FailTypeEnum failTypeEnum) {
        switch (failTypeEnum) {
            case FAIL_FAST:
                return new FailFastStrategy();
            case FAIL_OVER:
                return new FailOverStrategy();
            default:
                throw new UnsupportedOperationException("not support fail type " + failTypeEnum);
        }
    }
}
