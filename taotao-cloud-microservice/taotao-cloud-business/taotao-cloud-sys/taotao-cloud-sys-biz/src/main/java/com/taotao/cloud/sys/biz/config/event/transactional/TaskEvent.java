package com.taotao.cloud.sys.biz.config.event.transactional;

import org.springframework.context.ApplicationEvent;

/**
 * TaskEvent
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class TaskEvent extends ApplicationEvent {

    public TaskEvent( TaskRun source ) {
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
