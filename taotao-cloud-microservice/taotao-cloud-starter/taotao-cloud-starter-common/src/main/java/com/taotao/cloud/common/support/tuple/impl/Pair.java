package com.taotao.cloud.common.support.tuple.impl;


import com.taotao.cloud.common.support.tuple.IValueOne;
import com.taotao.cloud.common.support.tuple.IValueTwo;

/**
 * 二元的
 * @param <A> 第一个元素
 * @param <B> 第二个元素
 */
public class Pair<A,B> extends AbstractTuple
        implements IValueOne<A>, IValueTwo<B> {

    /**
     * 第一个元素
     */
    private final A a;

    /**
     * 第二个元素
     */
    private final B b;

    /**
     * 初始化二元运算符
     *
     * @param a   第一个元素
     * @param b   第二个元素
     */
    public Pair(A a, B b) {
        super(a, b);
        this.a = a;
        this.b = b;
    }

    /**
     * 初始化二元运算符
     *
     * @param a   第一个元素
     * @param b   第二个元素
     * @param <A> 泛型1
     * @param <B> 泛型2
     * @return 结果
     */
    public static <A,B> Pair<A,B> of(A a, B b) {
        return new Pair<>(a, b);
    }

    @Override
    public A getValueOne() {
        return this.a;
    }

    @Override
    public B getValueTwo() {
        return this.b;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }

}
