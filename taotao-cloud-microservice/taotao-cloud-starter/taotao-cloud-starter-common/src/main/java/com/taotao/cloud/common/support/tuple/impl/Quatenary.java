package com.taotao.cloud.common.support.tuple.impl;


import com.taotao.cloud.common.support.tuple.IValueFour;
import com.taotao.cloud.common.support.tuple.IValueOne;
import com.taotao.cloud.common.support.tuple.IValueThree;
import com.taotao.cloud.common.support.tuple.IValueTwo;

/**
 * 四元的
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:12:19
 */
public class Quatenary<A, B, C, D> extends AbstractTuple
        implements IValueOne<A>, IValueTwo<B>, IValueThree<C>, IValueFour<D> {

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
     * 第四个元素
     */
    private final D d;

    /**
     * 构造器
     * @param a 第一个元素
     * @param b 第二个元素
     * @param c 第三个元素
     * @param d 第四个元素
     */
    public Quatenary(A a, B b, C c, D d) {
        super(a, b, c, d);
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * 初始化三元运算符
     *
     * @param a   第一个元素
     * @param b   第二个元素
     * @param c   第三个元素
     * @param d   第四个元素
     * @param <A> 泛型1
     * @param <B> 泛型2
     * @param <C> 泛型3
     * @param <D> 泛型4
     * @return 结果
     */
    public static <A, B, C, D> Quatenary<A, B, C, D> of(A a, B b, C c, D d) {
        return new Quatenary<>(a, b, c, d);
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
    public D getValueFour() {
        return this.d;
    }

    @Override
    public String toString() {
        return "Quatenary{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                '}';
    }

}
