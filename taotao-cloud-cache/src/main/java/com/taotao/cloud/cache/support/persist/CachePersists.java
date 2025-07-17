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

package com.taotao.cloud.cache.support.persist;

import com.taotao.cloud.cache.api.ICachePersist;

/**
 * 缓存持久化工具类
 * @author shuigedeng
 * @since 2024.06
 */
public final class CachePersists {

    private CachePersists() {}

    /**
     * 无操作
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 2024.06
     */
    public static <K, V> ICachePersist<K, V> none() {
        return new CachePersistNone<>();
    }

    /**
     * DB json 操作
     * @param <K> key
     * @param <V> value
     * @param path 文件路径
     * @return 结果
     * @since 2024.06
     */
    public static <K, V> ICachePersist<K, V> dbJson(final String path) {
        return new CachePersistDbJson<>(path);
    }

    /**
     * AOF 持久化
     * @param <K> key
     * @param <V> value
     * @param path 文件路径
     * @return 结果
     * @since 2024.06
     */
    public static <K, V> ICachePersist<K, V> aof(final String path) {
        return new CachePersistAof<>(path);
    }
}
