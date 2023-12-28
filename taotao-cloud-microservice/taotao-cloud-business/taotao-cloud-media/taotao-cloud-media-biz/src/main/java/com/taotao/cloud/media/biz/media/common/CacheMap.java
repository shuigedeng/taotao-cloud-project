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

package com.taotao.cloud.media.biz.media.common;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用来存储短暂对象的缓存类，实现Map接口，内部有一个定时器用来清除过期（30秒）的对象。 为避免创建过多线程，没有特殊要求请使用getDefault()方法来获取本类的实例。
 *
 * @param <K>
 * @param <V>
 */
public class CacheMap<K, V> extends AbstractMap<K, V> {

    private static final long DEFAULT_TIMEOUT = 30000;
    private static CacheMap<Object, Object> defaultInstance;

    public static final synchronized CacheMap<Object, Object> getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new CacheMap<Object, Object>(DEFAULT_TIMEOUT);
        }
        return defaultInstance;
    }

    private class CacheEntry implements Entry<K, V> {

        long time;
        V value;
        K key;

        CacheEntry(K key, V value) {
            super();
            this.value = value;
            this.key = key;
            this.time = System.currentTimeMillis();
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return this.value = value;
        }
    }

    private class ClearThread extends Thread {

        ClearThread() {
            setName("clear cache thread");
        }

        public void run() {
            while (true) {
                try {
                    long now = System.currentTimeMillis();
                    Object[] keys = map.keySet().toArray();
                    for (Object key : keys) {
                        CacheEntry entry = map.get(key);
                        if (now - entry.time >= cacheTimeout) {
                            synchronized (map) {
                                map.remove(key);
                            }
                        }
                    }
                    Thread.sleep(cacheTimeout);
                } catch (Exception e) {
                    LogUtils.error(e);
                }
            }
        }
    }

    private long cacheTimeout;
    private Map<K, CacheEntry> map = new HashMap<K, CacheEntry>();

    public CacheMap(long timeout) {
        this.cacheTimeout = timeout;
        new ClearThread().start();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new HashSet<Entry<K, V>>();
        Set<Entry<K, CacheEntry>> wrapEntrySet = map.entrySet();
        for (Entry<K, CacheEntry> entry : wrapEntrySet) {
            entrySet.add(entry.getValue());
        }
        return entrySet;
    }

    @Override
    public V get(Object key) {
        CacheEntry entry = map.get(key);
        return entry == null ? null : entry.value;
    }

    @Override
    public V put(K key, V value) {
        CacheEntry entry = new CacheEntry(key, value);
        synchronized (map) {
            map.put(key, entry);
        }
        return value;
    }
}
