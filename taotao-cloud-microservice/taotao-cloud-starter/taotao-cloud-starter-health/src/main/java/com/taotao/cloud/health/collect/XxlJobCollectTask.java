package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.util.ContextUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;
/**
 * @author Huang Zhaoping
 */
public class XxlJobCollectTask extends AbstractCollectTask {

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.xxljob.timeSpan", 20);
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.xxljob.enabled", true);
    }

    @Override
    public String getDesc() {
        return "定时任务性能采集";
    }

    @Override
    public String getName() {
        return "xxljob.info";
    }

    @Override
   protected Object getData() {
        if (ContextUtils.getBean(ReflectionUtils.tryClassForName("com.xxl.job.core.executor.impl.XxlJobSpringExecutor"), false) == null) {
            return null;
        }
        JobInfo data = new JobInfo();
        data.count = ContextUtils.getApplicationContext().getBeanNamesForAnnotation(ReflectionUtils.classForName("com.xxl.job.core.handler.annotation.JobHandler")).length;
        return data;
    }

    @Data
    private static class JobInfo {
        @FieldReport(name = "xxljob.count", desc = "xxljob任务数量")
        private Integer count;
    }
}
