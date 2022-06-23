package com.taotao.cloud.sys.api.web.dto.jvm;

import com.sun.management.GarbageCollectorMXBean;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * jvm 监控中, 聚合监控, 将 7 个接口合并成一个
 */
public class AggregationVMInfo {
    private OperatingSystemMXBean system;
    private RuntimeMXBean runtime;
    private CompilationMXBean compilation;
    private ThreadMXBean thread;
    private ClassLoadingMXBean classLoading;
    private MemoryMXBean memory;
    private List<VMParam> flags = new ArrayList<>();
    private List<GarbageCollectorMXBean> garbageCollectors = new ArrayList<>();

	public OperatingSystemMXBean getSystem() {
		return system;
	}

	public void setSystem(OperatingSystemMXBean system) {
		this.system = system;
	}

	public RuntimeMXBean getRuntime() {
		return runtime;
	}

	public void setRuntime(RuntimeMXBean runtime) {
		this.runtime = runtime;
	}

	public CompilationMXBean getCompilation() {
		return compilation;
	}

	public void setCompilation(CompilationMXBean compilation) {
		this.compilation = compilation;
	}

	public ThreadMXBean getThread() {
		return thread;
	}

	public void setThread(ThreadMXBean thread) {
		this.thread = thread;
	}

	public ClassLoadingMXBean getClassLoading() {
		return classLoading;
	}

	public void setClassLoading(ClassLoadingMXBean classLoading) {
		this.classLoading = classLoading;
	}

	public MemoryMXBean getMemory() {
		return memory;
	}

	public void setMemory(MemoryMXBean memory) {
		this.memory = memory;
	}

	public List<VMParam> getFlags() {
		return flags;
	}

	public void setFlags(List<VMParam> flags) {
		this.flags = flags;
	}

	public List<GarbageCollectorMXBean> getGarbageCollectors() {
		return garbageCollectors;
	}

	public void setGarbageCollectors(
		List<GarbageCollectorMXBean> garbageCollectors) {
		this.garbageCollectors = garbageCollectors;
	}
}
