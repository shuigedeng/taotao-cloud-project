package com.taotao.cloud.cache.model;

import java.util.Objects;

/**
 * 包含频率信息的节点
 * @author shuigedeng
 * @since 2024.06
 */
public class FreqNode<K,V> {

    /**
     * 键
     */
    private K key;

    /**
     * 值
     */
    private V value = null;

    /**
     * 频率
     */
    private int frequency = 1;

    public FreqNode(K key) {
        this.key = key;
    }

    public K key() {
        return key;
    }

    public FreqNode<K, V> key(K key) {
        this.key = key;
        return this;
    }

    public V value() {
        return value;
    }

    public FreqNode<K, V> value(V value) {
        this.value = value;
        return this;
    }

    public int frequency() {
        return frequency;
    }

    public FreqNode<K, V> frequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    @Override
    public String toString() {
        return "FreqNode{" +
                "key=" + key +
                ", value=" + value +
                ", frequency=" + frequency +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FreqNode<?, ?> freqNode = (FreqNode<?, ?>) o;
        return frequency == freqNode.frequency &&
                Objects.equals(key, freqNode.key) &&
                Objects.equals(value, freqNode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, frequency);
    }
}
