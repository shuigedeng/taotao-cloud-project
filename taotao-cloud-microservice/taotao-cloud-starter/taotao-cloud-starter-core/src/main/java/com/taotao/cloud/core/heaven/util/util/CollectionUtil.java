/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.core.heaven.util.util;


import com.taotao.cloud.core.heaven.constant.PunctuationConst;
import com.taotao.cloud.core.heaven.support.condition.ICondition;
import com.taotao.cloud.core.heaven.support.filler.IFiller;
import com.taotao.cloud.core.heaven.support.filter.IFilter;
import com.taotao.cloud.core.heaven.support.handler.IHandler;
import com.taotao.cloud.core.heaven.util.common.ArgUtil;
import com.taotao.cloud.core.heaven.util.guava.Guavas;
import com.taotao.cloud.core.heaven.util.lang.ObjectUtil;
import com.taotao.cloud.core.heaven.util.lang.StringUtil;
import java.util.*;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 集合工具类
 *
 * @author bbhou
 * @since 0.0.1
 */
public final class CollectionUtil {

    /**
     * collection util
     */
    private CollectionUtil() {
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
     * @since 1.1.2
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
        if (ArrayUtil.isEmpty(array)) {
            return Guavas.newArrayList();
        }
        return Guavas.newArrayList(array);
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
     * @since 0.1.70
     */
    public static List<String> trimCollection(final Collection<String> stringList) {
        if (CollectionUtil.isEmpty(stringList)) {
            return Collections.emptyList();
        }

        List<String> resultList = Guavas.newArrayList(stringList.size());
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
            if (ObjectUtil.isNotNull(r)) {
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
     * @since 0.0.2
     */
    public static <T, R> List<R> buildCollection(final T[] targets, final IHandler<T, R> handler) {
        if (ArrayUtil.isEmpty(targets)) {
            return Collections.emptyList();
        }
        List<R> rList = new ArrayList<>(targets.length);
        for (T t : targets) {
            R r = handler.handle(t);
            if (ObjectUtil.isNotNull(r)) {
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
        if (ArrayUtil.isEmpty(array)) {
            return;
        }

        collection.addAll(Guavas.newArrayList(array));
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
        if (ObjectUtil.isNull(values)) {
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
        if (ObjectUtil.isNull(values)) {
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
     * @since 0.1.10
     */
    public static <E> List<E> fillList(final List<E> values, IFiller<E> filler) {
        if (ObjectUtil.isNull(values)) {
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
     * @since 0.0.5
     */
    public static List<String> splitByAnyBlank(String string) {
        if (StringUtil.isEmpty(string)) {
            return Collections.emptyList();
        } else {
            String pattern = "\\s+";
            String[] strings = string.split(pattern);
            return Guavas.newArrayList(strings);
        }
    }

    /**
     * 执行列表过滤
     *
     * @param list   原始列表
     * @param filter 过滤器
     * @param <T>    泛型
     * @return 过滤后的结果
     * @since 0.0.6
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
     * @since 0.0.6
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
     * @since 0.0.6
     */
    public static List<String> toStringList(final List<?> pathList) {
        if (CollectionUtil.isEmpty(pathList)) {
            return Collections.emptyList();
        }

        List<String> stringList = new ArrayList<>(pathList.size());
        for (Object object : pathList) {
            if (ObjectUtil.isNotNull(object)) {
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
     * @since 0.1.6
     */
    public static <T> java.util.Optional<T> firstNotNullElem(Collection<T> list) {
        if (CollectionUtil.isEmpty(list)) {
            return java.util.Optional.empty();
        }

        for (T elem : list) {
            if (ObjectUtil.isNotNull(elem)) {
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
     * @since 0.1.6
     */
    public static String join(final Collection<String> stringCollection,
                              final String connector) {
        return StringUtil.join(stringCollection, connector);
    }

    /**
     * 将字符串集合内容按照逗号连接起来
     *
     * @param stringCollection 字符串集合
     * @return 结果
     * @since 0.1.46
     */
    public static String join(final Collection<String> stringCollection) {
        return StringUtil.join(stringCollection, PunctuationConst.COMMA);
    }

    /**
     * 循环处理集合
     *
     * @param collection 集合
     * @param handler    处理器
     * @param <E>        泛型元素
     * @since 0.1.8
     */
    public static <E> void foreach(final Collection<E> collection, IHandler<E, Void> handler) {
        if (CollectionUtil.isEmpty(collection)) {
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
     * @since 0.1.93
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
     * @since 0.1.9
     */
    public static <E> List<E> fill(final int size,
                                   final E elem) {
        List<E> list = Guavas.newArrayList(size);

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
     * @since 0.1.85
     */
    public static List<Integer> fill(final int size, final int initValue) {
        List<Integer> list = Guavas.newArrayList(size);

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
     * @since 0.1.85
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
     * @since 0.1.10
     */
    public static <E> E getFirst(final List<E> list) {
        if (CollectionUtil.isEmpty(list)) {
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
     * @since 0.1.13
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
     * @since 0.1.72
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
     * @since 0.1.92
     */
    public static <T extends Comparable> List<T> getRepeatList(final Collection<T> collection) {
        if(CollectionUtil.isEmpty(collection)) {
            return Collections.emptyList();
        }

        List<T> resultList = Guavas.newArrayList();
        Set<T> set = Guavas.newHashSet();
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
     * @since 0.1.13
     */
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
     * @since 0.1.14
     */
    public static int getStartIndex(final int startIndex,
                                    final Collection<?> collection) {
        if (CollectionUtil.isEmpty(collection)) {
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
     * @since 0.1.14
     */
    public static int getEndIndex(final int endIndex,
                                  final Collection<?> collection) {
        if (CollectionUtil.isEmpty(collection)) {
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
     * @since 0.1.16
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
     * @since 0.1.16
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
     * @since 0.1.16
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
     * @since 0.1.70
     */
    public static boolean containAny(final Collection<String> firstList,
                                        final Collection<String> secondList) {
        if(CollectionUtil.isEmpty(firstList)
                || CollectionUtil.isEmpty(secondList)) {
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
     * @since 0.1.70
     */
    public static String getLast(final List<String> resultList) {
        if(CollectionUtil.isEmpty(resultList)) {
            return StringUtil.EMPTY;
        }

        return resultList.get(resultList.size()-1);
    }

    /**
     * 设置最后一行
     * @param resultList 结果列表
     * @param line 最后一行内容
     * @since 0.1.70
     */
    public static void setLast(List<String> resultList,
                             final String line) {
        if(resultList == null) {
            resultList = new ArrayList<>();
        }

        if(CollectionUtil.isEmpty(resultList)) {
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
     * @since 0.1.79
     */
    public static <T> List<T> getTopK(final Collection<T> collection, final int size) {
        if(CollectionUtil.isEmpty(collection)) {
            return Collections.emptyList();
        }

        int actualSize = Math.min(collection.size(), size);
        List<T> resultList = Guavas.newArrayList();

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
     * @since 0.1.85
     */
    public static List<String> replaceAll(final Collection<String> collection, final String regex, final String target) {
        if(CollectionUtil.isEmpty(collection)) {
            return Collections.emptyList();
        }

        List<String> resultList = Guavas.newArrayList(collection.size());
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
     * @since 0.1.89
     */
    public static <E> List<E> subList(final List<E> list,
                                      final int offset,
                                      final int limit) {
        // 参数校验
        ArgUtil.notNegative(offset, "offset");
        ArgUtil.notNegative(limit, "limit");

        //fast-return
        if(CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        //offset 的大小计算
        final int size = list.size();
        final int actualOffset = Math.min(offset, size);
        final int actualLimit = Math.min(limit, size-actualOffset);

        // 处理
        List<E> resultList = Guavas.newArrayList(actualLimit);
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
     * @since 0.1.100
     */
    public static <E> E random(final List<E> list) {
        if(CollectionUtil.isEmpty(list)) {
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
     * @since 0.1.128
     */
    public static <T> List<T> list() {
        return Collections.emptyList();
    }

    /**
     * 空列表
     * @param t 实体
     * @param <T> 泛型
     * @return 空列表
     * @since 0.1.128
     */
    public static <T> List<T> list(T t) {
        return Collections.singletonList(t);
    }

    /**
     * 列表
     * @param ts 数组
     * @param <T> 泛型
     * @return 空列表
     * @since 0.1.128
     */
    public static <T> List<T> list(T... ts) {
        return new ArrayList<>(Arrays.asList(ts));
    }

    /**
     * 复制列表
     * @param list 列表
     * @param <T> 泛型
     * @return 空列表
     * @since 0.1.128
     */
    public static <T> List<T> copy(List<T> list) {
        return new ArrayList<>(list);
    }

    /**
     * 获取第一个元素
     * @param list 列表
     * @param <T> 泛型
     * @return 空列表
     * @since 0.1.128
     */
    public static <T> T head(List<T> list) {
        if(CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取最后一个元素
     * @param list 列表
     * @param <T> 泛型
     * @return 空列表
     * @since 0.1.128
     */
    public static <T> T tail(List<T> list) {
        if(CollectionUtil.isEmpty(list)) {
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
     * @since 0.1.128
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
     * @since 0.1.128
     */
    public static <T> List<T> reverse(List<T> list, T t) {
        if(CollectionUtil.isEmpty(list)) {
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
     * @since 0.1.147
     */
    public static <T> void  addAll(Collection<T> collection, Collection<T> addCollection) {
        if(CollectionUtil.isNotEmpty(addCollection)) {
            collection.addAll(addCollection);
        }
    }

}
