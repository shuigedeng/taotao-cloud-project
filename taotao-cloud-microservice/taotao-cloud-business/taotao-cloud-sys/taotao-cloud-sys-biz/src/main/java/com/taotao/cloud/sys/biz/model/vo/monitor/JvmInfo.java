package com.taotao.cloud.sys.biz.model.vo.monitor;

import com.taotao.boot.common.constant.CommonConstants;
import com.taotao.boot.common.constant.CommonConstants;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.math.NumberUtil;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;

import static com.taotao.boot.common.model.DatePattern.NORM_DATETIME_PATTERN;

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

    public BigDecimal getTotal() {
        return NumberUtil.div(total, CommonConstants.MB, 2);
    }

    public BigDecimal getMax() {
        return NumberUtil.div(max, CommonConstants.MB, 2);
    }

    public BigDecimal getFree() {
        return NumberUtil.div(free, CommonConstants.MB, 2);
    }

    public BigDecimal getUsed() {
        return NumberUtil.div(total - free, CommonConstants.MB, 2);
    }

    public BigDecimal getUsage() {
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
            NORM_DATETIME_PATTERN);
    }

    /**
     * JDK运行时间
     */
    public String getRunTime() {
        return DateUtil.formatBetween(DateUtil.date(ManagementFactory.getRuntimeMXBean().getStartTime()),
            DateUtil.now());
    }

    /**
     * 运行参数
     */
    public String getInputArgs() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
    }
}
