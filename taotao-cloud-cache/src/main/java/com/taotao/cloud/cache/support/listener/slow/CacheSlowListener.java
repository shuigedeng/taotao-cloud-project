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

package com.taotao.cloud.cache.support.listener.slow;

import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.cache.api.CacheSlowListenerContext;
import com.taotao.cloud.cache.support.interceptor.common.CacheInterceptorCost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 慢日志监听类
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheSlowListener implements com.taotao.cloud.cache.api.CacheSlowListener {

    private static final Logger log = LoggerFactory.getLogger(CacheInterceptorCost.class);

    @Override
    public void listen( CacheSlowListenerContext context) {
        log.warn(
                "[Slow] methodName: {}, params: {}, cost time: {}",
                context.methodName(),
                JSON.toJSON(context.params()),
                context.costTimeMills());
    }

    @Override
    public long slowerThanMills() {
        return 1000L;
    }
}
