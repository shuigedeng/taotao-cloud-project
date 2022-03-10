package com.taotao.cloud.common.support.pipeline;

import java.util.List;

/**
 * 管道类
 * 1. 作用：用于列表信息的统一管理
 * @param <T> 模板泛型
 */
public interface Pipeline<T> {

    /**
     * 加入到列表的末尾
     * @param t 元素
     * @return this
     */
    Pipeline addLast(final T t);

    /**
     * 加入到列表的开头
     * @param t 元素
     * @return this
     */
    Pipeline addFirst(final T t);

    /**
     * 设置元素 index 位置为 t
     * @param index 下标志
     * @param t 元素
     * @return this
     */
    Pipeline set(final int index, final T t);

    /**
     * 移除最后一个元素
     * @return this
     */
    Pipeline removeLast();

    /**
     * 移除第一个元素
     * @return this
     */
    Pipeline removeFirst();

    /**
     * 移除 index 位置的元素
     * @param index 下标值
     * @return this
     */
    Pipeline remove(final int index);

    /**
     * 获取指定位置的元素
     * @param index 下标
     * @return 元素
     */
    T get(final int index);

    /**
     * 获取第一个位置的元素
     * @return 元素
     */
    T getFirst();

    /**
     * 获取最后一个位置的元素
     * @return 元素
     */
    T getLast();

    /**
     * 获取所有的元素列表
     * @return 所有的元素列表
     */
    List<T> list();

    /**
     * 进行 slice 分片返回一个从 startIndex~endIndex 的新列表
     * 1. 如果超过数组下标则直接报错
     * @param startIndex 开始下标
     * @param endIndex 结束下标
     * @return 截取后的元素列表
     */
    List<T> slice(final int startIndex, final int endIndex);

}
