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
    ICacheEntry<K,V> evict(final ICacheEvictContext<K, V> context);

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
