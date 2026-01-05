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

package com.taotao.cloud.cache.support.interceptor.evict;

import com.taotao.cloud.cache.api.CacheEvict;
import com.taotao.cloud.cache.api.CacheInterceptor;
import com.taotao.cloud.cache.api.CacheInterceptorContext;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 驱除策略拦截器
 *
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheInterceptorEvict<K, V> implements CacheInterceptor<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheInterceptorEvict.class);

    @Override
    public void before( CacheInterceptorContext<K, V> context) {}

    @Override
    @SuppressWarnings("all")
    public void after( CacheInterceptorContext<K, V> context) {
        CacheEvict<K, V> evict = context.cache().evict();

        Method method = context.method();
        final K key = (K) context.params()[0];
        if ("remove".equals(method.getName())) {
            evict.removeKey(key);
        } else {
            evict.updateKey(key);
        }
    }
}
