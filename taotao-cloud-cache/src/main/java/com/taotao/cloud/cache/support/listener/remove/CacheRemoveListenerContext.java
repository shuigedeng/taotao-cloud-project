package com.taotao.cloud.cache.support.listener.remove;

import com.taotao.cloud.cache.api.ICacheRemoveListenerContext;

/**
 * 删除的监听器
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheRemoveListenerContext<K,V> implements ICacheRemoveListenerContext<K,V> {

    /**
     * key
     * @since 2024.06
     */
    private K key;

    /**
     * 值
     * @since 2024.06
     */
    private V value;

    /**
     * 删除类型
     * @since 2024.06
     */
    private String type;

    /**
     * 新建实例
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 2024.06
     */
    public static <K,V> CacheRemoveListenerContext<K,V> newInstance() {
        return new CacheRemoveListenerContext<>();
    }

    @Override
    public K key() {
        return key;
    }

    public CacheRemoveListenerContext<K, V> key(K key) {
        this.key = key;
        return this;
    }

    @Override
    public V value() {
        return value;
    }

    public CacheRemoveListenerContext<K, V> value(V value) {
        this.value = value;
        return this;
    }

    @Override
    public String type() {
        return type;
    }

    public CacheRemoveListenerContext<K, V> type(String type) {
        this.type = type;
        return this;
    }
}
