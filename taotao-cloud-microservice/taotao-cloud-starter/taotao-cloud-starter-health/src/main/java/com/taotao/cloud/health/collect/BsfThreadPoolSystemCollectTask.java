package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.common.Collector;
import com.yh.csx.bsf.core.util.ContextUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;
import lombok.val;

/**
 * bsf 系统连接池采集任务
 * @author: chejiangyi
 * @version: 2019-07-30 21:14
 **/
public class BsfThreadPoolSystemCollectTask extends AbstractCollectTask {

    public BsfThreadPoolSystemCollectTask() {
    }

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.bsf.threadPool.timeSpan", 20);
    }

    @Override
    public String getDesc() {
        return "bsf系统线程池采集";
    }

    @Override
    public String getName() {
        return "bsf.threadPool.info";
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.bsf.threadPool.enabled", true);
    }

    @Override
    protected Object getData() {
        if(ContextUtils.getBean(ReflectionUtils.classForName("com.yh.csx.bsf.core.thread.ThreadMonitor"),false)==null)
        {return null;}
        SystemThreadPoolInfo info = new SystemThreadPoolInfo();
        info.setSystemActiveCount((Integer)Collector.Default.call("bsf.threadPool.system.active.count").run());
        info.setSystemCorePoolSize((Integer) Collector.Default.call("bsf.threadPool.system.core.poolSize").run());
        info.setSystemPoolSizeLargest((Integer) Collector.Default.call("bsf.threadPool.system.poolSize.largest").run());
       info.setSystemPoolSizeMax((Integer) Collector.Default.call("bsf.threadPool.system.poolSize.max").run());
       info.setSystemPoolSizeCount((Integer)Collector.Default.call("bsf.threadPool.system.poolSize.count").run());
       info.setSystemQueueSize((Integer)Collector.Default.call("bsf.threadPool.system.queue.size").run());
       info.setSystemTaskCount((Long) Collector.Default.call("bsf.threadPool.system.task.count").run());
       info.setSystemTaskCompleted((Long) Collector.Default.call("bsf.threadPool.system.task.completed").run());
       val hook = Collector.Default.hook("bsf.threadPool.system.hook");
       info.setSystemTaskHookCurrent(hook.getCurrent());
        info.setSystemTaskHookError(hook.getLastErrorPerSecond());
        info.setSystemTaskHookSuccess(hook.getLastSuccessPerSecond());
        info.setSystemTaskHookList(hook.getMaxTimeSpanList().toText());
        info.setSystemTaskHookListPerMinute(hook.getMaxTimeSpanListPerMinute().toText());
        return info;
    }


    @Data
    private static class SystemThreadPoolInfo {
        @FieldReport(name = "bsf.threadPool.system.active.count", desc = "bsf系统线程池活动线程数")
        private Integer systemActiveCount;
        @FieldReport(name = "bsf.threadPool.system.core.poolSize", desc = "bsf系统线程池核心线程数")
        private Integer systemCorePoolSize;
        @FieldReport(name = "bsf.threadPool.system.poolSize.largest", desc = "bsf线程池历史最大线程数")
        private Integer systemPoolSizeLargest;
        @FieldReport(name = "bsf.threadPool.system.poolSize.max", desc = "bsf线程池最大线程数")
        private Integer systemPoolSizeMax;
        @FieldReport(name = "bsf.threadPool.system.poolSize.count", desc = "bsf线程池当前线程数")
        private Integer systemPoolSizeCount;
        @FieldReport(name = "bsf.threadPool.system.queue.size", desc = "bsf线程池当前排队等待任务数")
        private Integer systemQueueSize;
        @FieldReport(name = "bsf.threadPool.system.task.count", desc = "bsf线程池历史任务数")
        private Long systemTaskCount;
        @FieldReport(name = "bsf.threadPool.system.task.completed", desc = "bsf线程池已完成任务数")
        private Long systemTaskCompleted;
        @FieldReport(name = "bsf.threadPool.system.task.hook.error", desc = "bsf线程池拦截上一次每秒出错次数")
        private Long systemTaskHookError;
        @FieldReport(name = "bsf.threadPool.system.task.hook.success", desc = "bsf线程池拦截上一次每秒成功次数")
        private Long systemTaskHookSuccess;
        @FieldReport(name = "bsf.threadPool.system.task.hook.current", desc = "bsf线程池拦截当前执行任务数")
        private Long systemTaskHookCurrent;
        @FieldReport(name = "bsf.threadPool.system.task.hook.list.detail", desc = "bsf线程池拦截历史最大耗时任务列表")
        private String systemTaskHookList;
        @FieldReport(name = "bsf.threadPool.system.task.hook.list.minute.detail", desc = "bsf线程池拦截历史最大耗时任务列表(每分钟)")
        private String systemTaskHookListPerMinute;
    }
}
