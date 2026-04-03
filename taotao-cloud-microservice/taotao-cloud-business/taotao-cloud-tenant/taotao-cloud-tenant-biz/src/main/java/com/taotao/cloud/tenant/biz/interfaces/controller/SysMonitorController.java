package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.lang.management.*;
import java.util.*;

/**
 * 系统监控Controller
 * 提供服务器、JVM、CPU、内存、磁盘等监控指标
 */
@Slf4j
@RestController
@RequestMapping("/system/monitor")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysMonitorController {

    /**
     * 获取系统整体监控信息
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // CPU信息
        info.put("cpu", getCpuInfo());
        
        // 内存信息
        info.put("memory", getMemoryInfo());
        
        // JVM信息
        info.put("jvm", getJvmInfo());
        
        // 磁盘信息
        info.put("disk", getDiskInfo());
        
        // 服务器信息
        info.put("server", getServerInfo());
        
        return Result.success(info);
    }

    /**
     * 获取CPU信息
     */
    @GetMapping("/cpu")
    public Result<Map<String, Object>> getCpu() {
        return Result.success(getCpuInfo());
    }

    /**
     * 获取内存信息
     */
    @GetMapping("/memory")
    public Result<Map<String, Object>> getMemory() {
        return Result.success(getMemoryInfo());
    }

    /**
     * 获取JVM信息
     */
    @GetMapping("/jvm")
    public Result<Map<String, Object>> getJvm() {
        return Result.success(getJvmInfo());
    }

    /**
     * 获取磁盘信息
     */
    @GetMapping("/disk")
    public Result<List<Map<String, Object>>> getDisk() {
        return Result.success(getDiskInfo());
    }

    /**
     * 获取服务器信息
     */
    @GetMapping("/server")
    public Result<Map<String, Object>> getServer() {
        return Result.success(getServerInfo());
    }

    /**
     * 获取CPU详细信息
     */
    private Map<String, Object> getCpuInfo() {
        Map<String, Object> cpuInfo = new HashMap<>();
        
        try {
            // 获取操作系统MXBean
            OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
            
            // CPU核心数
            int availableProcessors = osMXBean.getAvailableProcessors();
            cpuInfo.put("cores", availableProcessors);
            
            // 系统负载（1分钟平均负载）
            double systemLoadAverage = osMXBean.getSystemLoadAverage();
            cpuInfo.put("systemLoadAverage", systemLoadAverage);
            
            // 尝试获取更详细的CPU使用率（通过com.sun.management.OperatingSystemMXBean）
            try {
                if (osMXBean instanceof com.sun.management.OperatingSystemMXBean) {
                    com.sun.management.OperatingSystemMXBean sunOsMXBean = 
                        (com.sun.management.OperatingSystemMXBean) osMXBean;
                    
                    // 进程CPU使用率
                    double processCpuLoad = sunOsMXBean.getProcessCpuLoad();
                    cpuInfo.put("processCpuLoad", processCpuLoad >= 0 ? String.format("%.2f%%", processCpuLoad * 100) : "N/A");
                    
                    // 系统CPU使用率
                    double systemCpuLoad = sunOsMXBean.getSystemCpuLoad();
                    cpuInfo.put("systemCpuLoad", systemCpuLoad >= 0 ? String.format("%.2f%%", systemCpuLoad * 100) : "N/A");
                    
                    // 进程CPU时间（毫秒）
                    long processCpuTime = sunOsMXBean.getProcessCpuTime();
                    cpuInfo.put("processCpuTimeMs", processCpuTime / 1_000_000);
                }
            } catch (Exception e) {
                log.warn("获取详细CPU信息失败", e);
            }
            
            // 操作系统信息
            cpuInfo.put("osName", osMXBean.getName());
            cpuInfo.put("osVersion", osMXBean.getVersion());
            cpuInfo.put("osArch", osMXBean.getArch());
            
        } catch (Exception e) {
            log.error("获取CPU信息失败", e);
            cpuInfo.put("error", "获取CPU信息失败: " + e.getMessage());
        }
        
        return cpuInfo;
    }

    /**
     * 获取内存信息
     */
    private Map<String, Object> getMemoryInfo() {
        Map<String, Object> memoryInfo = new HashMap<>();
        
        try {
            // 运行时内存信息
            Runtime runtime = Runtime.getRuntime();
            
            // 堆内存信息
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            long maxMemory = runtime.maxMemory();
            
            memoryInfo.put("heapTotal", formatBytes(totalMemory));
            memoryInfo.put("heapUsed", formatBytes(usedMemory));
            memoryInfo.put("heapFree", formatBytes(freeMemory));
            memoryInfo.put("heapMax", formatBytes(maxMemory));
            memoryInfo.put("heapUsedPercent", String.format("%.2f%%", (double) usedMemory / maxMemory * 100));
            
            // 尝试获取物理内存信息
            try {
                OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
                if (osMXBean instanceof com.sun.management.OperatingSystemMXBean) {
                    com.sun.management.OperatingSystemMXBean sunOsMXBean = 
                        (com.sun.management.OperatingSystemMXBean) osMXBean;
                    
                    long totalPhysicalMemory = sunOsMXBean.getTotalPhysicalMemorySize();
                    long freePhysicalMemory = sunOsMXBean.getFreePhysicalMemorySize();
                    long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
                    
                    memoryInfo.put("physicalTotal", formatBytes(totalPhysicalMemory));
                    memoryInfo.put("physicalUsed", formatBytes(usedPhysicalMemory));
                    memoryInfo.put("physicalFree", formatBytes(freePhysicalMemory));
                    memoryInfo.put("physicalUsedPercent", String.format("%.2f%%", 
                        (double) usedPhysicalMemory / totalPhysicalMemory * 100));
                    
                    // 交换空间
                    long totalSwapSpace = sunOsMXBean.getTotalSwapSpaceSize();
                    long freeSwapSpace = sunOsMXBean.getFreeSwapSpaceSize();
                    long usedSwapSpace = totalSwapSpace - freeSwapSpace;
                    
                    memoryInfo.put("swapTotal", formatBytes(totalSwapSpace));
                    memoryInfo.put("swapUsed", formatBytes(usedSwapSpace));
                    memoryInfo.put("swapFree", formatBytes(freeSwapSpace));
                    if (totalSwapSpace > 0) {
                        memoryInfo.put("swapUsedPercent", String.format("%.2f%%", 
                            (double) usedSwapSpace / totalSwapSpace * 100));
                    }
                }
            } catch (Exception e) {
                log.warn("获取物理内存信息失败", e);
            }
            
            // 内存池信息
            List<Map<String, Object>> memoryPools = new ArrayList<>();
            List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
            for (MemoryPoolMXBean pool : memoryPoolMXBeans) {
                Map<String, Object> poolInfo = new HashMap<>();
                poolInfo.put("name", pool.getName());
                poolInfo.put("type", pool.getType().name());
                
                MemoryUsage usage = pool.getUsage();
                poolInfo.put("used", formatBytes(usage.getUsed()));
                poolInfo.put("committed", formatBytes(usage.getCommitted()));
                poolInfo.put("max", formatBytes(usage.getMax()));
                
                memoryPools.add(poolInfo);
            }
            memoryInfo.put("pools", memoryPools);
            
        } catch (Exception e) {
            log.error("获取内存信息失败", e);
            memoryInfo.put("error", "获取内存信息失败: " + e.getMessage());
        }
        
        return memoryInfo;
    }

    /**
     * 获取JVM信息
     */
    private Map<String, Object> getJvmInfo() {
        Map<String, Object> jvmInfo = new HashMap<>();
        
        try {
            // 运行时信息
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            
            jvmInfo.put("vmName", runtimeMXBean.getVmName());
            jvmInfo.put("vmVersion", runtimeMXBean.getVmVersion());
            jvmInfo.put("vmVendor", runtimeMXBean.getVmVendor());
            jvmInfo.put("startTime", new Date(runtimeMXBean.getStartTime()));
            jvmInfo.put("uptime", formatDuration(runtimeMXBean.getUptime()));
            
            // 输入参数
            List<String> inputArguments = runtimeMXBean.getInputArguments();
            jvmInfo.put("inputArguments", inputArguments);
            
            // 系统属性
            Map<String, String> systemProperties = runtimeMXBean.getSystemProperties();
            jvmInfo.put("javaVersion", systemProperties.get("java.version"));
            jvmInfo.put("javaHome", systemProperties.get("java.home"));
            jvmInfo.put("userDir", systemProperties.get("user.dir"));
            jvmInfo.put("userName", systemProperties.get("user.name"));
            
            // 类加载信息
            ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
            jvmInfo.put("loadedClassCount", classLoadingMXBean.getLoadedClassCount());
            jvmInfo.put("totalLoadedClassCount", classLoadingMXBean.getTotalLoadedClassCount());
            jvmInfo.put("unloadedClassCount", classLoadingMXBean.getUnloadedClassCount());
            
            // 线程信息
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            jvmInfo.put("threadCount", threadMXBean.getThreadCount());
            jvmInfo.put("peakThreadCount", threadMXBean.getPeakThreadCount());
            jvmInfo.put("daemonThreadCount", threadMXBean.getDaemonThreadCount());
            jvmInfo.put("totalStartedThreadCount", threadMXBean.getTotalStartedThreadCount());
            
            // 垃圾收集器信息
            List<Map<String, Object>> gcCollectors = new ArrayList<>();
            List<GarbageCollectorMXBean> gcMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
            for (GarbageCollectorMXBean gc : gcMXBeans) {
                Map<String, Object> gcInfo = new HashMap<>();
                gcInfo.put("name", gc.getName());
                gcInfo.put("collectionCount", gc.getCollectionCount());
                gcInfo.put("collectionTime", gc.getCollectionTime() + "ms");
                gcCollectors.add(gcInfo);
            }
            jvmInfo.put("garbageCollectors", gcCollectors);
            
            // 编译器信息
            CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
            if (compilationMXBean != null) {
                jvmInfo.put("compilerName", compilationMXBean.getName());
                jvmInfo.put("totalCompilationTime", compilationMXBean.getTotalCompilationTime() + "ms");
            }
            
        } catch (Exception e) {
            log.error("获取JVM信息失败", e);
            jvmInfo.put("error", "获取JVM信息失败: " + e.getMessage());
        }
        
        return jvmInfo;
    }

    /**
     * 获取磁盘信息
     */
    private List<Map<String, Object>> getDiskInfo() {
        List<Map<String, Object>> diskList = new ArrayList<>();
        
        try {
            File[] roots = File.listRoots();
            for (File root : roots) {
                Map<String, Object> diskInfo = new HashMap<>();
                
                diskInfo.put("path", root.getAbsolutePath());
                diskInfo.put("totalSpace", formatBytes(root.getTotalSpace()));
                diskInfo.put("freeSpace", formatBytes(root.getFreeSpace()));
                diskInfo.put("usableSpace", formatBytes(root.getUsableSpace()));
                diskInfo.put("usedSpace", formatBytes(root.getTotalSpace() - root.getFreeSpace()));
                
                double usedPercent = (double) (root.getTotalSpace() - root.getFreeSpace()) / root.getTotalSpace() * 100;
                diskInfo.put("usedPercent", String.format("%.2f%%", usedPercent));
                
                diskList.add(diskInfo);
            }
        } catch (Exception e) {
            log.error("获取磁盘信息失败", e);
            Map<String, Object> errorInfo = new HashMap<>();
            errorInfo.put("error", "获取磁盘信息失败: " + e.getMessage());
            diskList.add(errorInfo);
        }
        
        return diskList;
    }

    /**
     * 获取服务器信息
     */
    private Map<String, Object> getServerInfo() {
        Map<String, Object> serverInfo = new HashMap<>();
        
        try {
            // 主机名
            serverInfo.put("hostName", java.net.InetAddress.getLocalHost().getHostName());
            // IP地址
            serverInfo.put("hostAddress", java.net.InetAddress.getLocalHost().getHostAddress());
            
            // 操作系统
            serverInfo.put("osName", System.getProperty("os.name"));
            serverInfo.put("osVersion", System.getProperty("os.version"));
            serverInfo.put("osArch", System.getProperty("os.arch"));
            
            // 用户目录
            serverInfo.put("userDir", System.getProperty("user.dir"));
            
            // 时区
            serverInfo.put("timeZone", TimeZone.getDefault().getID());
            
            // 当前时间
            serverInfo.put("currentTime", new Date());
            
        } catch (Exception e) {
            log.error("获取服务器信息失败", e);
            serverInfo.put("error", "获取服务器信息失败: " + e.getMessage());
        }
        
        return serverInfo;
    }

    /**
     * 格式化字节数为可读格式
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }

    /**
     * 格式化持续时间
     */
    private String formatDuration(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return String.format("%d天 %d小时 %d分钟", days, hours % 24, minutes % 60);
        } else if (hours > 0) {
            return String.format("%d小时 %d分钟", hours, minutes % 60);
        } else if (minutes > 0) {
            return String.format("%d分钟 %d秒", minutes, seconds % 60);
        } else {
            return String.format("%d秒", seconds);
        }
    }
}
