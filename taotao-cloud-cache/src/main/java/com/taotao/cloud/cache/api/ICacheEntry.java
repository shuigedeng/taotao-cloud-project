package com.taotao.cloud.cache.api;

/**
 * 缓存明细信息
 * @author shuigedeng
 * @since 22024.06
 * @param <K> key
 * @param <V> value
 */
public interface ICacheEntry<K, V> {

    /**
     * @since 22024.06
     * @return key
     */
    K key();

    /**
     * @since 22024.06
     * @return value
     */
    V value();

}
