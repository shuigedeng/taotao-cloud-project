package com.taotao.cloud.job.server.jobserver.common;


/**
 * @author shuigedeng
 * @since 2022/10/2
 */
public class Holder<T> {

    private T value;

    public Holder(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
