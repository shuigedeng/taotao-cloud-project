package com.taotao.cloud.cache.model;

/**
 * 持久化明细
 * @author shuigedeng
 * @since 2024.06
 */
public class PersistRdbEntry<K,V> {

    /**
     * key
     * @since 2024.06
     */
    private K key;

    /**
     * value
     * @since 2024.06
     */
    private V value;

    /**
     * expire
     * @since 2024.06
     */
    private Long expire;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }
}
