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

package com.taotao.cloud.cache.core;

import com.taotao.cloud.cache.api.ICacheContext;
import com.taotao.cloud.cache.api.ICacheEvict;
import java.util.Map;

/**
 * 缓存上下文
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheContext<K, V> implements ICacheContext<K, V> {

    /**
     * map 信息
     * @since 2024.06
     */
    private Map<K, V> map;

    /**
     * 大小限制
     * @since 2024.06
     */
    private int size;

    /**
     * 驱除策略
     * @since 2024.06
     */
    private ICacheEvict<K, V> cacheEvict;

    @Override
    public Map<K, V> map() {
        return map;
    }

    public CacheContext<K, V> map(Map<K, V> map) {
        this.map = map;
        return this;
    }

    @Override
    public int size() {
        return size;
    }

    public CacheContext<K, V> size(int size) {
        this.size = size;
        return this;
    }

    @Override
    public ICacheEvict<K, V> cacheEvict() {
        return cacheEvict;
    }

    public CacheContext<K, V> cacheEvict(ICacheEvict<K, V> cacheEvict) {
        this.cacheEvict = cacheEvict;
        return this;
    }
}
