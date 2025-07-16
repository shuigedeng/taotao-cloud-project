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

package com.taotao.cloud.job.server.core.schedule.auxiliary;

import com.taotao.cloud.job.common.enums.TimeExpressionType;

/**
 * @author shuigedeng
 * @since 2022/2/24
 */
public interface TimingStrategyHandler {

    /**
     * 校验表达式
     *
     * @param timeExpression 时间表达式
     */
    void validate(String timeExpression);

    /**
     * 计算下次触发时间
     *
     * @param preTriggerTime 上次触发时间 (not null)
     * @param timeExpression 时间表达式
     * @param startTime      开始时间(include)
     * @param endTime        结束时间(include)
     * @return next trigger time
     */
    Long calculateNextTriggerTime(
            Long preTriggerTime, String timeExpression, Long startTime, Long endTime);

    /**
     * 支持的定时策略
     *
     * @return TimeExpressionType
     */
    TimeExpressionType supportType();
}
