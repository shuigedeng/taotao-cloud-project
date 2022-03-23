/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.common.utils.collection;


import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.support.handler.IMapEntryHandler;
import com.taotao.cloud.common.support.handler.IMapHandler;
import com.taotao.cloud.common.utils.guava.Guavas;
import com.taotao.cloud.common.utils.lang.ObjectUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map 工具类
 */
public final class MapUtil {

	private MapUtil() {
	}

	private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

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
	 */
	public static <K, V> Map<K, V> toMap(Collection<V> values, IHandler<? super V, K> keyFunction) {
		if (ObjectUtil.isNull(values)) {
			return Collections.emptyMap();
		}

		Map<K, V> map = new HashMap<>(values.size());

		for (V value : values) {
			final K key = keyFunction.handle(value);
			map.put(key, value);
		}
		return map;
	}

	/**
	 * 可遍历的结合转换为 map
	 *
	 * @param values     可遍历的元素 Iterator
	 * @param mapHandler map 转化方式
	 * @param <K>        key 泛型
	 * @param <V>        value 泛型
	 * @param <O>        原始泛型
	 * @return map 结果
	 */
	public static <K, V, O> Map<K, V> toMap(Collection<O> values, IMapHandler<K, V, O> mapHandler) {
		if (ObjectUtil.isNull(values)) {
			return Collections.emptyMap();
		}

		Map<K, V> map = new HashMap<>(values.size());
		for (O line : values) {
			final K key = mapHandler.getKey(line);
			final V value = mapHandler.getValue(line);
			map.put(key, value);
		}
		return map;
	}

	/**
	 * 可遍历的结合转换为 map
	 *
	 * @param map          可遍历的元素 Iterator
	 * @param entryHandler entry 转化方式
	 * @param <K>          key 泛型
	 * @param <V>          value 泛型
	 * @param <T>          目标泛型
	 * @return list 结果
	 */
	public static <K, V, T> List<T> toList(Map<K, V> map, IMapEntryHandler<K, V, T> entryHandler) {
		if (MapUtil.isEmpty(map)) {
			return Collections.emptyList();
		}

		List<T> resultList = Guavas.newArrayList(map.size());
		for (Map.Entry<K, V> entry : map.entrySet()) {
			final T result = entryHandler.handler(entry);
			resultList.add(result);
		}
		return resultList;
	}

	/**
	 * key 是元素的索引
	 *
	 * @param values 值
	 * @param <V>    元素泛型
	 * @return 结果 map
	 */
	public static <V> Map<Integer, V> toIndexMap(Collection<V> values) {
		if (ObjectUtil.isNull(values)) {
			return Collections.emptyMap();
		}

		Map<Integer, V> map = new HashMap<>(values.size());

		for (V v : values) {
			map.put(map.size(), v);
		}
		return map;
	}

	/**
	 * 获取对应的映射信息 （1）如果对应的值不存在，则返回 key 本身 （2）如果 map 为空，则返回
	 *
	 * @param map map
	 * @param key key
	 * @return value
	 */
	public static String getMapValue(final Map<String, String> map,
		final String key) {
		if (MapUtil.isEmpty(map)) {
			return key;
		}
		final String value = map.get(key);
		if (StringUtil.isEmpty(value)) {
			return key;
		}
		return value;
	}

	/**
	 * 获取对应的映射信息 1. 不存在则返回默认值
	 *
	 * @param map          map
	 * @param key          key
	 * @param defaultValue 默认值
	 * @param <K>          泛型 key
	 * @param <V>          泛型 value
	 * @return value
	 */
	public static <K, V> V getMapValue(final Map<K, V> map, final K key,
		final V defaultValue) {
		if (MapUtil.isEmpty(map)) {
			return defaultValue;
		}
		final V value = map.get(key);
		if (ObjectUtil.isNull(value)) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * 获取 map 的第一个值
	 *
	 * @param map map
	 * @return 结果
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
	 * map转字符串，转换后的字符串格式为 {@code xxx=xxx&xxx=xxx}
	 *
	 * @param params 待转换的map
	 * @param encode 是否转码
	 * @return  转换后的字符串
	 */
	public static String parseMapToString(Map<String, String> params, boolean encode) {
		if (null == params || params.isEmpty()) {
			return "";
		}
		List<String> paramList = new ArrayList<>();
		params.forEach((k, v) -> {
			if (null == v) {
				paramList.add(k + "=");
			} else {
				paramList.add(k + "=" + (encode ? urlEncode(v) : v));
			}
		});
		return String.join("&", paramList);
	}

	/**
	 * 编码
	 *
	 * @param value str
	 * @return encode str
	 */
	public static String urlEncode(String value) {
		if (value == null) {
			return "";
		}
		try {
			String encoded = URLEncoder.encode(value, DEFAULT_ENCODING.displayName());
			return encoded.replace("+", "%20").replace("*", "%2A").replace("~", "%7E")
				.replace("/", "%2F");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Failed To Encode Uri", e);
		}
	}

}
