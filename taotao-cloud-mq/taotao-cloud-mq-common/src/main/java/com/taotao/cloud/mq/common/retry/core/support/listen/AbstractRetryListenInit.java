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

import com.taotao.boot.common.support.pipeline.Pipeline;
import com.taotao.boot.common.support.pipeline.impl.DefaultPipeline;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.listen.RetryListen;
import java.util.List;

/**
 * 监听器初始化
 * @author shuigedeng
 * @since 0.0.1
 */
public abstract class AbstractRetryListenInit implements RetryListen {

    @Override
    public <R> void listen(RetryAttempt<R> attempt) {
        Pipeline<RetryListen> pipeline = new DefaultPipeline<>();
        this.init(pipeline, attempt);

        // 执行
        final List<RetryListen> retryListenList = pipeline.list();
        for (RetryListen retryListen : retryListenList) {
            retryListen.listen(attempt);
        }
    }

    /**
     * 初始化监听器列表
     * @param pipeline 泳道
     * @param attempt 重试信息
     */
    protected abstract void init(final Pipeline<RetryListen> pipeline, final RetryAttempt attempt);
}
