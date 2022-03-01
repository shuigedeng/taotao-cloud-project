package com.taotao.cloud.sys.biz.tools.jvm.controller.dtos;

import com.sanri.tools.modules.jvm.service.dtos.VMParam;
import lombok.Data;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.List;
import com.sun.management.GarbageCollectorMXBean;
import com.sun.management.OperatingSystemMXBean;

/**
 * jvm 监控中, 聚合监控, 将 7 个接口合并成一个
 */
@Data
public class AggregationVMInfo {
    private OperatingSystemMXBean system;
    private RuntimeMXBean runtime;
    private CompilationMXBean compilation;
    private ThreadMXBean thread;
    private ClassLoadingMXBean classLoading;
    private MemoryMXBean memory;
    private List<VMParam> flags = new ArrayList<>();
    private List<GarbageCollectorMXBean> garbageCollectors = new ArrayList<>();
}
