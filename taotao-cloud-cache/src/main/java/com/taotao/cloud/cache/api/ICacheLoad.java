package com.taotao.cloud.cache.api;

/**
 * 缓存接口
 * @author shuigedeng
 * @since 2024.06
 * @param <K> key
 * @param <V> value
 */
public interface ICacheLoad<K, V> {

    /**
     * 加载缓存信息
     * @param cache 缓存
     * @since 2024.06
     */
    void load(final ICache<K,V> cache);

}
