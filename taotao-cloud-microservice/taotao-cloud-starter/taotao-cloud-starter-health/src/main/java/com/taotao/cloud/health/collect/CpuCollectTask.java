package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;

import com.sun.management.OperatingSystemMXBean;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;

import java.lang.management.ManagementFactory;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 19:07
 **/
public class CpuCollectTask extends AbstractCollectTask {
    OperatingSystemMXBean sysembean;
    public CpuCollectTask(){
        sysembean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    }

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.cpu.timeSpan",10);
    }

    @Override
    public String getDesc() {
        return "cpu采集";
    }

    @Override
    public String getName() {
        return "cpu.info";
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.cpu.enabled",true);
    }

    @Override
    protected Object getData() {
        CpuInfo info = new CpuInfo();
        info.setProcessCpuLoad(sysembean.getProcessCpuLoad());
        info.setSystemCpuLoad(sysembean.getSystemCpuLoad());
        info.setCpuCoreNumber(Runtime.getRuntime().availableProcessors());
        return info;
    }


    @Data
    private static class CpuInfo{
        @FieldReport(name = "cpu.process",desc = "进程cpu负载")
        private double processCpuLoad;
        @FieldReport(name = "cpu.system",desc = "系统cpu负载")
        private double systemCpuLoad;
        @FieldReport(name = "cpu.core.num",desc = "系统cpu核心数")
        private Integer cpuCoreNumber;
    }

}
