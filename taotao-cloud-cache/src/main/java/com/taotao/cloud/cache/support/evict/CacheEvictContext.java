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

/**
 * 驱除策略
 *
 * 1. 新加的 key
 * 2. map 实现
 * 3. 淘汰监听器
 *
 * @author shuigedeng
 * @since 2024.06
 * @param <K> key
 * @param <V> value
 */
public class CacheEvictContext<K, V> implements com.taotao.cloud.cache.api.CacheEvictContext<K, V> {

    /**
     * 新加的 key
     * @since 2024.06
     */
    private K key;

    /**
     * cache 实现
     * @since 2024.06
     */
    private Cache<K, V> cache;

    /**
     * 最大的大小
     * @since 2024.06
     */
    private int size;

    @Override
    public K key() {
        return key;
    }

    public CacheEvictContext<K, V> key(K key) {
        this.key = key;
        return this;
    }

    @Override
    public Cache<K, V> cache() {
        return cache;
    }

    public CacheEvictContext<K, V> cache( Cache<K, V> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public int size() {
        return size;
    }

    public CacheEvictContext<K, V> size(int size) {
        this.size = size;
        return this;
    }
}
