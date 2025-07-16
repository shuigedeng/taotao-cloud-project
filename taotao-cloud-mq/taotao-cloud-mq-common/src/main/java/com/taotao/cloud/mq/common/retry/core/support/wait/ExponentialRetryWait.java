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

package com.taotao.cloud.mq.common.retry.core.support.wait;

import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.model.WaitTime;

/**
 * 指数增长的等待策略
 * 1. 如果因数大于 1 越来越快。
 * 2. 如果因数等于1 保持不变
 * 3. 如果因数大于0，且小于1 。越来越慢
 *
 * 斐波那契数列就是一种乘数接近于：1.618 的黄金递增。
 * 指数等待函数
 * @author shuigedeng
 * @since 0.0.1
 */
public class ExponentialRetryWait extends AbstractRetryWait {

    @Override
    public WaitTime waitTime(RetryWaitContext retryWaitContext) {
        final int previousAttempt = retryWaitContext.attempt() - 1;
        double exp = Math.pow(retryWaitContext.factor(), previousAttempt);
        long result = Math.round(retryWaitContext.value() * exp);

        return super.rangeCorrect(result, retryWaitContext.min(), retryWaitContext.max());
    }
}
