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

package com.taotao.cloud.jetcache.enhance;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.support.CachedAnnoConfig;
import com.alicp.jetcache.anno.support.ConfigProvider;

import com.taotao.cloud.common.constant.CommonConstant;
import java.util.concurrent.TimeUnit;

/**
 * JetCache 手动创建Cache 工厂
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-25 08:54:36
 */
public class JetCacheCreateCacheFactory {

    private static final String UNDEFINED = "$$undefined$$";
    private final ConfigProvider configProvider;

    public JetCacheCreateCacheFactory(ConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    public <K, V> Cache<K, V> create(String cacheName, int expire, TimeUnit timeUnit) {
        return create(cacheName, CacheType.BOTH, expire, timeUnit);
    }

    public <K, V> Cache<K, V> create(String cacheName, CacheType cacheType, int expire, TimeUnit timeUnit) {
        return create(CommonConstant.LOWERCASE_DEFAULT, cacheName, cacheType, expire, timeUnit);
    }

    public <K, V> Cache<K, V> create(String cacheName) {
        return create(cacheName, CacheType.BOTH);
    }

    public <K, V> Cache<K, V> create(String cacheName, CacheType cacheType) {
        return create(cacheName, cacheType, Integer.MIN_VALUE);
    }

    public <K, V> Cache<K, V> create(String cacheName, CacheType cacheType, int expire) {
        return create(CommonConstant.LOWERCASE_DEFAULT, cacheName, cacheType, expire);
    }

    public <K, V> Cache<K, V> create(String area, String cacheName, CacheType cacheType, int expire) {
        return create(area, cacheName, cacheType, expire, TimeUnit.SECONDS);
    }

    public <K, V> Cache<K, V> create(String area, String cacheName, CacheType cacheType, int expire, TimeUnit timeUnit) {
        return create(area, cacheName, cacheType, expire, timeUnit, Integer.MIN_VALUE);
    }

    public <K, V> Cache<K, V> create(String area, String cacheName, CacheType cacheType, int expire, TimeUnit timeUnit, int localExpire) {
        return create(area, cacheName, cacheType, expire, timeUnit, localExpire, Integer.MIN_VALUE);
    }

    public <K, V> Cache<K, V> create(String area, String cacheName, CacheType cacheType, int expire, TimeUnit timeUnit, int localExpire, int localLimit) {
        return create(area, cacheName, cacheType, expire, timeUnit, localExpire, localLimit, UNDEFINED, UNDEFINED);
    }

    public <K, V> Cache<K, V> create(String area, String cacheName, CacheType cacheType, int expire, TimeUnit timeUnit, int localExpire, int localLimit, String serialPolicy, String keyConvertor) {
        CachedAnnoConfig cac = new CachedAnnoConfig();
        cac.setArea(area);
        cac.setName(cacheName);
        cac.setCacheType(cacheType);
        cac.setExpire(expire);
        cac.setTimeUnit(timeUnit);
        cac.setLocalExpire(localExpire);
        cac.setLocalLimit(localLimit);
        cac.setSerialPolicy(serialPolicy);
        cac.setKeyConvertor(keyConvertor);
        return create(cac);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> create(CachedAnnoConfig cac) {
        return configProvider.getCacheContext().__createOrGetCache(cac, cac.getArea(), cac.getName());
    }
}
