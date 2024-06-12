package com.taotao.cloud.cache.api;

/**
 * 删除监听器上下文
 *
 * （1）耗时统计
 * （2）监听器
 *
 * @author shuigedeng
 * @since 2024.06
 * @param <K> key
 * @param <V> value
 */
public interface ICacheRemoveListenerContext<K,V> {

    /**
     * 清空的 key
     * @return key
     * @since 2024.06
     */
    K key();

    /**
     * 值
     * @return 值
     * @since 2024.06
     */
    V value();

    /**
     * 删除类型
     * @return 类型
     * @since 2024.06
     */
    String type();

}
