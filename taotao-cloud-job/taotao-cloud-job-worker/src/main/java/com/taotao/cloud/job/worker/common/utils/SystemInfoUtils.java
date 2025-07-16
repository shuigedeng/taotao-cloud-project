/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.job.worker.common.utils;

import com.taotao.cloud.job.common.module.SystemMetrics;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * 系统信息工具，用于采集监控指标
 *
 * @author shuigedeng
 * @since 2020/3/25
 */
public class SystemInfoUtils {

    private static final NumberFormat NF = NumberFormat.getNumberInstance();

    static {
        NF.setMaximumFractionDigits(4);
        NF.setMinimumFractionDigits(4);
        NF.setRoundingMode(RoundingMode.HALF_UP);
        // 不按照千分位输出
        NF.setGroupingUsed(false);
    }

    // JMX bean can be accessed externally and is meant for management tools like hyperic ( or even
    // nagios ) - It would delegate to Runtime anyway.
    private static final Runtime runtime = Runtime.getRuntime();
    private static final OperatingSystemMXBean osMXBean =
            ManagementFactory.getOperatingSystemMXBean();

    public static SystemMetrics getSystemMetrics() {

        SystemMetrics metrics = new SystemMetrics();

        fillCPUInfo(metrics);
        fillMemoryInfo(metrics);
        fillDiskInfo(metrics);

        // 在Worker完成分数计算，减小Server压力
        metrics.calculateScore();
        return metrics;
    }

    private static void fillCPUInfo(SystemMetrics metrics) {
        metrics.setCpuProcessors(osMXBean.getAvailableProcessors());
        metrics.setCpuLoad(miniDouble(osMXBean.getSystemLoadAverage()));
    }

    private static void fillMemoryInfo(SystemMetrics metrics) {
        // JVM内存信息(maxMemory指JVM能从操作系统获取的最大内存，即-Xmx参数设置的值，totalMemory指JVM当前持久的总内存)
        long maxMemory = runtime.maxMemory();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        metrics.setJvmMaxMemory(bytes2GB(maxMemory));
        // 已使用内存：当前申请总量 - 当前空余量
        metrics.setJvmUsedMemory(bytes2GB(usedMemory));
        // 已用内存比例
        metrics.setJvmMemoryUsage(miniDouble((double) usedMemory / maxMemory));
    }

    private static void fillDiskInfo(SystemMetrics metrics) {
        long free = 0;
        long total = 0;
        File[] roots = File.listRoots();
        for (File file : roots) {
            free += file.getFreeSpace();
            total += file.getTotalSpace();
        }

        metrics.setDiskUsed(bytes2GB(total - free));
        // 防止内存溢出导致total为负数,导致找不到worker实例
        metrics.setDiskTotal(bytes2GB(total < 0 ? Long.MAX_VALUE >> 6 : total));
        metrics.setDiskUsage(miniDouble(metrics.getDiskUsed() / metrics.getDiskTotal()));
    }

    private static double bytes2GB(long bytes) {
        return miniDouble(bytes / 1024.0 / 1024 / 1024);
    }

    private static double miniDouble(double origin) {
        return Double.parseDouble(NF.format(origin));
    }
}
