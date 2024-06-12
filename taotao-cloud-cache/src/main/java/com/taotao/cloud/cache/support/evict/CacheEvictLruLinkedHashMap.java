package com.taotao.cloud.cache.support.evict;

import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.api.ICacheEntry;
import com.taotao.cloud.cache.api.ICacheEvict;
import com.taotao.cloud.cache.api.ICacheEvictContext;
import com.taotao.cloud.cache.model.CacheEntry;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 丢弃策略-LRU 最近最少使用
 *
 * 实现方式：LinkedHashMap
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheEvictLruLinkedHashMap<K,V> extends LinkedHashMap<K,V>
    implements ICacheEvict<K,V> {

    private static final Logger LOG = LoggerFactory.getLogger(CacheEvictLruDoubleListMap.class);

    /**
     * 是否移除标识
     * @since 2024.06
     */
    private volatile boolean removeFlag = false;

    /**
     * 最旧的一个元素
     * @since 2024.06
     */
    private transient Map.Entry<K, V> eldest = null;

    public CacheEvictLruLinkedHashMap() {
        super(16, 0.75f, true);
    }

    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> result = null;
        final ICache<K,V> cache = context.cache();
        // 超过限制，移除队尾的元素
        if(cache.size() >= context.size()) {
            removeFlag = true;

            // 执行 put 操作
            super.put(context.key(), null);

            // 构建淘汰的元素
            K evictKey = eldest.getKey();
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
        } else {
            removeFlag = false;
        }

        return result;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        this.eldest = eldest;
        return removeFlag;
    }

    @Override
    public void updateKey(K key) {
        super.put(key, null);
    }

    @Override
    public void removeKey(K key) {
        super.remove(key);
    }

}
