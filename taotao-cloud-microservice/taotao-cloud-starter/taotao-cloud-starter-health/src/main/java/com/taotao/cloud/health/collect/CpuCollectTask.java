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
import com.taotao.cloud.health.model.CollectInfo;
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

	private static final String TASK_NAME = "taotao.cloud.health.collect.cpu";

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
		return this.getClass().getName();
	}

	@Override
	public String getName() {
		return TASK_NAME;
	}

	@Override
	public boolean getEnabled() {
		return properties.isCpuEnabled();
	}

	@Override
	protected CollectInfo getData() {
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

	private static class CpuInfo implements CollectInfo{

		@FieldReport(name = TASK_NAME + ".process", desc = "进程cpu负载")
		private Double processCpuLoad;
		@FieldReport(name = TASK_NAME + ".system", desc = "系统cpu负载")
		private Double systemCpuLoad;
		@FieldReport(name = TASK_NAME + ".committed.virtual.memory.size", desc = "已提交的虚拟内存大小")
		private Long committedVirtualMemorySize;
		@FieldReport(name = TASK_NAME + ".total.swap.space.size", desc = "总交换空间大小")
		private Long totalSwapSpaceSize;
		@FieldReport(name = TASK_NAME + ".free.swap.space.size", desc = "空闲交换空间大小")
		private Long freeSwapSpaceSize;
		@FieldReport(name = TASK_NAME + ".process.cpu.time", desc = "处理cpu时间")
		private Long processCpuTime;
		@FieldReport(name = TASK_NAME + ".free.physical.memory.size", desc = "空闲的物理内存空间")
		private Long freePhysicalMemorySize;
		@FieldReport(name = TASK_NAME + ".total.physical.memory.size", desc = "总的物理内存空间")
		private Long totalPhysicalMemorySize;
		@FieldReport(name = TASK_NAME + ".core.num", desc = "系统cpu核心数")
		private Integer cpuCoreNumber;
	}

}
