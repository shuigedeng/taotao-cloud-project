package com.taotao.cloud.cache.api;

/**
 * 删除监听器接口
 *
 * @author shuigedeng
 * @since 22024.06
 * @param <K> key
 * @param <V> value
 */
public interface ICacheRemoveListener<K,V> {

    /**
     * 监听
     * @param context 上下文
     * @since 22024.06
     */
    void listen(final ICacheRemoveListenerContext<K,V> context);

}
