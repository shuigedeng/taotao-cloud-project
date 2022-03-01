package com.taotao.cloud.core.tuple;

/**
 * 表示有4个元素的元组类型
 * 可迭代
 * 不可变，线程安全
 */
public final class Tuple4<A, B, C, D> extends Tuple {

    public final A first;
    public final B second;
    public final C third;
    public final D fourth;

    private Tuple4(final A first, final B second, final C third, final D fourth) {
        super(first, second, third, fourth);
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    /**
     * 创建一个包含4个元素的元组
     *
     * @param first  第一个元素
     * @param second 第二个元素
     * @param third  第三个元素
     * @param fourth 第四个元素
     * @param <A>    第一个元素类型
     * @param <B>    第二个元素类型
     * @param <C>    第三个元素类型
     * @param <D>    第四个元素类型
     * @return 元组
     * @see Tuples#tuple(Object, Object, Object, Object)
     */
    public static <A, B, C, D> Tuple4<A, B, C, D> with(final A first, final B second, final C third, final D fourth) {
        return new Tuple4<>(first, second, third, fourth);
    }

    /**
     * 反转元组
     *
     * @return 反转后的元组
     */
    @Override
    public Tuple4<D, C, B, A> reverse() {
        return new Tuple4<>(this.fourth, this.third, this.second, this.first);
    }
}
