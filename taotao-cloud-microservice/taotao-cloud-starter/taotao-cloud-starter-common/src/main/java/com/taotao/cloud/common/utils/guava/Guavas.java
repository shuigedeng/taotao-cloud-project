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
package com.taotao.cloud.common.utils.guava;


import com.taotao.cloud.common.utils.collection.ArrayUtil;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * guava 工具类 1. 只采用很少的集合创建
 */
public final class Guavas {

	private Guavas() {
	}

	/**
	 * 新建数组链表
	 *
	 * @param <E> 元素
	 * @return 链表
	 */
	public static <E> List<E> newArrayList() {
		return new ArrayList<>();
	}

	/**
	 * 新建数组链表
	 *
	 * @param <E>  元素
	 * @param size 大小
	 * @return 链表
	 */
	public static <E> List<E> newArrayList(final int size) {
		return new ArrayList<>(size);
	}

	/**
	 * 新建数组链表
	 *
	 * @param <E>   元素
	 * @param elems 元素数组
	 * @return 链表
	 */
	@SuppressWarnings("unchecked")
	public static <E> List<E> newArrayList(E... elems) {
		if (ArrayUtil.isEmpty(elems)) {
			return new ArrayList<>();
		}
		List<E> list = Guavas.newArrayList(elems.length);

		list.addAll(Arrays.asList(elems));
		return list;
	}

	/**
	 * 新建collection链表
	 *
	 * @param <E>        元素
	 * @param collection 集合元素
	 * @return 链表
	 */
	public static <E> List<E> newArrayList(Collection<E> collection) {
		if (CollectionUtil.isEmpty(collection)) {
			return new ArrayList<>();
		}
		List<E> list = Guavas.newArrayList(collection.size());
		list.addAll(collection);
		return list;
	}

	/**
	 * 新建 hash Map
	 *
	 * @param <K> key
	 * @param <V> value
	 * @return hash Map
	 */
	public static <K, V> Map<K, V> newHashMap() {
		return new HashMap<>();
	}

	/**
	 * 新建 hash Map
	 *
	 * @param <K>  key
	 * @param <V>  value
	 * @param size 大小
	 * @return hash Map
	 */
	public static <K, V> Map<K, V> newHashMap(final int size) {
		return new HashMap<>(size);
	}

	/**
	 * 新建 ConcurrentHashMap
	 *
	 * @param <K> key
	 * @param <V> value
	 * @return hash Map
	 */
	public static <K, V> Map<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<>();
	}

	/**
	 * 新建 LinkedHashMap
	 *
	 * @param <K> key
	 * @param <V> value
	 * @return hash Map
	 */
	public static <K, V> Map<K, V> newLinkedHashMap() {
		return new LinkedHashMap<>();
	}

	/**
	 * 新建 LinkedHashMap
	 *
	 * @param size 大小
	 * @param <K>  key
	 * @param <V>  value
	 * @return hash Map
	 */
	public static <K, V> Map<K, V> newLinkedHashMap(final int size) {
		return new LinkedHashMap<>(size);
	}

	/**
	 * 新建 hash set
	 *
	 * @param <E> 元素
	 * @return 链表
	 */
	public static <E> Set<E> newHashSet() {
		return new HashSet<>();
	}

	/**
	 * 新建 hash set
	 *
	 * @param <E>  元素
	 * @param size 大小
	 * @return 链表
	 */
	public static <E> Set<E> newHashSet(final int size) {
		return new HashSet<>(size);
	}

}
