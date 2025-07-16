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

package com.taotao.cloud.mq.common.retry.api.support.wait;

import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.model.WaitTime;

/**
 * 重试等待策略
 * 1. 所有的实现必须要有无参构造器，因为会基于反射处理类信息。
 * 2. 尽可能的保证为线程安全的，比如 stateless。
 * @author shuigedeng
 * @since 0.0.1
 */
public interface RetryWait {

    /**
     * 等待时间
     * @param retryWaitContext 上下文信息
     * @return 等待时间的结果信息
     * @since 0.0.3 参数调整
     */
    WaitTime waitTime(final RetryWaitContext retryWaitContext);
}
