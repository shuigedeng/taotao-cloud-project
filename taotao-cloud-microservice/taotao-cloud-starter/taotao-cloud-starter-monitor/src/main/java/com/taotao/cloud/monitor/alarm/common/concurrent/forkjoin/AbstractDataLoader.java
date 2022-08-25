package com.taotao.cloud.monitor.alarm.common.concurrent.forkjoin;

import java.util.concurrent.RecursiveAction;

public abstract class AbstractDataLoader<T> extends RecursiveAction implements IDataLoader {

    protected T context;

    public AbstractDataLoader(T context) {
        this.context = context;
    }

    public void compute() {
        load(context);
    }


    /**
     * 获取执行后的结果
     * @return
     */
    public T getContext() {
        this.join();
        return context;
    }

    public void setContext(T context) {
        this.context = context;
    }
}
