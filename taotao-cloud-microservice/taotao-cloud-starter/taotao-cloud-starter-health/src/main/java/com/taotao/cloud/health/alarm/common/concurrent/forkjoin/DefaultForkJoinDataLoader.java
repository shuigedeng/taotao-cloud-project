package com.taotao.cloud.health.alarm.common.concurrent.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

public class DefaultForkJoinDataLoader<T> extends AbstractDataLoader<T> {
    /**
     * 待执行的任务列表
     */
    private List<AbstractDataLoader> taskList;


    public DefaultForkJoinDataLoader(T context) {
        super(context);
        taskList = new ArrayList<>();
    }


    public DefaultForkJoinDataLoader<T> addTask(IDataLoader dataLoader) {
        taskList.add(new AbstractDataLoader(this.context) {
            @Override
            public void load(Object context) {
                dataLoader.load(context);
            }
        });
        return this;
    }


    @Override
    public void load(Object context) {
        this.taskList.forEach(ForkJoinTask::fork);
    }


    /**
     * 获取执行后的结果
     * @return
     */
    public T getContext() {
        this.taskList.forEach(ForkJoinTask::join);
        return this.context;
    }
}
