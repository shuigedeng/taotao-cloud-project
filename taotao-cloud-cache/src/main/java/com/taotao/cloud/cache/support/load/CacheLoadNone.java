package com.taotao.cloud.cache.support.load;

import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.api.ICacheLoad;

/**
 * 加载策略-无
 * @author shuigedeng
 * @since 0.0.7
 */
public class CacheLoadNone<K,V> implements ICacheLoad<K,V> {

    @Override
    public void load(ICache<K, V> cache) {
        //nothing...
    }

}
