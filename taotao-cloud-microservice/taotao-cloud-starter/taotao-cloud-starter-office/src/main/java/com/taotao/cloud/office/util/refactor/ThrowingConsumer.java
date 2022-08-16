package com.taotao.cloud.office.util.refactor;

import java.util.function.Consumer;

/**
 * java8中自带的Consumer是不会抛出异常的，为了支持执行自定义操作时能够将具体的异常抛出，重写了一个可以抛出异常的Consumer
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
    /**
     * 重写accept方法，捕获并抛出异常
     * @param t
     */
    @Override
    default void accept(T t) {
        try {
            acceptBase(t);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    void acceptBase(T t);
}
