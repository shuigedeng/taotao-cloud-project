package com.taotao.cloud.sys.api.model.vo.monitor;

import org.dromara.hutoolcore.date.DatePattern;
import org.dromara.hutoolcore.date.DateUtil;
import org.dromara.hutoolcore.util.NumberUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import lombok.Data;

import java.lang.management.ManagementFactory;

/**
 * JVM相关信息
 */
@Data
public class JvmInfo {

    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    public double getTotal() {
        return NumberUtil.div(total, CommonConstant.MB, 2);
    }

    public double getMax() {
        return NumberUtil.div(max, CommonConstant.MB, 2);
    }

    public double getFree() {
        return NumberUtil.div(free, CommonConstant.MB, 2);
    }

    public double getUsed() {
        return NumberUtil.div(total - free, CommonConstant.MB, 2);
    }

    public double getUsage() {
        return NumberUtil.div((total - free) * 100, total, 2);
    }

    /**
     * 获取JDK名称
     */
    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    /**
     * JDK启动时间
     */
    public String getStartTime() {
        return DateUtil.format(DateUtil.date(ManagementFactory.getRuntimeMXBean().getStartTime()),
            DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * JDK运行时间
     */
    public String getRunTime() {
        return DateUtil.formatBetween(DateUtil.date(ManagementFactory.getRuntimeMXBean().getStartTime()),
            DateUtil.date());
    }

    /**
     * 运行参数
     */
    public String getInputArgs() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
    }
}
