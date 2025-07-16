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

package com.taotao.cloud.mq.common.retry.core.support.condition;

import com.taotao.cloud.mq.common.retry.api.model.AttemptTime;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.condition.RetryCondition;

/**
 * 根据时间进行重试的抽象类
 * @author shuigedeng
 * @since 0.0.1
 */
public abstract class AbstractTimeRetryCondition implements RetryCondition {

    @Override
    public boolean condition(RetryAttempt retryAttempt) {
        return timeCondition(retryAttempt.time());
    }

    /**
     * 对消耗时间信息进行判断
     * 1. 用户可以判定是执行重试
     * 2. 比如任务执行的时间过长，过者任务执行的时间不在预期的时间范围内
     * @param attemptTime 时间信息
     * @return 对消耗时间信息进行判断
     */
    protected abstract boolean timeCondition(final AttemptTime attemptTime);
}
