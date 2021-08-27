package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.base.BsfException;
import com.yh.csx.bsf.core.common.Collector;
import com.yh.csx.bsf.core.util.ContextUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;
import lombok.var;

/**
 * @author Huang Zhaoping
 */
public class RocketMQCollectTask extends AbstractCollectTask {

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.rocketmq.timeSpan", 20);
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.rocketmq.enabled", true);
    }

    @Override
    public String getDesc() {
        return "RocketMQ性能采集";
    }

    @Override
    public String getName() {
        return "rocketmq.info";
    }

    @Override
    protected Object getData() {
        RocketMQInfo data = new RocketMQInfo();
        if (ContextUtils.getBean(ReflectionUtils.tryClassForName("com.yh.csx.bsf.mq.rocketmq.RocketMQConsumerProvider"), false) != null) {
            var hook = getCollectorHook("com.yh.csx.bsf.mq.rocketmq.RocketMQMonitor");
            data.consumerHookCurrent = hook.getCurrent();
            data.consumerHookError = hook.getLastErrorPerSecond();
            data.consumerHookSuccess = hook.getLastSuccessPerSecond();
            data.consumerHookList = hook.getMaxTimeSpanList().toText();
            data.consumerHookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
        }
        if (ContextUtils.getBean(ReflectionUtils.tryClassForName("com.yh.csx.bsf.mq.rocketmq.RocketMQProducerProvider"), false) != null) {
            var hook = getCollectorHook("com.yh.csx.bsf.mq.rocketmq.RocketMQMonitor");
            data.producerHookCurrent = hook.getCurrent();
            data.producerHookError = hook.getLastErrorPerSecond();
            data.producerHookSuccess = hook.getLastSuccessPerSecond();
            data.producerHookList = hook.getMaxTimeSpanList().toText();
            data.producerHookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
        }
        return data;
    }

    private Collector.Hook getCollectorHook(String className) {
        Class<?> monitor = ReflectionUtils.classForName(className);
        try {
            return (Collector.Hook) ReflectionUtils.findMethod(monitor, "hook").invoke(null);
        } catch (Exception e) {
            throw new BsfException(e);
        }
    }

    @Data
    private static class RocketMQInfo {
        @FieldReport(name = "rocketmq.consumer.hook.error", desc = "Consumer拦截上一次每秒出错次数")
        private Long consumerHookError;
        @FieldReport(name = "rocketmq.consumer.hook.success", desc = "Consumer拦截上一次每秒成功次数")
        private Long consumerHookSuccess;
        @FieldReport(name = "rocketmq.consumer.hook.current", desc = "Consumer拦截当前执行任务数")
        private Long consumerHookCurrent;
        @FieldReport(name = "rocketmq.consumer.hook.list.detail", desc = "Consumer拦截历史最大耗时任务列表")
        private String consumerHookList;
        @FieldReport(name = "rocketmq.consumer.hook.list.minute.detail", desc = "Consumer拦截历史最大耗时任务列表(每分钟)")
        private String consumerHookListPerMinute;

        @FieldReport(name = "rocketmq.producer.hook.error", desc = "Producer拦截上一次每秒出错次数")
        private Long producerHookError;
        @FieldReport(name = "rocketmq.producer.hook.success", desc = "Producer拦截上一次每秒成功次数")
        private Long producerHookSuccess;
        @FieldReport(name = "rocketmq.producer.hook.current", desc = "Producer拦截当前执行任务数")
        private Long producerHookCurrent;
        @FieldReport(name = "rocketmq.producer.hook.list.detail", desc = "Producer拦截历史最大耗时任务列表")
        private String producerHookList;
        @FieldReport(name = "rocketmq.producer.hook.list.minute.detail", desc = "Producer拦截历史最大耗时任务列表(每分钟)")
        private String producerHookListPerMinute;
    }
}
