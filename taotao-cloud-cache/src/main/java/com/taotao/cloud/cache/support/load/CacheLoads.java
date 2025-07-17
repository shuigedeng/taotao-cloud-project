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

package com.taotao.cloud.cache.support.load;

import com.taotao.cloud.cache.api.ICacheLoad;

/**
 *
 * 加载策略工具类
 * @author shuigedeng
 * @since 2024.06
 */
public final class CacheLoads {

    private CacheLoads() {}

    /**
     * 无加载
     * @param <K> key
     * @param <V> value
     * @return 值
     * @since 2024.06
     */
    public static <K, V> ICacheLoad<K, V> none() {
        return new CacheLoadNone<>();
    }

    /**
     * 文件 JSON
     * @param dbPath 文件路径
     * @param <K> key
     * @param <V> value
     * @return 值
     * @since 2024.06
     */
    public static <K, V> ICacheLoad<K, V> dbJson(final String dbPath) {
        return new CacheLoadDbJson<>(dbPath);
    }

    /**
     * AOF 文件加载模式
     * @param dbPath 文件路径
     * @param <K> key
     * @param <V> value
     * @return 值
     * @since 2024.06
     */
    public static <K, V> ICacheLoad<K, V> aof(final String dbPath) {
        return new CacheLoadAof<>(dbPath);
    }
}
