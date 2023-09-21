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

package com.taotao.cloud.payment.biz.jeepay.jeepay.util;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 不区分大小写转换Map
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public class CaseInsensitiveMap<V> extends AbstractMap<String, V> implements Map<String, V>, Cloneable, Serializable {

    private static final long serialVersionUID = -1121409735905111733L;

    private Map<String, Entry<String, V>> store;

    public CaseInsensitiveMap() {
        this.store = new HashMap<String, Entry<String, V>>();
    }

    public static <V> CaseInsensitiveMap<V> of(Map<String, V> map) {
        if (map == null) {
            return null;
        }
        CaseInsensitiveMap<V> ciMap = new CaseInsensitiveMap<>();
        ciMap.putAll(map);
        return ciMap;
    }

    @Override
    public boolean containsKey(Object key) {
        String keyLower = convertKey(key);
        return this.store.containsKey(keyLower);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.values().contains(value);
    }

    @Override
    public V get(Object key) {
        String keyLower = convertKey(key);
        Entry<String, V> entry = this.store.get(keyLower);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    @Override
    public V put(String key, V value) {
        String keyLower = convertKey(key);
        this.store.put(keyLower, new SimpleEntry<String, V>(key, value));
        return value;
    }

    @Override
    public V remove(Object key) {
        String keyLower = convertKey(key);
        Entry<String, V> entry = this.store.remove(keyLower);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    @Override
    public void clear() {
        this.store.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.store.values().stream().map(entry -> entry.getKey()).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return this.store.values().stream().map(entry -> entry.getValue()).toList();
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return this.store.values().stream().collect(Collectors.toSet());
    }

    private static String convertKey(Object key) {
        if (key == null) {
            return null;
        } else if (key instanceof String) {
            return ((String) key).toLowerCase();
        }
        throw new IllegalArgumentException("key must be a String");
    }
}
