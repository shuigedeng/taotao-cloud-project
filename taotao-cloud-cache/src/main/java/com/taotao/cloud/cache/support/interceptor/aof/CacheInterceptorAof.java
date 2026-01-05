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

package com.taotao.cloud.cache.support.interceptor.aof;

import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.cache.api.Cache;
import com.taotao.cloud.cache.api.CacheInterceptor;
import com.taotao.cloud.cache.api.CacheInterceptorContext;
import com.taotao.cloud.cache.api.CachePersist;
import com.taotao.cloud.cache.model.PersistAofEntry;
import com.taotao.cloud.cache.support.persist.CachePersistAof;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 顺序追加模式
 *
 * AOF 持久化到文件，暂时不考虑 buffer 等特性。
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheInterceptorAof<K, V> implements CacheInterceptor<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheInterceptorAof.class);

    @Override
    public void before( CacheInterceptorContext<K, V> context) {}

    @Override
    public void after( CacheInterceptorContext<K, V> context) {
        // 持久化类
        Cache<K, V> cache = context.cache();
        CachePersist<K, V> persist = cache.persist();

        if (persist instanceof CachePersistAof) {
            CachePersistAof<K, V> cachePersistAof = (CachePersistAof<K, V>) persist;

            String methodName = context.method().getName();
            PersistAofEntry aofEntry = PersistAofEntry.newInstance();
            aofEntry.setMethodName(methodName);
            aofEntry.setParams(context.params());

            String json = JSON.toJSONString(aofEntry);

            // 直接持久化
            log.debug("AOF 开始追加文件内容：{}", json);
            cachePersistAof.append(json);
            log.debug("AOF 完成追加文件内容：{}", json);
        }
    }
}
