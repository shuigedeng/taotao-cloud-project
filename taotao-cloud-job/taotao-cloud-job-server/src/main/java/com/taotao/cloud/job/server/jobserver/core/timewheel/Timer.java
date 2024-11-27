package com.taotao.cloud.job.server.jobserver.core.timewheel;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface Timer {

    /**
     * 调度定时任务
     */
    TimerFuture schedule(TimerTask task, long delay, TimeUnit unit);

    /**
     * 停止所有调度任务
     */
    Set<TimerTask> stop();
}
