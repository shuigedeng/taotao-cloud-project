package com.taotao.cloud.common.support.tuple.impl;


import com.taotao.cloud.common.support.tuple.IValueOne;
import com.taotao.cloud.common.support.tuple.IValueThree;
import com.taotao.cloud.common.support.tuple.IValueTwo;

/**
 * 三元的
 *
 * @param <A> 第一个元素
 * @param <B> 第二个元素
 * @param <C> 第三个元素
 */
public class Ternary<A, B, C> extends AbstractTuple
        implements IValueOne<A>, IValueTwo<B>, IValueThree<C> {

    /**
     * 第一个元素
     */
    private final A a;

    /**
     * 第二个元素
     */
    private final B b;

    /**
     * 第三个元素
     */
    private final C c;

    /**
     * 构造器
     * @param a 第一个元素
     * @param b 第二个元素
     * @param c 第三个元素
     */
    public Ternary(A a, B b, C c) {
        super(a, b, c);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * 初始化三元运算符
     *
     * @param a   第一个元素
     * @param b   第二个元素
     * @param c   第三个元素
     * @param <A> 泛型1
     * @param <B> 泛型2
     * @param <C> 泛型3
     * @return 结果
     */
    public static <A, B, C> Ternary<A, B, C> of(A a, B b, C c) {
        return new Ternary<>(a, b, c);
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
    public C getValueThree() {
        return this.c;
    }

    @Override
    public String toString() {
        return "Ternary{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
