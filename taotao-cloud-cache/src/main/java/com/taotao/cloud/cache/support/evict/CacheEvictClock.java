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
import com.taotao.cloud.cache.api.CacheEntry;
import com.taotao.cloud.cache.api.CacheEvictContext;
import com.taotao.cloud.cache.support.struct.lru.LruMap;
import com.taotao.cloud.cache.support.struct.lru.impl.LruMapCircleList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 淘汰策略-clock 算法
 *
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheEvictClock<K, V> extends AbstractCacheEvict<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheEvictClock.class);

    /**
     * 循环链表
     * @since 2024.06
     */
    private final LruMap<K, V> circleList;

    public CacheEvictClock() {
        this.circleList = new LruMapCircleList<>();
    }

    @Override
    protected CacheEntry<K, V> doEvict( CacheEvictContext<K, V> context) {
        CacheEntry<K, V> result = null;
        final Cache<K, V> cache = context.cache();
        // 超过限制，移除队尾的元素
        if (cache.size() >= context.size()) {
            CacheEntry<K, V> evictEntry = circleList.removeEldest();
            ;
            // 执行缓存移除操作
            final K evictKey = evictEntry.key();
            V evictValue = cache.remove(evictKey);

            log.debug("基于 clock 算法淘汰 key：{}, value: {}", evictKey, evictValue);
            result = new com.taotao.cloud.cache.model.CacheEntry<>(evictKey, evictValue);
        }

        return result;
    }

    /**
     * 更新信息
     * @param key 元素
     * @since 2024.06
     */
    @Override
    public void updateKey(final K key) {
        this.circleList.updateKey(key);
    }

    /**
     * 移除元素
     *
     * @param key 元素
     * @since 2024.06
     */
    @Override
    public void removeKey(final K key) {
        this.circleList.removeKey(key);
    }
}
