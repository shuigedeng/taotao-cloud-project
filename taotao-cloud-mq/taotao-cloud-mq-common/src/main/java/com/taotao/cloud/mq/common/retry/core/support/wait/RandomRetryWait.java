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
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机等待策略
 * @author shuigedeng
 * @since 0.0.1
 */
public class RandomRetryWait extends AbstractRetryWait {

    @Override
    public WaitTime waitTime(RetryWaitContext retryWaitContext) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long time =
                random.nextLong(
                        retryWaitContext.min(), retryWaitContext.max() - retryWaitContext.min());
        return super.rangeCorrect(time, retryWaitContext.min(), retryWaitContext.max());
    }
}
