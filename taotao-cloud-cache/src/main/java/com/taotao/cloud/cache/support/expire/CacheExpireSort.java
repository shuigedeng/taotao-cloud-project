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

package com.taotao.cloud.cache.support.expire;

import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.api.ICacheExpire;
import com.xkzhangsan.time.utils.CollectionUtil;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.dromara.hutool.core.map.MapUtil;

/**
 * 缓存过期-时间排序策略
 *
 * 优点：定时删除时不用做过多消耗
 * 缺点：惰性删除不友好
 *
 * @author shuigedeng
 * @since 2024.06
 * @param <K> key
 * @param <V> value
 */
public class CacheExpireSort<K, V> implements ICacheExpire<K, V> {

    /**
     * 单次清空的数量限制
     * @since 2024.06
     */
    private static final int LIMIT = 100;

    /**
     * 排序缓存存储
     *
     * 使用按照时间排序的缓存处理。
     * @since 2024.06
     */
    private final Map<Long, List<K>> sortMap =
            new TreeMap<>(
                    new Comparator<Long>() {
                        @Override
                        public int compare(Long o1, Long o2) {
                            return (int) (o1 - o2);
                        }
                    });

    /**
     * 过期 map
     *
     * 空间换时间
     * @since 2024.06
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 缓存实现
     * @since 2024.06
     */
    private final ICache<K, V> cache;

    /**
     * 线程执行类
     * @since 2024.06
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE =
            Executors.newSingleThreadScheduledExecutor();

    public CacheExpireSort(ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    /**
     * 初始化任务
     * @since 2024.06
     */
    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThread(), 100, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 定时执行任务
     * @since 2024.06
     */
    private class ExpireThread implements Runnable {
        @Override
        public void run() {
            // 1.判断是否为空
            if (MapUtil.isEmpty(sortMap)) {
                return;
            }

            // 2. 获取 key 进行处理
            int count = 0;
            for (Map.Entry<Long, List<K>> entry : sortMap.entrySet()) {
                final Long expireAt = entry.getKey();
                List<K> expireKeys = entry.getValue();

                // 判断队列是否为空
                if (CollectionUtil.isEmpty(expireKeys)) {
                    sortMap.remove(expireAt);
                    continue;
                }
                if (count >= LIMIT) {
                    return;
                }

                // 删除的逻辑处理
                long currentTime = System.currentTimeMillis();
                if (currentTime >= expireAt) {
                    Iterator<K> iterator = expireKeys.iterator();
                    while (iterator.hasNext()) {
                        K key = iterator.next();
                        // 先移除本身
                        iterator.remove();
                        expireMap.remove(key);

                        // 再移除缓存，后续可以通过惰性删除做补偿
                        cache.remove(key);

                        count++;
                    }
                } else {
                    // 直接跳过，没有过期的信息
                    return;
                }
            }
        }
    }

    @Override
    public void expire(K key, long expireAt) {
        List<K> keys = sortMap.get(expireAt);
        if (keys == null) {
            keys = new ArrayList<>();
        }
        keys.add(key);

        // 设置对应的信息
        sortMap.put(expireAt, keys);
        expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (CollectionUtil.isEmpty(keyList)) {
            return;
        }

        // 这样维护两套的代价太大，后续优化，暂时不用。
        // 判断大小，小的作为外循环
        final int expireSize = expireMap.size();
        if (expireSize <= keyList.size()) {
            // 一般过期的数量都是较少的
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                K key = entry.getKey();

                // 这里直接执行过期处理，不再判断是否存在于集合中。
                // 因为基于集合的判断，时间复杂度为 O(n)
                this.removeExpireKey(key);
            }
        } else {
            for (K key : keyList) {
                this.removeExpireKey(key);
            }
        }
    }

    /**
     * 移除过期信息
     * @param key key
     * @since 2024.06
     */
    private void removeExpireKey(final K key) {
        Long expireTime = expireMap.get(key);
        if (expireTime != null) {
            final long currentTime = System.currentTimeMillis();
            if (currentTime >= expireTime) {
                expireMap.remove(key);

                List<K> expireKeys = sortMap.get(expireTime);
                expireKeys.remove(key);
                sortMap.put(expireTime, expireKeys);
            }
        }
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }
}
