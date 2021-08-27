package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.util.ContextUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

/**
 * @author Huang Zhaoping
 */
public class CatCollectTask extends AbstractCollectTask {

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.cat.timeSpan", 20);
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.cat.enabled", true);
    }

    @Override
    public String getDesc() {
        return "Cat监控性能采集";
    }

    @Override
    public String getName() {
        return "cat.info";
    }

    @Override
    protected Object getData() {
        CatInfo data = new CatInfo();
        if (ContextUtils.getBean(ReflectionUtils.tryClassForName("com.yh.csx.bsf.cat.CatConfiguration"), false) != null) {
            Class senderClass = ReflectionUtils.tryClassForName("com.dianping.cat.message.io.TcpSocketSender");
            if (senderClass != null) {
                Object sender = ReflectionUtils.callMethod(senderClass, "getInstance", null);
                Object messageQueue = ReflectionUtils.getFieldValue(sender, "messageQueue");
                if (messageQueue != null) {
                    data.queueSize = (Integer) ReflectionUtils.callMethod(messageQueue, "size", null);
                }
                Object statistics = ReflectionUtils.getFieldValue(sender, "statistics");
                if (statistics != null) {
                    Map<String, Long> values = ReflectionUtils.tryCallMethod(statistics, "getStatistics", null, Collections.emptyMap());
                    data.messageCount = values.getOrDefault("cat.status.message.produced", 0L);
                    data.overflowCount = values.getOrDefault("cat.status.message.overflowed", 0L);
                }
            }
        }
        return data;
    }

    @Data
    private static class CatInfo {
        @FieldReport(name = "cat.queue.size", desc = "Cat监控队列大小")
        private Integer queueSize;
        @FieldReport(name = "cat.message.count", desc = "Cat产生消息数量")
        private Long messageCount;
        @FieldReport(name = "cat.overflow.count", desc = "Cat队列丢弃数量")
        private Long overflowCount;
    }
}
