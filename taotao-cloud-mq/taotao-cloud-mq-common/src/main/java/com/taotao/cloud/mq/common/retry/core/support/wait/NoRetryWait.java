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

import com.taotao.boot.common.support.instance.impl.InstanceFactory;
import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.model.WaitTime;
import com.taotao.cloud.mq.common.retry.api.support.wait.RetryWait;

/**
 * 无时间等待
 * 1. 不是很建议使用这种方式
 * 2. 一般的异常都有时间性，在一定区间内有问题，那就是有问题。
 * @author shuigedeng
 * @since 0.0.1
 */
public class NoRetryWait extends AbstractRetryWait {

    /**
     * 获取一个单例示例
     * @return 单例示例
     */
    public static RetryWait getInstance() {
        return InstanceFactory.getInstance().singleton(NoRetryWait.class);
    }

    @Override
    public WaitTime waitTime(RetryWaitContext retryWaitContext) {
        return super.rangeCorrect(0, retryWaitContext.min(), retryWaitContext.max());
    }
}
