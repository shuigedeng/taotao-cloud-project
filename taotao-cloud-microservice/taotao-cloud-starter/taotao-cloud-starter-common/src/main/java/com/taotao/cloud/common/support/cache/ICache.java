package com.taotao.cloud.common.support.cache;

/**
 * 缓存接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:07:29
 */
public interface ICache<K, V> {

    /**
     * 根据 key 获取对应的结果
     * @param key key
     * @return 结果
     */
    V get(K key);

    /**
     * 设置内容
     * @param key key
     * @param value value
     */
    void set(K key, V value);

}
