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

package com.taotao.cloud.mq.common.retry.core.support.stop;

import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.stop.RetryStop;

/**
 * 最大尝试次数终止策略
 * @author shuigedeng
 * @since 0.0.1
 */
public class MaxAttemptRetryStop implements RetryStop {

    /**
     * 最大重试次数
     * 1. 必须为正整数
     */
    private final int maxAttempt;

    public MaxAttemptRetryStop(int maxAttempt) {
        ArgUtils.positive(maxAttempt, "MaxAttempt");
        this.maxAttempt = maxAttempt;
    }

    @Override
    public boolean stop(RetryAttempt attempt) {
        int times = attempt.attempt();
        return times >= maxAttempt;
    }
}
