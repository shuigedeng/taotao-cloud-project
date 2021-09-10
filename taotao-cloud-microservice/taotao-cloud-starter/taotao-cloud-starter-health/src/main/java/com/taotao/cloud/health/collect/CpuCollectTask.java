/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.health.collect;

import com.sun.management.OperatingSystemMXBean;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.lang.management.ManagementFactory;

/**
 * CpuCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:23:08
 */
public class CpuCollectTask extends AbstractCollectTask {

	private OperatingSystemMXBean sysembean;
	private CollectTaskProperties properties;

	public CpuCollectTask(CollectTaskProperties properties) {
		this.sysembean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getCpuTimeSpan();
	}

	@Override
	public String getDesc() {
		return "CpuCollectTask";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.cpu";
	}

	@Override
	public boolean getEnabled() {
		return properties.isCpuEnabled();
	}

	@Override
	protected Object getData() {
		try {
			CpuInfo info = new CpuInfo();
			info.processCpuLoad = sysembean.getProcessCpuLoad();
			info.systemCpuLoad = sysembean.getSystemCpuLoad();

			info.committedVirtualMemorySize = sysembean.getCommittedVirtualMemorySize();
			info.totalSwapSpaceSize = sysembean.getTotalSwapSpaceSize();
			info.freeSwapSpaceSize = sysembean.getFreeSwapSpaceSize();
			info.processCpuTime = sysembean.getProcessCpuTime();
			info.freePhysicalMemorySize = sysembean.getFreePhysicalMemorySize();
			info.totalPhysicalMemorySize = sysembean.getTotalPhysicalMemorySize();

			info.cpuCoreNumber = Runtime.getRuntime().availableProcessors();
			return info;
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}

	private static class CpuInfo {

		@FieldReport(name = "taotao.cloud.health.collect.cpu.process", desc = "进程cpu负载")
		private double processCpuLoad;
		@FieldReport(name = "taotao.cloud.health.collect.cpu.system", desc = "系统cpu负载")
		private double systemCpuLoad;
		@FieldReport(name = "taotao.cloud.health.collect.cpu.committed.virtual.memory.size", desc = "已提交的虚拟内存大小")
		private long committedVirtualMemorySize;
		@FieldReport(name = "taotao.cloud.health.collect.cpu.total.swap.space.size", desc = "总交换空间大小")
		private long totalSwapSpaceSize;
		@FieldReport(name = "taotao.cloud.health.collect.cpu.free.swap.space.size", desc = "空闲交换空间大小")
		private long freeSwapSpaceSize;
		@FieldReport(name = "taotao.cloud.health.collect.cpu.process.cpu.time", desc = "处理cpu时间")
		private long processCpuTime;
		@FieldReport(name = "taotao.cloud.health.collect.cpu.free.physical.memory.size", desc = "空闲的物理内存空间")
		private long freePhysicalMemorySize;
		@FieldReport(name = "taotao.cloud.health.collect.cpu.total.physical.memory.size", desc = "总的物理内存空间")
		private long totalPhysicalMemorySize;
		@FieldReport(name = "taotao.cloud.health.collect.cpu.core.num", desc = "系统cpu核心数")
		private Integer cpuCoreNumber;
	}

}
