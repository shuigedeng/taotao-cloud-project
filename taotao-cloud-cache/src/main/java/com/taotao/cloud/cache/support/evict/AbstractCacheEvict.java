package com.taotao.cloud.cache.support.evict;

import com.taotao.cloud.cache.api.ICacheEntry;
import com.taotao.cloud.cache.api.ICacheEvict;
import com.taotao.cloud.cache.api.ICacheEvictContext;

/**
 * 丢弃策略-抽象实现类
 * @author shuigedeng
 * @since 2024.06
 */
public abstract class AbstractCacheEvict<K,V> implements ICacheEvict<K,V> {

    @Override
    public ICacheEntry<K,V> evict(ICacheEvictContext<K, V> context) {
        //3. 返回结果
        return doEvict(context);
    }

    /**
     * 执行移除
     * @param context 上下文
     * @return 结果
     * @since 2024.06
     */
    protected abstract ICacheEntry<K,V> doEvict(ICacheEvictContext<K, V> context);

    @Override
    public void updateKey(K key) {

    }

    @Override
    public void removeKey(K key) {

    }
}
