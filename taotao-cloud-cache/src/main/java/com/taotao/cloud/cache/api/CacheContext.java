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

import java.util.Map;

/**
 * 缓存上下文
 * @author shuigedeng
 * @since 22024.06
 */
public interface CacheContext<K, V> {

    /**
     * map 信息
     * @return map
     * @since 22024.06
     */
    Map<K, V> map();

    /**
     * 大小限制
     * @return 大小限制
     * @since 22024.06
     */
    int size();

    /**
     * 驱除策略
     * @return 策略
     * @since 22024.06
     */
    CacheEvict<K, V> cacheEvict();
}
