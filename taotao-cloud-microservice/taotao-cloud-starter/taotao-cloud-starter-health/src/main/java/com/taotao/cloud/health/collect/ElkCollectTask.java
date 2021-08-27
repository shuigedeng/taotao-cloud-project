package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.util.ContextUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;
import lombok.val;

/**
 * @author Huang Zhaoping
 */
public class ElkCollectTask extends AbstractCollectTask {

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.elk.timeSpan", 20);
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.elk.enabled", true);
    }

    @Override
    public String getDesc() {
        return "ELK性能采集";
    }

    @Override
    public String getName() {
        return "elk.info";
    }

    @Override
   protected Object getData() {
        val appender = ContextUtils.getBean(ReflectionUtils.tryClassForName("net.logstash.logback.appender.LogstashTcpSocketAppender"), false);
        if (appender == null) {
            return null;
        }
        ElkInfo data = new ElkInfo();
        data.queueSize = ReflectionUtils.tryGetValue(appender, "getQueueSize");
        data.consecutiveDropped = ReflectionUtils.tryGetValue(appender, "consecutiveDroppedCount.get");
        return data;
    }

    @Data
    private static class ElkInfo {
        @FieldReport(name = "elk.queue.size", desc = "ELK消息队列大小")
        private Integer queueSize;
        @FieldReport(name = "elk.consecutiveDropped", desc = "ELK消息连续丢弃数量")
        private Long consecutiveDropped;
    }
}
