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
import com.taotao.cloud.cache.api.CacheEvict;
import com.taotao.cloud.cache.api.CacheEvictContext;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 丢弃策略-LRU 最近最少使用
 *
 * 实现方式：LinkedHashMap
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheEvictLruLinkedHashMap<K, V> extends LinkedHashMap<K, V>
        implements CacheEvict<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheEvictLruDoubleListMap.class);

    /**
     * 是否移除标识
     * @since 2024.06
     */
    private volatile boolean removeFlag = false;

    /**
     * 最旧的一个元素
     * @since 2024.06
     */
    private transient Map.Entry<K, V> eldest = null;

    public CacheEvictLruLinkedHashMap() {
        super(16, 0.75f, true);
    }

    @Override
    public CacheEntry<K, V> evict( CacheEvictContext<K, V> context) {
        CacheEntry<K, V> result = null;
        final Cache<K, V> cache = context.cache();
        // 超过限制，移除队尾的元素
        if (cache.size() >= context.size()) {
            removeFlag = true;

            // 执行 put 操作
            super.put(context.key(), null);

            // 构建淘汰的元素
            K evictKey = eldest.getKey();
            V evictValue = cache.remove(evictKey);
            result = new com.taotao.cloud.cache.model.CacheEntry<>(evictKey, evictValue);
        } else {
            removeFlag = false;
        }

        return result;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        this.eldest = eldest;
        return removeFlag;
    }

    @Override
    public void updateKey(K key) {
        super.put(key, null);
    }

    @Override
    public void removeKey(K key) {
        super.remove(key);
    }
}
