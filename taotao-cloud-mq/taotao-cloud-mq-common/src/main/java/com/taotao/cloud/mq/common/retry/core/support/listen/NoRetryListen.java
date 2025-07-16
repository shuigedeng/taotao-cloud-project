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
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.listen.RetryListen;

/**
 * 不进行任何监听动作
 * @author shuigedeng
 * @since 0.0.1
 */
public class NoRetryListen implements RetryListen {

    /**
     * 获取单例
     * @return 单例
     */
    public static RetryListen getInstance() {
        return InstanceFactory.getInstance().singleton(NoRetryListen.class);
    }

    @Override
    public <R> void listen(RetryAttempt<R> attempt) {}
}
