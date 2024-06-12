package com.taotao.cloud.cache.support.evict;

import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.api.ICacheEntry;
import com.taotao.cloud.cache.api.ICacheEvictContext;
import com.taotao.cloud.cache.model.CacheEntry;
import com.taotao.cloud.cache.support.struct.lru.ILruMap;
import com.taotao.cloud.cache.support.struct.lru.impl.LruMapCircleList;

/**
 * 淘汰策略-clock 算法
 *
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheEvictClock<K,V> extends AbstractCacheEvict<K,V> {

    private static final Logger LOG = LoggerFactory.getLogger(CacheEvictClock.class);

    /**
     * 循环链表
     * @since 2024.06
     */
    private final ILruMap<K,V> circleList;

    public CacheEvictClock() {
        this.circleList = new LruMapCircleList<>();
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> result = null;
        final ICache<K,V> cache = context.cache();
        // 超过限制，移除队尾的元素
        if(cache.size() >= context.size()) {
            ICacheEntry<K,V>  evictEntry = circleList.removeEldest();;
            // 执行缓存移除操作
            final K evictKey = evictEntry.key();
            V evictValue = cache.remove(evictKey);

            log.debug("基于 clock 算法淘汰 key：{}, value: {}", evictKey, evictValue);
            result = new CacheEntry<>(evictKey, evictValue);
        }

        return result;
    }


    /**
     * 更新信息
     * @param key 元素
     * @since 2024.06
     */
    @Override
    public void updateKey(final K key) {
        this.circleList.updateKey(key);
    }

    /**
     * 移除元素
     *
     * @param key 元素
     * @since 2024.06
     */
    @Override
    public void removeKey(final K key) {
        this.circleList.removeKey(key);
    }

}
