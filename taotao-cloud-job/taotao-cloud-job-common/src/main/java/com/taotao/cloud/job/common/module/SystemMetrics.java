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

package com.taotao.cloud.job.common.module;

import lombok.Data;

/**
 * Class for system metrics.
 *
 * @author shuigedeng
 * @since 2020/3/25
 */
@Data
public class SystemMetrics implements Comparable<SystemMetrics> {

    /**
     * CPU processor num.
     */
    private int cpuProcessors;

    /**
     * Percent of CPU load.
     */
    private double cpuLoad;

    /**
     * Memory that is used by JVM, in GB.
     */
    private double jvmUsedMemory;

    /**
     * Max memory that JVM can use, in GB.
     */
    private double jvmMaxMemory;

    /**
     * Ratio of memory that JVM uses to total memory, 0.X,
     * the value is between 0 and 1.
     */
    private double jvmMemoryUsage;

    /**
     * Total used disk space, in GB.
     */
    private double diskUsed;

    /**
     * Total disk space, in GB.
     */
    private double diskTotal;

    /**
     * Used disk ratio.
     */
    private double diskUsage;

    /**
     * user-customized system metrics collector, eg. GPU usage
     * implement SystemMetricsCollector to set the value in worker side
     * implement WorkerFilter to filter the worker in server side
     */
    private String extra;

    /**
     * Score of cache.
     */
    private int score;

    /**
     * Override compareTo.
     *
     * @param that the metrics that is to be compared with current.
     * @return {@code int}
     */
    @Override
    public int compareTo(SystemMetrics that) {
        // Sort by metrics in descending order.
        return that.calculateScore() - this.calculateScore();
    }

    /**
     * Calculate score, based on CPU and memory info.
     *
     * @return score
     */
    public int calculateScore() {
        if (score > 0) {
            return score;
        }
        // Memory is vital to TaskTracker, so we set the multiplier factor as 2.
        double memScore = (jvmMaxMemory - jvmUsedMemory) * 2;
        // Calculate the remaining load of CPU. Multiplier is set as 1.
        double cpuScore = cpuProcessors - cpuLoad;
        // Windows can not fetch CPU load, set cpuScore as 1.
        if (cpuScore > cpuProcessors) {
            cpuScore = 1;
        }
        score = (int) (memScore + cpuScore);
        return score;
    }

    /**
     * Judge if the machine is available.
     *
     * @param minCPUCores Minimum available CPU cores.
     * @param minMemorySpace Minimum available memory size
     * @param minDiskSpace Minimum disk space
     * @return {@code boolean} whether the machine is available.
     */
    public boolean available(double minCPUCores, double minMemorySpace, double minDiskSpace) {

        double availableMemory = jvmMaxMemory - jvmUsedMemory;
        double availableDisk = diskTotal - diskUsed;

        if (availableMemory < minMemorySpace || availableDisk < minDiskSpace) {
            return false;
        }

        // 0 indicates the CPU is free, which is the optimal condition.
        // Negative number means being unable to fetch CPU info, return true.
        if (cpuLoad <= 0 || minCPUCores <= 0) {
            return true;
        }
        return minCPUCores < (cpuProcessors - cpuLoad);
    }
}
