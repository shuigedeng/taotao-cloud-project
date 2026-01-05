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

package com.taotao.cloud.cache.api;

/**
 * 删除监听器上下文
 *
 * （1）耗时统计
 * （2）监听器
 *
 * @author shuigedeng
 * @since 2024.06
 * @param <K> key
 * @param <V> value
 */
public interface CacheRemoveListenerContext<K, V> {

    /**
     * 清空的 key
     * @return key
     * @since 2024.06
     */
    K key();

    /**
     * 值
     * @return 值
     * @since 2024.06
     */
    V value();

    /**
     * 删除类型
     * @return 类型
     * @since 2024.06
     */
    String type();
}
