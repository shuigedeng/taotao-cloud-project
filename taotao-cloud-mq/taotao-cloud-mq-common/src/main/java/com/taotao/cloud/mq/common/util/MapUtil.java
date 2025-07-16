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

package com.taotao.cloud.mq.common.util;

import com.taotao.boot.common.utils.lang.ObjectUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import java.util.*;

/**
 * Map 工具类
 *
 * @author bbhou
 * @since 0.0.1
 */
public final class MapUtil {

    private MapUtil() {}

    /**
     * 判断map为空
     *
     * @param map map
     * @return {@code true} 为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || 0 == map.size();
    }

    /**
     * 判断map为非空
     *
     * @param map map
     * @return {@code true} 非空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 可遍历的结合转换为 map
     *
     * @param values      可遍历的元素 Iterator
     * @param keyFunction 转化方式
     * @param <K>         key 泛型
     * @param <V>         value 泛型
     * @return map 结果
     * @since 0.1.7
     */
    // public static <K, V> Map<K, V> toMap(Collection<V> values, IHandler<? super V, K>
    // keyFunction) {
    //    if (ObjectUtil.isNull(values)) {
    //        return Collections.emptyMap();
    //    }
    //
    //    Map<K, V> map = new HashMap<>(values.size());
    //
    //    for (V value : values) {
    //        final K key = keyFunction.handle(value);
    //        map.put(key, value);
    //    }
    //    return map;
    // }

    /**
     * 可遍历的结合转换为 map
     *
     * @param values     可遍历的元素 Iterator
     * @param mapHandler map 转化方式
     * @param <K>        key 泛型
     * @param <V>        value 泛型
     * @param <O>        原始泛型
     * @return map 结果
     * @since 0.1.83
     */
    // public static <K, V, O> Map<K, V> toMap(Collection<O> values, IMapHandler<K, V, O>
    // mapHandler) {
    //    if (ObjectUtil.isNull(values)) {
    //        return Collections.emptyMap();
    //    }
    //
    //    Map<K, V> map = new HashMap<>(values.size());
    //    for (O line : values) {
    //        final K key = mapHandler.getKey(line);
    //        final V value = mapHandler.getValue(line);
    //        map.put(key, value);
    //    }
    //    return map;
    // }

    /**
     * 可遍历的结合转换为 map
     *
     * @param map          可遍历的元素 Iterator
     * @param entryHandler entry 转化方式
     * @param <K>          key 泛型
     * @param <V>          value 泛型
     * @param <T>          目标泛型
     * @return list 结果
     * @since 0.1.85
     */
    // public static <K, V, T> List<T> toList(Map<K, V> map, IMapEntryHandler<K, V, T> entryHandler)
    // {
    //    if (MapUtil.isEmpty(map)) {
    //        return Collections.emptyList();
    //    }
    //
    //    List<T> resultList = Guavas.newArrayList(map.size());
    //    for (Map.Entry<K, V> entry : map.entrySet()) {
    //        final T result = entryHandler.handler(entry);
    //        resultList.add(result);
    //    }
    //    return resultList;
    // }

    /**
     * key 是元素的索引
     *
     * @param values 值
     * @param <V>    元素泛型
     * @return 结果 map
     */
    public static <V> Map<Integer, V> toIndexMap(Collection<V> values) {
        if (ObjectUtils.isNull(values)) {
            return Collections.emptyMap();
        }

        Map<Integer, V> map = new HashMap<>(values.size());

        for (V v : values) {
            map.put(map.size(), v);
        }
        return map;
    }

    /**
     * 获取对应的映射信息
     * （1）如果对应的值不存在，则返回 key 本身
     * （2）如果 map 为空，则返回
     *
     * @param map map
     * @param key key
     * @return value
     * @since 0.1.17
     */
    public static String getMapValue(final Map<String, String> map, final String key) {
        if (MapUtil.isEmpty(map)) {
            return key;
        }
        final String value = map.get(key);
        if (StringUtils.isEmpty(value)) {
            return key;
        }
        return value;
    }

    /**
     * 获取对应的映射信息
     * 1. 不存在则返回默认值
     *
     * @param map map
     * @param key key
     * @param defaultValue 默认值
     * @param <K> 泛型 key
     * @param <V> 泛型 value
     * @return value
     * @since 0.1.93
     */
    public static <K, V> V getMapValue(final Map<K, V> map, final K key, final V defaultValue) {
        if (MapUtil.isEmpty(map)) {
            return defaultValue;
        }
        final V value = map.get(key);
        if (ObjectUtils.isNull(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取 map 的第一个值
     * @param map map
     * @return 结果
     * @since 0.1.111
     */
    public static Map.Entry<String, Object> getFirstEntry(final Map<String, Object> map) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            return entry;
        }

        return null;
    }

    /**
     * 元素放置到 map 中
     * @param listMap 列表
     * @param key 键
     * @param elem 元素
     * @param <T> 泛型
     * @since 0.1.161
     */
    public static <T> void putToListMap(Map<String, List<T>> listMap, String key, T elem) {
        List<T> list = listMap.get(key);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(elem);

        listMap.put(key, list);
    }

    /**
     * 元素放置到 map 中
     * @param setMap 列表
     * @param key 键
     * @param elem 元素
     * @param <T> 泛型
     * @since 0.1.161
     */
    public static <T> void putToSetMap(Map<String, Set<T>> setMap, String key, T elem) {
        Set<T> set = setMap.get(key);
        if (set == null) {
            set = new HashSet<>();
        }
        set.add(elem);

        setMap.put(key, set);
    }
}
