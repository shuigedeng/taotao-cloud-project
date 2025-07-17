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
 * 驱除策略
 *
 * @author shuigedeng
 * @since 22024.06
 */
public interface ICacheEvict<K, V> {

    /**
     * 驱除策略
     *
     * @param context 上下文
     * @since 2024.06
     * @return 被移除的明细，没有时返回 null
     */
    ICacheEntry<K, V> evict(final ICacheEvictContext<K, V> context);

    /**
     * 更新 key 信息
     * @param key key
     * @since 2024.06
     */
    void updateKey(final K key);

    /**
     * 删除 key 信息
     * @param key key
     * @since 2024.06
     */
    void removeKey(final K key);
}
