package com.taotao.cloud.health.alarm.common.concurrent.forkjoin;

public interface IDataLoader<T> {


    /**
     * 具体的业务逻辑，放在这个方法里面执行，将返回的结果，封装到context内
     *
     * @param context
     */
    void load(T context);

}
