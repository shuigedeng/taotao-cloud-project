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

package com.taotao.cloud.job.worker.common.grpc.strategies;

import com.taotao.cloud.job.common.exception.TtcJobException;
import com.taotao.cloud.job.worker.common.constant.TransportTypeEnum;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * StrategyManager
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class StrategyManager {

    public static Map<TransportTypeEnum, GrpcStrategy<?>> strategyMap = new HashMap<>();

    public static <T> void registerCausa( TransportTypeEnum ruleType, GrpcStrategy<?> strategy ) {
        strategyMap.put(ruleType, strategy);
    }

    @SuppressWarnings("unchecked")
    public static <T> GrpcStrategy<T> match( TransportTypeEnum ruleType ) {

        if (!strategyMap.containsKey(ruleType)) {
            throw new TtcJobException("There's no strategy defined for this type: \"");
        }

        return (GrpcStrategy<T>) strategyMap.get(ruleType);
    }
}
