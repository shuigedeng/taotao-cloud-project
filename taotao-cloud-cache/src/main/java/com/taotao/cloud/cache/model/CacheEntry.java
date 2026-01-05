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

package com.taotao.cloud.cache.model;

/**
 * key value 的明细信息
 * @author shuigedeng
 * @since 2024.06
 * @param <K> key
 * @param <V> value
 */
public class CacheEntry<K, V> implements com.taotao.cloud.cache.api.CacheEntry<K, V> {

    /**
     * key
     * @since 2024.06
     */
    private final K key;

    /**
     * value
     * @since 2024.06
     */
    private final V value;

    /**
     * 新建元素
     * @param key key
     * @param value value
     * @param <K> 泛型
     * @param <V> 泛型
     * @return 结果
     * @since 2024.06
     */
    public static <K, V> CacheEntry<K, V> of(final K key, final V value) {
        return new CacheEntry<>(key, value);
    }

    public CacheEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public String toString() {
        return "EvictEntry{" + "key=" + key + ", value=" + value + '}';
    }
}
