package com.taotao.cloud.cache.support.struct.lru;

import com.taotao.cloud.cache.api.ICacheEntry;

/**
 * LRU map 接口
 * @author shuigedeng
 * @since 2024.06
 */
public interface ILruMap<K,V> {

    /**
     * 移除最老的元素
     * @return 移除的明细
     * @since 2024.06
     */
    ICacheEntry<K, V> removeEldest();

    /**
     * 更新 key 的信息
     * @param key key
     * @since 2024.06
     */
    void updateKey(final K key);

    /**
     * 移除对应的 key 信息
     * @param key key
     * @since 2024.06
     */
    void removeKey(final K key);

    /**
     * 是否为空
     * @return 是否
     * @since 2024.06
     */
    boolean isEmpty();

    /**
     * 是否包含元素
     * @param key 元素
     * @return 结果
     * @since 2024.06
     */
    boolean contains(final K key);

}
