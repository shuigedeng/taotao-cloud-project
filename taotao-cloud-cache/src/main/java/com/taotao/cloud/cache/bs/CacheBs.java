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

package com.taotao.cloud.cache.bs;

import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.cache.api.*;
import com.taotao.cloud.cache.core.Cache;
import com.taotao.cloud.cache.support.evict.CacheEvicts;
import com.taotao.cloud.cache.support.listener.remove.CacheRemoveListeners;
import com.taotao.cloud.cache.support.listener.slow.CacheSlowListeners;
import com.taotao.cloud.cache.support.load.CacheLoads;
import com.taotao.cloud.cache.support.persist.CachePersists;
import com.taotao.cloud.cache.support.proxy.CacheProxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存引导类
 * @author shuigedeng
 * @since 2024.06
 */
public final class CacheBs<K, V> {

    private CacheBs() {}

    /**
     * 创建对象实例
     * @param <K> key
     * @param <V> value
     * @return this
     * @since 2024.06
     */
    public static <K, V> CacheBs<K, V> newInstance() {
        return new CacheBs<>();
    }

    /**
     * map 实现
     */
    private Map<K, V> map = new HashMap<>();

    /**
     * 大小限制
     */
    private int size = Integer.MAX_VALUE;

    /**
     * 驱除策略
     */
    private ICacheEvict<K, V> evict = CacheEvicts.fifo();

    /**
     * 删除监听类
     */
    private final List<ICacheRemoveListener<K, V>> removeListeners =
            CacheRemoveListeners.defaults();

    /**
     * 慢操作监听类
     */
    private final List<ICacheSlowListener> slowListeners = CacheSlowListeners.none();

    /**
     * 加载策略
     */
    private ICacheLoad<K, V> load = CacheLoads.none();

    /**
     * 持久化实现策略
     */
    private ICachePersist<K, V> persist = CachePersists.none();

    /**
     * map 实现
     * @param map map
     * @return this
     * @since 2024.06
     */
    public CacheBs<K, V> map(Map<K, V> map) {
        ArgUtils.notNull(map, "map");

        this.map = map;
        return this;
    }

    /**
     * 设置 size 信息
     * @param size size
     * @return this
     * @since 2024.06
     */
    public CacheBs<K, V> size(int size) {
        ArgUtils.notNegative(size, "size");

        this.size = size;
        return this;
    }

    /**
     * 设置驱除策略
     * @param evict 驱除策略
     * @return this
     * @since 2024.06
     */
    public CacheBs<K, V> evict(ICacheEvict<K, V> evict) {
        ArgUtils.notNull(evict, "evict");

        this.evict = evict;
        return this;
    }

    /**
     * 设置加载
     * @param load 加载
     * @return this
     * @since 2024.06
     */
    public CacheBs<K, V> load(ICacheLoad<K, V> load) {
        ArgUtils.notNull(load, "load");

        this.load = load;
        return this;
    }

    /**
     * 添加删除监听器
     * @param removeListener 监听器
     * @return this
     * @since 2024.06
     */
    public CacheBs<K, V> addRemoveListener(ICacheRemoveListener<K, V> removeListener) {
        ArgUtils.notNull(removeListener, "removeListener");

        this.removeListeners.add(removeListener);
        return this;
    }

    /**
     * 添加慢日志监听器
     * @param slowListener 监听器
     * @return this
     * @since 2024.06
     */
    public CacheBs<K, V> addSlowListener(ICacheSlowListener slowListener) {
        ArgUtils.notNull(slowListener, "slowListener");

        this.slowListeners.add(slowListener);
        return this;
    }

    /**
     * 设置持久化策略
     * @param persist 持久化
     * @return this
     * @since 2024.06
     */
    public CacheBs<K, V> persist(ICachePersist<K, V> persist) {
        this.persist = persist;
        return this;
    }

    /**
     * 构建缓存信息
     * @return 缓存信息
     * @since 2024.06
     */
    public ICache<K, V> build() {
        Cache<K, V> cache = new Cache<>();
        cache.map(map);
        cache.evict(evict);
        cache.sizeLimit(size);
        cache.removeListeners(removeListeners);
        cache.load(load);
        cache.persist(persist);
        cache.slowListeners(slowListeners);

        // 初始化
        cache.init();
        return CacheProxy.getProxy(cache);
    }
}
