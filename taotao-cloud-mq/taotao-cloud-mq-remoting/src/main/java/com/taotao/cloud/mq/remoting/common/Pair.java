package com.taotao.cloud.mq.remoting.common;

import java.util.Objects;

/**
 * 键值对
 */
public class Pair<T1, T2> {

    private T1 object1;
    private T2 object2;

    public Pair(T1 object1, T2 object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    /**
     * 创建一个Pair
     */
    public static <T1, T2> Pair<T1, T2> of(T1 object1, T2 object2) {
        return new Pair<>(object1, object2);
    }

    public T1 getObject1() {
        return object1;
    }

    public void setObject1(T1 object1) {
        this.object1 = object1;
    }

    public T2 getObject2() {
        return object2;
    }

    public void setObject2(T2 object2) {
        this.object2 = object2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(object1, pair.object1) &&
                Objects.equals(object2, pair.object2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object1, object2);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "object1=" + object1 +
                ", object2=" + object2 +
                '}';
    }
}
