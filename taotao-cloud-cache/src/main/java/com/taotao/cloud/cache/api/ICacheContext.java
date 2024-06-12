package com.taotao.cloud.cache.api;

import java.util.Map;

/**
 * 缓存上下文
 * @author shuigedeng
 * @since 22024.06
 */
public interface ICacheContext<K, V> {

    /**
     * map 信息
     * @return map
     * @since 22024.06
     */
    Map<K, V> map();

    /**
     * 大小限制
     * @return 大小限制
     * @since 22024.06
     */
    int size();

    /**
     * 驱除策略
     * @return 策略
     * @since 22024.06
     */
    ICacheEvict<K,V> cacheEvict();

}
