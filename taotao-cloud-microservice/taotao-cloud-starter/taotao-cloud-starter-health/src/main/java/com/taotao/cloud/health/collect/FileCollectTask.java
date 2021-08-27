package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.common.Collector;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;
import lombok.var;

/**
 * @author Huang Zhaoping
 */
public class FileCollectTask extends AbstractCollectTask {

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.file.timeSpan", 20);
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.file.enabled", true);
    }

    @Override
    public String getDesc() {
        return "File服务性能采集";
    }

    @Override
    public String getName() {
        return "file.info";
    }

    @Override
    protected Object getData() {
        FileInfo data = new FileInfo();
        if(!PropertyUtils.getPropertyCache("bsf.file.enabled",false)){
            data.provider = "file";
            var hook = (Collector.Hook)ReflectionUtils.callMethod(ReflectionUtils.classForName("com.yh.csx.bsf.file.impl.FileProviderMonitor"), "hook", null);
            data.hookCurrent = hook.getCurrent();
            data.hookError = hook.getLastErrorPerSecond();
            data.hookSuccess = hook.getLastSuccessPerSecond();
            data.hookList = hook.getMaxTimeSpanList().toText();
            data.hookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
        }
        return data;
    }

    @Data
    private static class FileInfo {
        @FieldReport(name = "file.provider", desc = "File服务提供者")
        private String provider;
        @FieldReport(name = "file.hook.error", desc = "File服务拦截上一次每秒出错次数")
        private Long hookError;
        @FieldReport(name = "file.hook.success", desc = "File服务拦截上一次每秒成功次数")
        private Long hookSuccess;
        @FieldReport(name = "file.hook.current", desc = "File服务拦截当前执行任务数")
        private Long hookCurrent;
        @FieldReport(name = "file.hook.list.detail", desc = "File服务拦截历史最大耗时任务列表")
        private String hookList;
        @FieldReport(name = "file.hook.list.minute.detail", desc = "File服务拦截历史最大耗时任务列表(每分钟)")
        private String hookListPerMinute;
    }
}
