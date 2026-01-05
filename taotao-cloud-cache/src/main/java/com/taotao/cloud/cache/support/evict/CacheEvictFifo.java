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

package com.taotao.cloud.cache.support.evict;

import com.taotao.cloud.cache.api.Cache;
import com.taotao.cloud.cache.api.CacheEvictContext;
import com.taotao.cloud.cache.model.CacheEntry;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 丢弃策略-先进先出
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheEvictFifo<K, V> extends AbstractCacheEvict<K, V> {

    /**
     * queue 信息
     * @since 2024.06
     */
    private final Queue<K> queue = new LinkedList<>();

    @Override
    public CacheEntry<K, V> doEvict( CacheEvictContext<K, V> context) {
        CacheEntry<K, V> result = null;

        final Cache<K, V> cache = context.cache();
        // 超过限制，执行移除
        if (cache.size() >= context.size()) {
            K evictKey = queue.remove();
            // 移除最开始的元素
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
        }

        // 将新加的元素放入队尾
        final K key = context.key();
        queue.add(key);

        return result;
    }
}
