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

package com.taotao.cloud.job.server.core.schedule;

import com.taotao.cloud.job.common.enums.TimeExpressionType;
import com.taotao.cloud.job.common.exception.TtcJobException;
import com.taotao.cloud.job.server.core.schedule.auxiliary.TimingStrategyHandler;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author shuigedeng
 * @since 2022/3/21
 */
@Slf4j
@Service
public class TimingStrategyService {

    private static final int NEXT_N_TIMES = 5;

    private static final List<String> TIPS =
            Collections.singletonList("It is valid, but has not trigger time list!");

    private final Map<TimeExpressionType, TimingStrategyHandler> strategyContainer;

    public TimingStrategyService(List<TimingStrategyHandler> timingStrategyHandlers) {
        // init
        strategyContainer = new EnumMap<>(TimeExpressionType.class);
        for (TimingStrategyHandler timingStrategyHandler : timingStrategyHandlers) {
            strategyContainer.put(timingStrategyHandler.supportType(), timingStrategyHandler);
        }
    }

    /**
     * 计算下次的调度时间
     *
     * @param preTriggerTime     上次触发时间(nullable)
     * @param timeExpressionType 定时表达式类型
     * @param timeExpression     表达式
     * @param startTime          起始时间(include)
     * @param endTime            结束时间(include)
     * @return 下次的调度时间
     */
    public Long calculateNextTriggerTime(
            Long preTriggerTime,
            TimeExpressionType timeExpressionType,
            String timeExpression,
            Long startTime,
            Long endTime) {
        if (preTriggerTime == null || preTriggerTime < System.currentTimeMillis()) {
            preTriggerTime = System.currentTimeMillis();
        }
        return getHandler(timeExpressionType)
                .calculateNextTriggerTime(preTriggerTime, timeExpression, startTime, endTime);
    }

    private TimingStrategyHandler getHandler(TimeExpressionType timeExpressionType) {
        TimingStrategyHandler timingStrategyHandler = strategyContainer.get(timeExpressionType);
        if (timingStrategyHandler == null) {
            throw new TtcJobException(
                    "No matching TimingStrategyHandler for this TimeExpressionType:"
                            + timeExpressionType);
        }
        return timingStrategyHandler;
    }
}
