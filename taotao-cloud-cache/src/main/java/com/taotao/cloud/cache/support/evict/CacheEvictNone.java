package com.taotao.cloud.cache.support.evict;

import com.taotao.cloud.cache.api.ICacheEntry;
import com.taotao.cloud.cache.api.ICacheEvict;
import com.taotao.cloud.cache.api.ICacheEvictContext;

/**
 * 丢弃策略
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheEvictNone<K,V> extends AbstractCacheEvict<K,V> {

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        return null;
    }

}
