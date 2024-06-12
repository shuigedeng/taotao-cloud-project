package com.taotao.cloud.cache.support.load;

import com.taotao.cloud.cache.api.ICacheLoad;

/**
 *
 * 加载策略工具类
 * @author shuigedeng
 * @since 2024.06
 */
public final class CacheLoads {

    private CacheLoads(){}

    /**
     * 无加载
     * @param <K> key
     * @param <V> value
     * @return 值
     * @since 2024.06
     */
    public static <K,V> ICacheLoad<K,V> none() {
        return new CacheLoadNone<>();
    }

    /**
     * 文件 JSON
     * @param dbPath 文件路径
     * @param <K> key
     * @param <V> value
     * @return 值
     * @since 2024.06
     */
    public static <K,V> ICacheLoad<K,V> dbJson(final String dbPath) {
        return new CacheLoadDbJson<>(dbPath);
    }

    /**
     * AOF 文件加载模式
     * @param dbPath 文件路径
     * @param <K> key
     * @param <V> value
     * @return 值
     * @since 2024.06
     */
    public static <K,V> ICacheLoad<K,V> aof(final String dbPath) {
        return new CacheLoadAof<>(dbPath);
    }

}
