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

package com.taotao.cloud.mq.common.retry.api.support.condition;

import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;

/**
 * 重试执行的条件
 *
 * 注意：实现类应该有无参构造函数
 * @author shuigedeng
 * @since 0.0.1
 */
public interface RetryCondition<R> {

    /**
     * 是否满足重试的条件
     * @param retryAttempt 重试相关信息
     * @return 是否
     */
    boolean condition(final RetryAttempt<R> retryAttempt);
}
