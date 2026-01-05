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

package com.taotao.cloud.cache.support.listener.remove;

/**
 * 删除的监听器
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheRemoveListenerContext<K, V> implements com.taotao.cloud.cache.api.CacheRemoveListenerContext<K, V> {

    /**
     * key
     * @since 2024.06
     */
    private K key;

    /**
     * 值
     * @since 2024.06
     */
    private V value;

    /**
     * 删除类型
     * @since 2024.06
     */
    private String type;

    /**
     * 新建实例
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 2024.06
     */
    public static <K, V> CacheRemoveListenerContext<K, V> newInstance() {
        return new CacheRemoveListenerContext<>();
    }

    @Override
    public K key() {
        return key;
    }

    public CacheRemoveListenerContext<K, V> key(K key) {
        this.key = key;
        return this;
    }

    @Override
    public V value() {
        return value;
    }

    public CacheRemoveListenerContext<K, V> value(V value) {
        this.value = value;
        return this;
    }

    @Override
    public String type() {
        return type;
    }

    public CacheRemoveListenerContext<K, V> type(String type) {
        this.type = type;
        return this;
    }
}
