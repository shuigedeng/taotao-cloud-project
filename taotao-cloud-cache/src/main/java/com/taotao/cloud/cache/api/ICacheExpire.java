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

import java.util.Collection;

/**
 * 缓存过期接口
 * @author shuigedeng
 * @since 2024.06
 */
public interface ICacheExpire<K, V> {

    /**
     * 指定过期信息
     * @param key key
     * @param expireAt 什么时候过期
     * @since 2024.06
     */
    void expire(final K key, final long expireAt);

    /**
     * 惰性删除中需要处理的 keys
     * @param keyList keys
     * @since 2024.06
     */
    void refreshExpire(final Collection<K> keyList);

    /**
     * 待过期的 key
     * 不存在，则返回 null
     * @param key 待过期的 key
     * @return 结果
     * @since 2024.06
     */
    Long expireTime(final K key);
}
