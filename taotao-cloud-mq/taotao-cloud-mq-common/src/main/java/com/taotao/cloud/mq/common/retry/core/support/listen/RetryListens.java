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

package com.taotao.cloud.mq.common.retry.core.support.listen;

import com.taotao.boot.common.support.instance.impl.InstanceFactory;
import com.taotao.boot.common.support.pipeline.Pipeline;
import com.taotao.boot.common.utils.collection.ArrayUtils;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.listen.RetryListen;

/**
 * 监听器工具类
 * @author shuigedeng
 * @since 0.0.2
 */
public final class RetryListens {

    private RetryListens() {}

    /**
     * 不执行任何监听
     * @return 不执行任何监听
     */
    public static RetryListen noListen() {
        return InstanceFactory.getInstance().singleton(NoRetryListen.class);
    }

    /**
     * 指定多个监听器
     * @param retryListens 监听器信息
     * @return 结果
     */
    public static RetryListen listens(final RetryListen... retryListens) {
        if (ArrayUtils.isEmpty(retryListens)) {
            return noListen();
        }
        return new AbstractRetryListenInit() {
            @Override
            protected void init(Pipeline<RetryListen> pipeline, RetryAttempt attempt) {
                for (RetryListen retryListen : retryListens) {
                    pipeline.addLast(retryListen);
                }
            }
        };
    }
}
