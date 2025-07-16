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

package com.taotao.cloud.rpc.client.support.calltype.impl;

import com.taotao.cloud.rpc.client.support.calltype.CallTypeStrategy;
import com.taotao.cloud.rpc.common.common.constant.enums.CallTypeEnum;

/**
 * callType 策略工厂类
 *
 * @author shuigedeng
 * @since 0.1.0
 */
public final class CallTypeStrategyFactory {

    private CallTypeStrategyFactory() {}

    /**
     * 获取调用策略
     *
     * @param callTypeEnum 调用类型枚举
     * @return 调用策略实现
     * @since 0.1.0
     */
    public static CallTypeStrategy callTypeStrategy(final CallTypeEnum callTypeEnum) {
        switch (callTypeEnum) {
            case SYNC:
                return SyncCallTypeStrategy.getInstance();
            case ONE_WAY:
                return OneWayCallTypeStrategy.getInstance();
            default:
                throw new UnsupportedOperationException("Not support call type : " + callTypeEnum);
        }
    }
}
