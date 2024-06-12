package com.taotao.cloud.cache.core;

import com.taotao.cloud.cache.api.ICacheContext;
import com.taotao.cloud.cache.api.ICacheEvict;

import java.util.Map;

/**
 * 缓存上下文
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheContext<K,V> implements ICacheContext<K, V> {

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
    private ICacheEvict<K,V> cacheEvict;

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
