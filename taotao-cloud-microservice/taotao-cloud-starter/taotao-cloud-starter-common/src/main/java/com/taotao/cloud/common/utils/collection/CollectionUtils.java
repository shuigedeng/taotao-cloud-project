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

package com.taotao.cloud.common.utils.collection;


import com.beust.jcommander.internal.Sets;
import com.google.common.collect.Lists;
import com.taotao.cloud.common.constant.PunctuationConst;
import com.taotao.cloud.common.support.condition.ICondition;
import com.taotao.cloud.common.support.filler.IFiller;
import com.taotao.cloud.common.support.filter.IFilter;
import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.utils.common.ArgUtils;
import com.taotao.cloud.common.utils.lang.ObjectUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 集合工具类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {

	/**
	 * Return {@code true} if the supplied Collection is not {@code null} or empty. Otherwise,
	 * return {@code false}.
	 *
	 * @param collection the Collection to check
	 * @return whether the given Collection is not empty
	 */
	//public static boolean isNotEmpty(@Nullable Collection<?> collection) {
	//	return !CollectionUtil.isEmpty(collection);
	//}

	/**
	 * Return {@code true} if the supplied Map is not {@code null} or empty. Otherwise, return
	 * {@code false}.
	 *
	 * @param map the Map to check
	 * @return whether the given Map is not empty
	 */
	public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
		return !CollectionUtils.isEmpty(map);
	}

	/**
	 * Check whether the given Array contains the given element.
	 *
	 * @param array   the Array to check
	 * @param element the element to look for
	 * @param <T>     The generic tag
	 * @return {@code true} if found, {@code false} else
	 */
	public static <T> boolean contains(@Nullable T[] array, final T element) {
		if (array == null) {
			return false;
		}
		return Arrays.stream(array).anyMatch(x -> ObjectUtils.nullSafeEquals(x, element));
	}

	/**
	 * Concatenates 2 arrays
	 *
	 * @param one   数组1
	 * @param other 数组2
	 * @return 新数组
	 */
	public static String[] concat(String[] one, String[] other) {
		return concat(one, other, String.class);
	}

	/**
	 * Concatenates 2 arrays
	 *
	 * @param one   数组1
	 * @param other 数组2
	 * @param clazz 数组类
	 * @return 新数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] concat(T[] one, T[] other, Class<T> clazz) {
		T[] target = (T[]) Array.newInstance(clazz, one.length + other.length);
		System.arraycopy(one, 0, target, 0, one.length);
		System.arraycopy(other, 0, target, one.length, other.length);
		return target;
	}

	/**
	 * 不可变 Set
	 *
	 * @param es  对象
	 * @param <E> 泛型
	 * @return 集合
	 */
	@SafeVarargs
	public static <E> Set<E> ofImmutableSet(E... es) {
		return Arrays.stream(Objects.requireNonNull(es, "args es is null."))
			.collect(Collectors.toSet());
	}

	/**
	 * 不可变 List
	 *
	 * @param es  对象
	 * @param <E> 泛型
	 * @return 集合
	 */
	@SafeVarargs
	public static <E> List<E> ofImmutableList(E... es) {
		return Arrays.stream(Objects.requireNonNull(es, "args es is null."))
			.collect(Collectors.toList());
	}

	/**
	 * Iterable 转换为List集合
	 *
	 * @param elements Iterable
	 * @param <E>      泛型
	 * @return 集合
	 */
	public static <E> List<E> toList(Iterable<E> elements) {
		Objects.requireNonNull(elements, "elements es is null.");
		if (elements instanceof Collection) {
			return new ArrayList<>((Collection<E>) elements);
		}
		Iterator<E> iterator = elements.iterator();
		List<E> list = new ArrayList<>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	/**
	 * 将key value 数组转为 map
	 *
	 * @param keysValues key value 数组
	 * @param <K>        key
	 * @param <V>        value
	 * @return map 集合
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> toMap(Object... keysValues) {
		int kvLength = keysValues.length;
		if (kvLength % 2 != 0) {
			throw new IllegalArgumentException(
				"wrong number of arguments for met, keysValues length can not be odd");
		}
		Map<K, V> keyValueMap = new HashMap<>(kvLength);
		for (int i = kvLength - 2; i >= 0; i -= 2) {
			Object key = keysValues[i];
			Object value = keysValues[i + 1];
			keyValueMap.put((K) key, (V) value);
		}
		return keyValueMap;
	}

	/**
	 * A temporary workaround for Java 8 specific performance issue JDK-8161372 .<br> This class
	 * should be removed once we drop Java 8 support.
	 *
	 * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
	 */
	public static <K, V> V computeIfAbsent(Map<K, V> map, K key,
		Function<K, V> mappingFunction) {
		V value = map.get(key);
		if (value != null) {
			return value;
		}
		return map.computeIfAbsent(key, mappingFunction);
	}

	/**
	 * list 分片
	 *
	 * @param list List
	 * @param size 分片大小
	 * @param <T>  泛型
	 * @return List 分片
	 */
	public static <T> List<List<T>> partition(List<T> list, int size) {
		Objects.requireNonNull(list, "List to partition must not null.");
		Assert.isTrue(size > 0, "List to partition size must more then zero.");
		return (list instanceof RandomAccess)
			? new RandomAccessPartition<>(list, size)
			: new Partition<>(list, size);
	}

	private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {

		RandomAccessPartition(List<T> list, int size) {
			super(list, size);
		}
	}

	private static class Partition<T> extends AbstractList<List<T>> {

		final List<T> list;
		final int size;

		Partition(List<T> list, int size) {
			this.list = list;
			this.size = size;
		}

		@Override
		public List<T> get(int index) {
			if (index >= 0 && index < this.size()) {
				int start = index * this.size;
				int end = Math.min(start + this.size, this.list.size());
				return this.list.subList(start, end);
			}
			throw new IndexOutOfBoundsException(
				String.format("index (%s) must be less than size (%s)", index, this.size()));
		}

		@Override
		public int size() {
			return ceilDiv(this.list.size(), this.size);
		}

		@Override
		public boolean isEmpty() {
			return this.list.isEmpty();
		}

		private static int ceilDiv(int x, int y) {
			int r = x / y;
			if (r * y < x) {
				r++;
			}
			return r;
		}
	}

	/**
	 * 空列表
	 */
	public static final List EMPTY_LIST = Collections.emptyList();

	/**
	 * 是否为空
	 *
	 * @param collection 集合
	 * @return {@code true} 是
	 */
	public static boolean isEmpty(Collection collection) {
		return null == collection
			|| collection.isEmpty();    //更有可读性
	}

	/**
	 * 是否不为空
	 *
	 * @param collection 集合
	 * @return {@code true} 是
	 */
	public static boolean isNotEmpty(Collection collection) {
		return !isEmpty(collection);
	}

	/**
	 * 根据数组返回对应列表
	 *
	 * @param array string array
	 * @return string list
	 */
	public static List<String> arrayToList(String[] array) {
		if (ArrayUtils.isEmpty(array)) {
			return Lists.newArrayList();
		}
		return Lists.newArrayList(array);
	}


	/**
	 * 列表转数组
	 *
	 * @param stringList string list
	 * @return string array
	 */
	public static String[] listToArray(List<String> stringList) {
		String[] strings = new String[stringList.size()];
		return stringList.toArray(strings);
	}

	/**
	 * 对字符串列表每条字符串执行trim()操作。
	 * 1. 空直接返回空列表
	 *
	 * @param stringList 原始的列表
	 * @return trim 的字符串列表
	 */
	public static List<String> trimCollection(final Collection<String> stringList) {
		if (CollectionUtils.isEmpty(stringList)) {
			return Collections.emptyList();
		}

		List<String> resultList = Lists.newArrayList();
		for (String original : stringList) {
			resultList.add(original.trim());
		}

		return resultList;
	}

	/**
	 * 构建结果集合
	 * 1. 如果转换的结果为 null，会被跳过。
	 *
	 * @param targets 原始信息
	 * @param handler 处理接口
	 * @param <T>     入参
	 * @param <R>     出参
	 * @return 结果
	 */
	public static <T, R> Collection<R> buildCollection(final Collection<T> targets, final IHandler<T, R> handler) {
		if (isEmpty(targets)) {
			return Collections.emptyList();
		}
		Collection<R> rList = new ArrayList<>();
		for (T t : targets) {
			R r = handler.handle(t);
			if (ObjectUtils.isNotNull(r)) {
				rList.add(r);
			}
		}
		return rList;
	}

	/**
	 * 构建结果集合
	 * 1. 如果转换的结果为 null，会被跳过。
	 *
	 * @param targets 原始信息
	 * @param handler 处理接口
	 * @param <T>     入参
	 * @param <R>     出参
	 * @return 结果
	 */
	public static <T, R> List<R> buildCollection(final T[] targets, final IHandler<T, R> handler) {
		if (ArrayUtils.isEmpty(targets)) {
			return Collections.emptyList();
		}
		List<R> rList = new ArrayList<>(targets.length);
		for (T t : targets) {
			R r = handler.handle(t);
			if (ObjectUtils.isNotNull(r)) {
				rList.add(r);
			}
		}
		return rList;
	}

	/**
	 * 将数组的内容添加到集合
	 *
	 * @param collection 集合
	 * @param array      数组
	 * @param <T>        泛型
	 */
	public static <T> void addArray(final Collection<T> collection, final T[] array) {
		if (ArrayUtils.isEmpty(array)) {
			return;
		}

		collection.addAll(Lists.newArrayList(array));
	}

	/**
	 * 可遍历的元素对象的某个元素，转换为列表
	 *
	 * @param values      遍历对象
	 * @param keyFunction 转换方式
	 * @param <K>         k 泛型
	 * @param <V>         v 泛型
	 * @return 结果列表
	 */
	public static <K, V> List<K> toList(final Iterable<V> values, IHandler<? super V, K> keyFunction) {
		if (ObjectUtils.isNull(values)) {
			return Collections.emptyList();
		}
		return toList(values.iterator(), keyFunction);
	}

	/**
	 * 可遍历的元素对象的某个元素，转换为列表
	 *
	 * @param values      遍历对象
	 * @param keyFunction 转换方式
	 * @param <K>         k 泛型
	 * @param <V>         v 泛型
	 * @return 结果列表
	 */
	public static <K, V> List<K> toList(final Iterator<V> values, IHandler<? super V, K> keyFunction) {
		if (ObjectUtils.isNull(values)) {
			return Collections.emptyList();
		}

		List<K> list = new ArrayList<>();
		while (values.hasNext()) {
			V value = values.next();
			final K key = keyFunction.handle(value);
			list.add(key);
		}
		return list;
	}

	/**
	 * 遍历填充对象
	 *
	 * @param values 遍历对象
	 * @param filler 对象填充器
	 * @param <E>    e 泛型
	 * @return 返回类表
	 */
	public static <E> List<E> fillList(final List<E> values, IFiller<E> filler) {
		if (ObjectUtils.isNull(values)) {
			return values;
		}

		for (E e : values) {
			filler.fill(e);
		}

		return values;
	}


	/**
	 * 按照任意空格拆分
	 *
	 * @param string 字符串
	 * @return 拆分后的列表
	 */
	public static List<String> splitByAnyBlank(String string) {
		if (StringUtils.isEmpty(string)) {
			return Collections.emptyList();
		} else {
			String pattern = "\\s+";
			String[] strings = string.split(pattern);
			return Lists.newArrayList(strings);
		}
	}

	/**
	 * 执行列表过滤
	 *
	 * @param list   原始列表
	 * @param filter 过滤器
	 * @param <T>    泛型
	 * @return 过滤后的结果
	 */
	public static <T> List<T> filterList(final List<T> list, final IFilter<T> filter) {
		if (isEmpty(list)) {
			return Collections.emptyList();
		}

		List<T> resultList = new ArrayList<>();
		for (T t : list) {
			if (filter.filter(t)) {
				continue;
			}

			resultList.add(t);
		}
		return resultList;
	}

	/**
	 * 执行列表过滤
	 *
	 * @param list      原始列表
	 * @param condition 条件过滤器
	 * @param <T>       泛型
	 * @return 过滤后的结果
	 */
	public static <T> List<T> conditionList(final List<T> list, final ICondition<T> condition) {
		if (isEmpty(list)) {
			return Collections.emptyList();
		}

		List<T> resultList = new ArrayList<>();
		for (T t : list) {
			if (condition.condition(t)) {
				resultList.add(t);
			}
		}
		return resultList;
	}

	/**
	 * 对象列表转换为 toString 列表
	 * 1. 会跳过所有的 null 对象。
	 * 2. 建议放在 collectUtil 下。
	 *
	 * @param pathList 原始对象
	 * @return 结果
	 */
	public static List<String> toStringList(final List<?> pathList) {
		if (CollectionUtils.isEmpty(pathList)) {
			return Collections.emptyList();
		}

		List<String> stringList = new ArrayList<>(pathList.size());
		for (Object object : pathList) {
			if (ObjectUtils.isNotNull(object)) {
				stringList.add(object.toString());
			}
		}

		return stringList;
	}

	/**
	 * 找到第一个不为 null 的元素
	 *
	 * @param list 列表
	 * @param <T>  泛型
	 * @return 不为 null 的元素
	 */
	public static <T> java.util.Optional<T> firstNotNullElem(Collection<T> list) {
		if (CollectionUtils.isEmpty(list)) {
			return java.util.Optional.empty();
		}

		for (T elem : list) {
			if (ObjectUtils.isNotNull(elem)) {
				return java.util.Optional.of(elem);
			}
		}
		return Optional.empty();
	}

	/**
	 * 将字符串集合内容按照 connector 连接起来
	 *
	 * @param stringCollection 字符串集合
	 * @param connector  连接符号
	 * @return 结果
	 */
	public static String join(final Collection<String> stringCollection,
		final String connector) {
		return StringUtils.join(stringCollection, connector);
	}

	/**
	 * 将字符串集合内容按照逗号连接起来
	 *
	 * @param stringCollection 字符串集合
	 * @return 结果
	 */
	public static String join(final Collection<String> stringCollection) {
		return StringUtils.join(stringCollection, PunctuationConst.COMMA);
	}

	/**
	 * 循环处理集合
	 *
	 * @param collection 集合
	 * @param handler    处理器
	 * @param <E>        泛型元素
	 */
	public static <E> void foreach(final Collection<E> collection, IHandler<E, Void> handler) {
		if (CollectionUtils.isEmpty(collection)) {
			return;
		}

		for (E e : collection) {
			handler.handle(e);
		}
	}

	/**
	 * 循环处理集合
	 *
	 * @param collection 集合
	 * @param <E>        泛型元素
	 */
	public static <E> void foreachPrint(final Collection<E> collection) {
		foreach(collection, new IHandler<E, Void>() {
			@Override
			public Void handle(E e) {
				System.out.println(e);
				return null;
			}
		});
	}

	/**
	 * 填充信息
	 *
	 * @param size 大小
	 * @param elem 单个元素
	 * @param <E>  泛型
	 * @return 列表
	 */
	public static <E> List<E> fill(final int size,
		final E elem) {
		List<E> list = Lists.newArrayList();

		for (int i = 0; i < size; i++) {
			list.add(elem);
		}
		return list;
	}

	/**
	 * 填充信息
	 *
	 * @param size 大小
	 * @param initValue 初始值
	 * @return 列表
	 */
	public static List<Integer> fill(final int size, final int initValue) {
		List<Integer> list = Lists.newArrayList();

		for (int i = 0; i < size; i++) {
			list.add(i + initValue);
		}
		return list;
	}

	/**
	 * 填充信息
	 *
	 * @param size 大小
	 * @return 列表
	 */
	public static List<Integer> fill(final int size) {
		return fill(size, 0);
	}

	/**
	 * 获取第一个元素
	 * 1. 避免 NPE
	 *
	 * @param list 列表
	 * @param <E>  泛型
	 * @return 结果
	 */
	public static <E> E getFirst(final List<E> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 去重集合
	 *
	 * @param <T>        集合元素类型
	 * @param collection 集合
	 * @return {@link ArrayList}
	 */
	public static <T> List<T> distinct(Collection<T> collection) {
		if (isEmpty(collection)) {
			return Collections.emptyList();
		} else if (collection instanceof Set) {
			return new ArrayList<>(collection);
		} else {
			return new ArrayList<>(new LinkedHashSet<>(collection));
		}
	}

	/**
	 * 去重并且排序
	 * @param collection 原始集合
	 * @param <T> 泛型
	 * @return 结果
	 */
	public static <T extends Comparable> List<T> distinctAndSort(final Collection<T> collection) {
		List<T> list = distinct(collection);
		return sort(list);
	}

	/**
	 * 获取重复列表
	 * @param collection 集合
	 * @param <T> 泛型
	 * @return 结果列表
	 */
	public static <T extends Comparable> List<T> getRepeatList(final Collection<T> collection) {
		if(CollectionUtils.isEmpty(collection)) {
			return Collections.emptyList();
		}

		List<T> resultList = Lists.newArrayList();
		Set<T> set = Sets.newHashSet();
		for(T elem : collection) {
			// 重复数据
			if(set.contains(elem)) {
				resultList.add(elem);
			}

			set.add(elem);
		}

		return resultList;
	}

	/**
	 * 排序
	 *
	 * @param <T> 集合元素类型
	 * @param collection 集合
	 * @return {@link ArrayList}
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable> List<T> sort(List<T> collection) {
		if (isEmpty(collection)) {
			return Collections.emptyList();
		}
		Collections.sort(collection);
		return new ArrayList<>(collection);
	}

	/**
	 * 获取开始的下标
	 * （1）默认为0
	 * （2）如果为负数，或者超过 arrays.length-1，则使用 0
	 * （3）正常返回 startIndex
	 *
	 * @param startIndex 开始下标
	 * @param collection 集合信息
	 * @return 尽可能安全的下标范围。如果为空，则返回 0;
	 */
	public static int getStartIndex(final int startIndex,
		final Collection<?> collection) {
		if (CollectionUtils.isEmpty(collection)) {
			return 0;
		}
		if (startIndex < 0
			|| startIndex > collection.size() - 1) {
			return 0;
		}

		return startIndex;
	}

	/**
	 * 获取开始的下标
	 * （1）默认为0
	 * （2）如果为负数，或者超过 arrays.length-1，则使用 arrays.length-1
	 * （3）正常返回 endIndex
	 *
	 * @param endIndex   结束下标
	 * @param collection 集合信息
	 * @return 尽可能安全的下标范围。如果为空，则返回 0;
	 */
	public static int getEndIndex(final int endIndex,
		final Collection<?> collection) {
		if (CollectionUtils.isEmpty(collection)) {
			return 0;
		}
		final int maxIndex = collection.size() - 1;
		if (endIndex < 0
			|| endIndex > maxIndex) {
			return maxIndex;
		}

		return endIndex;
	}

	/**
	 * 集合的并集
	 *
	 * @param collectionOne 集合1
	 * @param collectionTwo 集合2
	 * @param <E>           泛型
	 * @return 结果集合
	 */
	public static <E> List<E> union(final Collection<E> collectionOne,
		final Collection<E> collectionTwo) {
		Set<E> set = new LinkedHashSet<>();
		set.addAll(collectionOne);
		set.addAll(collectionTwo);
		return new ArrayList<>(set);
	}

	/**
	 * 集合的差集
	 *
	 * @param collectionOne 集合1
	 * @param collectionTwo 集合2
	 * @param <E>           泛型
	 * @return 结果集合
	 */
	public static <E> List<E> difference(final Collection<E> collectionOne,
		final Collection<E> collectionTwo) {
		Set<E> set = new LinkedHashSet<>();
		set.addAll(collectionOne);
		set.removeAll(collectionTwo);
		return new ArrayList<>(set);
	}

	/**
	 * 集合的差交集
	 *
	 * @param collectionOne 集合1
	 * @param collectionTwo 集合2
	 * @param <E>           泛型
	 * @return 结果集合
	 */
	public static <E> List<E> interSection(final Collection<E> collectionOne,
		final Collection<E> collectionTwo) {
		Set<E> set = new LinkedHashSet<>();

		for (E e : collectionOne) {
			if (collectionTwo.contains(e)) {
				set.add(e);
			}
		}
		return new ArrayList<>(set);
	}

	/**
	 * 包含任何一个
	 * @param firstList 第一个列表
	 * @param secondList 第二个列表
	 * @return 是否包含
	 */
	public static boolean containAny(final Collection<String> firstList,
		final Collection<String> secondList) {
		if(CollectionUtils.isEmpty(firstList)
			|| CollectionUtils.isEmpty(secondList)) {
			return false;
		}

		for(String second : secondList) {
			if(firstList.contains(second)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 获取最后一行
	 * @param resultList 结果列表
	 * @return 最后一行
	 */
	public static String getLast(final List<String> resultList) {
		if(CollectionUtils.isEmpty(resultList)) {
			return StringUtils.EMPTY;
		}

		return resultList.get(resultList.size()-1);
	}

	/**
	 * 设置最后一行
	 * @param resultList 结果列表
	 * @param line 最后一行内容
	 */
	public static void setLast(List<String> resultList,
		final String line) {
		if(resultList == null) {
			resultList = new ArrayList<>();
		}

		if(CollectionUtils.isEmpty(resultList)) {
			resultList.add(line);
		}

		// 其他直接设置即可
		resultList.set(resultList.size()-1, line);
	}

	/**
	 * 获取指定的前几个元素
	 * @param collection 集合
	 * @param size 大小
	 * @param <T> 泛型
	 * @return 结果
	 */
	public static <T> List<T> getTopK(final Collection<T> collection, final int size) {
		if(CollectionUtils.isEmpty(collection)) {
			return Collections.emptyList();
		}

		int actualSize = Math.min(collection.size(), size);
		List<T> resultList = Lists.newArrayList();

		for(T t : collection) {
			resultList.add(t);

			if(resultList.size() >= actualSize) {
				break;
			}
		}
		return resultList;
	}

	/**
	 * 针对整个集合做替换处理
	 * @param collection 集合
	 * @param regex 正则
	 * @param target 目标
	 * @return 结果
	 */
	public static List<String> replaceAll(final Collection<String> collection, final String regex, final String target) {
		if(CollectionUtils.isEmpty(collection)) {
			return Collections.emptyList();
		}

		List<String> resultList = Lists.newArrayList();
		for(String s : collection) {
			String result = s.replaceAll(regex, target);
			resultList.add(result);
		}

		return resultList;
	}

	/**
	 * 对原始列表进行截取
	 *
	 * @param list   集合1
	 * @param offset 偏移量
	 * @param limit   大小
	 * @param <E>    泛型
	 * @return 结果集合
	 */
	public static <E> List<E> subList(final List<E> list,
		final int offset,
		final int limit) {
		// 参数校验
		ArgUtils.notNegative(offset, "offset");
		ArgUtils.notNegative(limit, "limit");

		//fast-return
		if(CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}

		//offset 的大小计算
		final int size = list.size();
		final int actualOffset = Math.min(offset, size);
		final int actualLimit = Math.min(limit, size-actualOffset);

		// 处理
		List<E> resultList = Lists.newArrayList();
		for(int i = actualOffset; i < actualOffset+actualLimit; i++) {
			resultList.add(list.get(i));
		}
		return resultList;
	}

	/**
	 * 随机获取一个元素
	 * @param list 列表
	 * @param <E> 元素
	 * @return 结果
	 */
	public static <E> E random(final List<E> list) {
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}

		Random random = ThreadLocalRandom.current();
		int next = random.nextInt(list.size());

		return list.get(next);
	}

	/**
	 * 空列表
	 * @param <T> 泛型
	 * @return 空列表
	 */
	public static <T> List<T> list() {
		return Collections.emptyList();
	}

	/**
	 * 空列表
	 * @param t 实体
	 * @param <T> 泛型
	 * @return 空列表
	 */
	public static <T> List<T> list(T t) {
		return Collections.singletonList(t);
	}

	/**
	 * 列表
	 * @param ts 数组
	 * @param <T> 泛型
	 * @return 空列表
	 */
	@SafeVarargs
	public static <T> List<T> list(T... ts) {
		return new ArrayList<>(Arrays.asList(ts));
	}

	/**
	 * 复制列表
	 * @param list 列表
	 * @param <T> 泛型
	 * @return 空列表
	 */
	public static <T> List<T> copy(List<T> list) {
		return new ArrayList<>(list);
	}

	/**
	 * 获取第一个元素
	 * @param list 列表
	 * @param <T> 泛型
	 * @return 空列表
	 */
	public static <T> T head(List<T> list) {
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 获取最后一个元素
	 * @param list 列表
	 * @param <T> 泛型
	 * @return 空列表
	 */
	public static <T> T tail(List<T> list) {
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(list.size()-1);
	}

	/**
	 * 添加元素到列表
	 * @param list 列表
	 * @param t 元素
	 * @param <T> 泛型
	 * @return 空列表
	 */
	public static <T> List<T> append(List<T> list, T t) {
		if(list == null) {
			list = new ArrayList<>();
		}
		list.add(t);

		return list;
	}

	/**
	 * 反转列表
	 * @param list 列表
	 * @param t 元素
	 * @param <T> 泛型
	 * @return 空列表
	 */
	public static <T> List<T> reverse(List<T> list, T t) {
		if(CollectionUtils.isEmpty(list)) {
			return list;
		}

		List<T> results = new ArrayList<>(list.size());
		for(int i = list.size()-1; i >= 0; i--) {
			results.add(list.get(i));
		}
		list.add(t);

		return results;
	}

	/**
	 * 添加所有的元素
	 * @param collection 集合1
	 * @param addCollection 集合2
	 * @param <T> 泛型
	 */
	public static <T> void  addAll(Collection<T> collection, Collection<T> addCollection) {
		if(CollectionUtils.isNotEmpty(addCollection)) {
			collection.addAll(addCollection);
		}
	}

}
