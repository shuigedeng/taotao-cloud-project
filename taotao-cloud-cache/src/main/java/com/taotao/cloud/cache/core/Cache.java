/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.cache.core;

import com.taotao.boot.common.utils.lang.ObjectUtils;
import com.taotao.cloud.cache.annotation.CacheInterceptor;
import com.taotao.cloud.cache.api.*;
import com.taotao.cloud.cache.constant.enums.CacheRemoveType;
import com.taotao.cloud.cache.exception.CacheRuntimeException;
import com.taotao.cloud.cache.support.evict.CacheEvictContext;
import com.taotao.cloud.cache.support.persist.InnerCachePersist;
import com.taotao.cloud.cache.support.proxy.DefaultCacheProxy;
import java.util.*;

/**
 * 缓存信息
 *
 * @author shuigedeng
 * @param <K> key
 * @param <V> value
 * @since 2024.06
 */
public class Cache<K, V> implements com.taotao.cloud.cache.api.Cache<K, V> {

    /**
     * map 信息
     * @since 2024.06
     */
    private Map<K, V> map;

    /**
     * 大小限制
     * @since 2024.06
     */
    private int sizeLimit;

    /**
     * 驱除策略
     * @since 2024.06
     */
    private CacheEvict<K, V> evict;

    /**
     * 过期策略
     * 暂时不做暴露
     * @since 2024.06
     */
    private CacheExpire<K, V> expire;

    /**
     * 删除监听类
     * @since 2024.06
     */
    private List<CacheRemoveListener<K, V>> removeListeners;

    /**
     * 慢日志监听类
     * @since 2024.06
     */
    private List<CacheSlowListener> slowListeners;

    /**
     * 加载类
     * @since 2024.06
     */
    private CacheLoad<K, V> load;

    /**
     * 持久化
     * @since 2024.06
     */
    private CachePersist<K, V> persist;

    /**
     * 设置 map 实现
     * @param map 实现
     * @return this
     */
    public Cache<K, V> map(Map<K, V> map) {
        this.map = map;
        return this;
    }

    /**
     * 设置大小限制
     * @param sizeLimit 大小限制
     * @return this
     */
    public Cache<K, V> sizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }

    /**
     * 设置驱除策略
     * @param cacheEvict 驱除策略
     * @return this
     * @since 2024.06
     */
    public Cache<K, V> evict( CacheEvict<K, V> cacheEvict) {
        this.evict = cacheEvict;
        return this;
    }

    /**
     * 获取持久化类
     * @return 持久化类
     * @since 2024.06
     */
    @Override
    public CachePersist<K, V> persist() {
        return persist;
    }

    /**
     * 获取驱除策略
     * @return 驱除策略
     * @since 2024.06
     */
    @Override
    public CacheEvict<K, V> evict() {
        return this.evict;
    }

    /**
     * 设置持久化策略
     * @param persist 持久化
     * @since 2024.06
     */
    public void persist( CachePersist<K, V> persist) {
        this.persist = persist;
    }

    @Override
    public List<CacheRemoveListener<K, V>> removeListeners() {
        return removeListeners;
    }

    public Cache<K, V> removeListeners(List<CacheRemoveListener<K, V>> removeListeners) {
        this.removeListeners = removeListeners;
        return this;
    }

    @Override
    public List<CacheSlowListener> slowListeners() {
        return slowListeners;
    }

    public Cache<K, V> slowListeners(List<CacheSlowListener> slowListeners) {
        this.slowListeners = slowListeners;
        return this;
    }

    @Override
    public CacheLoad<K, V> load() {
        return load;
    }

    public Cache<K, V> load( CacheLoad<K, V> load) {
        this.load = load;
        return this;
    }

    /**
     * 初始化
     * @since 2024.06
     */
    public void init() {
        this.expire = new com.taotao.cloud.cache.support.expire.CacheExpire<>(this);
        this.load.load(this);

        // 初始化持久化
        if (this.persist != null) {
            new InnerCachePersist<>(this, persist);
        }
    }

    /**
     * 设置过期时间
     * @param key         key
     * @param timeInMills 毫秒时间之后过期
     * @return this
     */
    @Override
    @CacheInterceptor
    public com.taotao.cloud.cache.api.Cache<K, V> expire(K key, long timeInMills) {
        long expireTime = System.currentTimeMillis() + timeInMills;

        // 使用代理调用
        Cache<K, V> cachePoxy = (Cache<K, V>) DefaultCacheProxy.getProxy(this);
        return cachePoxy.expireAt(key, expireTime);
    }

    /**
     * 指定过期信息
     * @param key key
     * @param timeInMills 时间戳
     * @return this
     */
    @Override
    @CacheInterceptor(aof = true)
    public com.taotao.cloud.cache.api.Cache<K, V> expireAt(K key, long timeInMills) {
        this.expire.expire(key, timeInMills);
        return this;
    }

    @Override
    @CacheInterceptor
    public CacheExpire<K, V> expire() {
        return this.expire;
    }

    @Override
    @CacheInterceptor(refresh = true)
    public int size() {
        return map.size();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    @CacheInterceptor(refresh = true, evict = true)
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    @CacheInterceptor(evict = true)
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        // 1. 刷新所有过期信息
        K genericKey = (K) key;
        this.expire.refreshExpire(Collections.singletonList(genericKey));

        return map.get(key);
    }

    @Override
    @CacheInterceptor(aof = true, evict = true)
    public V put(K key, V value) {
        // 1.1 尝试驱除
        CacheEvictContext<K, V> context = new CacheEvictContext<>();
        context.key(key).size(sizeLimit).cache(this);

        CacheEntry<K, V> evictEntry = evict.evict(context);

        // 添加拦截器调用
        if (ObjectUtils.isNotNull(evictEntry)) {
            // 执行淘汰监听器
            CacheRemoveListenerContext<K, V> removeListenerContext =
                    com.taotao.cloud.cache.support.listener.remove.CacheRemoveListenerContext.<K, V>newInstance()
                            .key(evictEntry.key())
                            .value(evictEntry.value())
                            .type(CacheRemoveType.EVICT.code());
            for (CacheRemoveListener<K, V> listener : context.cache().removeListeners()) {
                listener.listen(removeListenerContext);
            }
        }

        // 2. 判断驱除后的信息
        if (isSizeLimit()) {
            throw new CacheRuntimeException("当前队列已满，数据添加失败！");
        }

        // 3. 执行添加
        return map.put(key, value);
    }

    /**
     * 是否已经达到大小最大的限制
     * @return 是否限制
     * @since 2024.06
     */
    private boolean isSizeLimit() {
        final int currentSize = this.size();
        return currentSize >= this.sizeLimit;
    }

    @Override
    @CacheInterceptor(aof = true, evict = true)
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    @CacheInterceptor(aof = true)
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    @CacheInterceptor(refresh = true, aof = true)
    public void clear() {
        map.clear();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Collection<V> values() {
        return map.values();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
