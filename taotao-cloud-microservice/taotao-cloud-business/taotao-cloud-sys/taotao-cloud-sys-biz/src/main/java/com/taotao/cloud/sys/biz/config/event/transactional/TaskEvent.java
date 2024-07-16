package com.taotao.cloud.sys.biz.config.event.transactional;

import org.springframework.context.ApplicationEvent;

public class TaskEvent extends ApplicationEvent {

    public TaskEvent(TaskRun source) {
        super(source);
    }

    @Override
    public TaskRun getSource() {
        return (TaskRun) super.getSource();
    }


    @FunctionalInterface
    public interface TaskRun {
        void run();
    }
}
