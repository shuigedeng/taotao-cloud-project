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

package com.taotao.cloud.cache.support.interceptor.common;

import com.taotao.cloud.cache.api.ICacheInterceptor;
import com.taotao.cloud.cache.api.ICacheInterceptorContext;
import com.taotao.cloud.cache.api.ICacheSlowListener;
import com.taotao.cloud.cache.support.listener.slow.CacheSlowListenerContext;
import com.xkzhangsan.time.utils.CollectionUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 耗时统计
 *
 * （1）耗时
 * （2）慢日志
 * @author shuigedeng
 * @since 2024.06
 * @param <K> key
 * @param <V> value
 */
public class CacheInterceptorCost<K, V> implements ICacheInterceptor<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheInterceptorCost.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("Cost start, method: {}", context.method().getName());
    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        long costMills = context.endMills() - context.startMills();
        final String methodName = context.method().getName();
        log.debug("Cost end, method: {}, cost: {}ms", methodName, costMills);

        // 添加慢日志操作
        List<ICacheSlowListener> slowListeners = context.cache().slowListeners();
        if (CollectionUtil.isNotEmpty(slowListeners)) {
            CacheSlowListenerContext listenerContext =
                    CacheSlowListenerContext.newInstance()
                            .startTimeMills(context.startMills())
                            .endTimeMills(context.endMills())
                            .costTimeMills(costMills)
                            .methodName(methodName)
                            .params(context.params())
                            .result(context.result());

            // 设置多个，可以考虑不同的慢日志级别，做不同的处理
            for (ICacheSlowListener slowListener : slowListeners) {
                long slowThanMills = slowListener.slowerThanMills();
                if (costMills >= slowThanMills) {
                    slowListener.listen(listenerContext);
                }
            }
        }
    }
}
