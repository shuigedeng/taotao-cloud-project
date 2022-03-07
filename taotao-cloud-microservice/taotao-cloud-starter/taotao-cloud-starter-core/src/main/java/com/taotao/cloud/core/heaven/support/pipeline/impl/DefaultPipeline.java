package com.taotao.cloud.core.heaven.support.pipeline.impl;


import com.taotao.cloud.core.heaven.annotation.NotThreadSafe;
import com.taotao.cloud.core.heaven.support.pipeline.Pipeline;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 默认的泳道实现
 */
@NotThreadSafe
public class DefaultPipeline<T> implements Pipeline<T> {

    /**
     * 创建一个内部的链表
     */
    private LinkedList<T> list = new LinkedList<>();

    @Override
    public Pipeline addLast(T t) {
        list.addLast(t);
        return this;
    }

    @Override
    public Pipeline addFirst(T t) {
        list.addFirst(t);
        return this;
    }

    @Override
    public Pipeline set(int index, T t) {
        list.set(index, t);
        return this;
    }

    @Override
    public Pipeline removeLast() {
        list.removeLast();
        return this;
    }

    @Override
    public Pipeline removeFirst() {
        list.removeFirst();
        return this;
    }

    @Override
    public Pipeline remove(int index) {
        list.remove(index);
        return this;
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T getFirst() {
        return list.getFirst();
    }

    @Override
    public T getLast() {
        return list.getLast();
    }

    @Override
    public List<T> list() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<T> slice(int startIndex, int endIndex) {
        return list.subList(startIndex, endIndex);
    }
}
