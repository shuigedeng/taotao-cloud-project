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

package com.taotao.cloud.cache.support.interceptor.refresh;

import com.taotao.cloud.cache.api.Cache;
import com.taotao.cloud.cache.api.CacheInterceptor;
import com.taotao.cloud.cache.api.CacheInterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 刷新
 *
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheInterceptorRefresh<K, V> implements CacheInterceptor<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheInterceptorRefresh.class);

    @Override
    public void before( CacheInterceptorContext<K, V> context) {
        log.debug("Refresh start");
        final Cache<K, V> cache = context.cache();
        cache.expire().refreshExpire(cache.keySet());
    }

    @Override
    public void after( CacheInterceptorContext<K, V> context) {}
}
