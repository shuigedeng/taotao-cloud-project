package com.taotao.cloud.sys.biz.tools.jvm.controller;

import com.sanri.tools.modules.jvm.controller.dtos.AggregationVMInfo;
import com.sanri.tools.modules.jvm.service.DiagnosticCommandService;
import com.sanri.tools.modules.jvm.service.MBeanMonitorService;
import com.sanri.tools.modules.jvm.service.dtos.VMParam;
import com.sun.management.GarbageCollectorMXBean;
import com.sun.management.GcInfo;
import com.sun.management.OperatingSystemMXBean;
import com.sun.management.ThreadMXBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.lang.management.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/jvm")
@Validated
@Slf4j
public class AggregationController {

    @Autowired
    private MBeanMonitorService mBeanMonitorService;
    @Autowired
    private DiagnosticCommandService diagnosticCommandService;

    /**
     * 监控数据列表
     * 系统,运行时数据,编译,内存,线程,类加载器,vm参数
     */
    @GetMapping("/dashboard")
    public AggregationVMInfo dashboard(@NotBlank final String connName) throws ExecutionException, InterruptedException, IntrospectionException, ReflectionException, InstanceNotFoundException, IOException {
        final AggregationVMInfo aggregationVMInfo = new AggregationVMInfo();
        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    try {
                        final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class.getName());
                        aggregationVMInfo.setSystem((OperatingSystemMXBean) proxyMXBean);
                    } catch (IOException | ClassNotFoundException e) {
                        log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
                    }
                }),
                CompletableFuture.runAsync(() -> {
                    try {
                        final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName, ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class.getName());
                        aggregationVMInfo.setRuntime((RuntimeMXBean) proxyMXBean);
                    } catch (IOException | ClassNotFoundException e) {
                        log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
                    }
                }),
                CompletableFuture.runAsync(() -> {
                    try {
                        final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName, ManagementFactory.COMPILATION_MXBEAN_NAME, CompilationMXBean.class.getName());
                        aggregationVMInfo.setCompilation((CompilationMXBean) proxyMXBean);
                    } catch (IOException | ClassNotFoundException e) {
                        log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
                    }
                }),
                CompletableFuture.runAsync(() -> {
                    try {
                        final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName, ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class.getName());
                        aggregationVMInfo.setThread((ThreadMXBean) proxyMXBean);
                    } catch (IOException | ClassNotFoundException e) {
                        log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
                    }
                }),
                CompletableFuture.runAsync(() -> {
                    try {
                        final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName, ManagementFactory.CLASS_LOADING_MXBEAN_NAME, ClassLoadingMXBean.class.getName());
                        aggregationVMInfo.setClassLoading((ClassLoadingMXBean) proxyMXBean);
                    } catch (IOException | ClassNotFoundException e) {
                        log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
                    }
                }),
                CompletableFuture.runAsync(() -> {
                    try {
                        final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName, ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class.getName());
                        aggregationVMInfo.setMemory((MemoryMXBean) proxyMXBean);
                    } catch (IOException | ClassNotFoundException e) {
                        log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
                    }
                }),
                CompletableFuture.runAsync(() -> {
                    try {
                        final List<VMParam> vmParams = diagnosticCommandService.flagsSetted(connName);
                        aggregationVMInfo.setFlags(vmParams);
                    } catch (Exception e) {
                        log.info("执行 flagsSetted 获取时异常:{}", e.getMessage(), e);
                    }
                }),
                CompletableFuture.runAsync(() -> {
                    try {
                        final List<PlatformManagedObject> platformManagedObjects = mBeanMonitorService.proxyMXBeans(connName, GarbageCollectorMXBean.class.getName());
                        List<GarbageCollectorMXBean> garbageCollectorMXBeans = new ArrayList<>();
                        for (PlatformManagedObject platformManagedObject : platformManagedObjects) {
                            garbageCollectorMXBeans.add((GarbageCollectorMXBean) platformManagedObject);
                        }
                        aggregationVMInfo.setGarbageCollectors(garbageCollectorMXBeans);
                    } catch (IOException | ClassNotFoundException e) {
                        log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
                    }

                })
        );
        voidCompletableFuture.get();
        return aggregationVMInfo;
    }
}
