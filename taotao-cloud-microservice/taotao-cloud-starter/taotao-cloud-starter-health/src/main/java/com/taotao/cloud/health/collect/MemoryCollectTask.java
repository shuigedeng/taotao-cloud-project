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
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

/**
 * MemoryCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:09:07
 */
public class MemoryCollectTask extends AbstractCollectTask {

	private OperatingSystemMXBean systemMXBean;
	private CollectTaskProperties properties;

	public MemoryCollectTask(CollectTaskProperties properties) {
		this.systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getMemeryTimeSpan();
	}

	@Override
	public String getDesc() {
		return "MemoryCollectTask";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.memery";
	}

	@Override
	public boolean getEnabled() {
		return properties.isMemeryEnabled();
	}

	@Override
	protected Object getData() {
		try {
			JvmInfo jvmInfo = new JvmInfo();

			Runtime rt = Runtime.getRuntime();
			jvmInfo.totalInfo = new JvmTotalInfo();
			JvmTotalInfo totalInfo = jvmInfo.totalInfo;

			totalInfo.total = rt.totalMemory() / byteToMb;
			totalInfo.free = rt.freeMemory() / byteToMb;
			totalInfo.max = rt.maxMemory() / byteToMb;
			totalInfo.use = totalInfo.total - totalInfo.free;

			List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
			if (pools != null && !pools.isEmpty()) {
				for (MemoryPoolMXBean pool : pools) {
					String name = pool.getName();
					Object jvmGen = null;
					if (name.contains("Eden")) {
						jvmInfo.edenInfo = new JVMEdenInfo();
						jvmGen = jvmInfo.edenInfo;
					} else if (name.contains("Code Cache")) {
						jvmInfo.genCodeCache = new JVMCodeCacheInfo();
						jvmGen = jvmInfo.genCodeCache;
					} else if (name.contains("Old")) {
						jvmInfo.genOldInfo = new JVMOldInfo();
						jvmGen = jvmInfo.genOldInfo;
					} else if (name.contains("Perm")) {
						jvmInfo.genPermInfo = new JVMPermInfo();
						jvmGen = jvmInfo.genPermInfo;
					} else if (name.contains("Survivor")) {
						jvmInfo.survivorInfo = new JVMSurvivorInfo();
						jvmGen = jvmInfo.survivorInfo;
					} else if (name.contains("Metaspace")) {
						jvmInfo.genMetaspace = new JVMMetaspaceInfo();
						jvmGen = jvmInfo.genMetaspace;
					} else if (name.contains("Compressed Class Space")) {
						jvmInfo.genCompressedClassSpace = new JVMCompressedClassSpaceInfo();
						jvmGen = jvmInfo.genCompressedClassSpace;
					} else {
						// int a=1;
					}

					if (jvmGen != null && pool.getUsage() != null) {
						ReflectionUtil.setFieldValue(
							ReflectionUtil.findField(jvmGen.getClass(), "init"), jvmGen,
							pool.getUsage().getInit() / byteToMb);
						ReflectionUtil.setFieldValue(
							ReflectionUtil.findField(jvmGen.getClass(), "used"), jvmGen,
							pool.getUsage().getUsed() / byteToMb);
						ReflectionUtil.setFieldValue(
							ReflectionUtil.findField(jvmGen.getClass(), "max"), jvmGen,
							pool.getUsage().getMax() / byteToMb);
						long poolUsageCommitted = pool.getUsage().getCommitted();
						ReflectionUtil.setFieldValue(
							ReflectionUtil.findField(jvmGen.getClass(), "committed"), jvmGen,
							poolUsageCommitted / byteToMb);
						if (poolUsageCommitted > 0) {
							ReflectionUtil.setFieldValue(
								ReflectionUtil.findField(jvmGen.getClass(), "usedRate"), jvmGen,
								(pool.getUsage().getUsed() * 100 / poolUsageCommitted));
						}
					}
				}
			}

			SystemInfo systemInfo = new SystemInfo();
			systemInfo.free = systemMXBean.getFreePhysicalMemorySize() / byteToMb;
			systemInfo.total = systemMXBean.getTotalPhysicalMemorySize() / byteToMb;
			systemInfo.use = systemInfo.total - systemInfo.free;

			return new MemeryInfo(jvmInfo, systemInfo);
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}


	private static class JvmInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.total.info", desc = "JVM 内存统计")
		private JvmTotalInfo totalInfo;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.eden.info", desc = "JVM Eden 年轻代内存(M)")
		private JVMEdenInfo edenInfo;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.survivor.info", desc = "JVM Survivor 年轻代内存(M)")
		private JVMSurvivorInfo survivorInfo;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.old.info", desc = "JVM Old 老年代内存(M)")
		private JVMOldInfo genOldInfo;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.perm.info", desc = "JVM Perm 永久代内存(M)")
		private JVMPermInfo genPermInfo;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.codeCache.info", desc = "JVM CodeCache 编译码缓存内存(M)")
		private JVMCodeCacheInfo genCodeCache;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.metaspace.info", desc = "JVM metaspace 元数据缓存内存(M)")
		private JVMMetaspaceInfo genMetaspace;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.compressedClassSpace.info", desc = "JVM CompressedClassSpace 缓存内存(M)")
		private JVMCompressedClassSpaceInfo genCompressedClassSpace;
	}

	private static class JvmTotalInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.use", desc = "JVM内存已用空间(M)")
		private double use;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.free", desc = "JVM内存可用空间(M)")
		private double free;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.max", desc = "JVM内存最大可用空间(M)")
		private double max;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.total", desc = "JVM内存总空间(M)")
		private double total;
	}

	private static class SystemInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.os.use", desc = "Os内存已用空间(M)")
		private double use;
		@FieldReport(name = "taotao.cloud.health.collect.memery.os.free", desc = "Os内存可用空间(M)")
		private double free;
		@FieldReport(name = "taotao.cloud.health.collect.memery.os.total", desc = "Os内存总空间(M)")
		private double total;
	}

	private static class MemeryInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm", desc = "JVM内存空间(M)")
		private JvmInfo jvmInfo = new JvmInfo();
		@FieldReport(name = "taotao.cloud.health.collect.memery.system", desc = "Os内存空间(M)")
		private SystemInfo systemInfo = new SystemInfo();

		public MemeryInfo(JvmInfo jvmInfo,
			SystemInfo systemInfo) {
			this.jvmInfo = jvmInfo;
			this.systemInfo = systemInfo;
		}
	}

	private static class JVMPermInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.perm.init", desc = "perm 初始内存大小(M)")
		private double init;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.perm.max", desc = "perm 最大内存大小(M)")
		private double max;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.perm.used", desc = "perm 已使用内存大小(M)")
		private double used;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.perm.committed", desc = "perm 已申请内存大小(M)")
		private double committed;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.perm.usedRate", desc = "perm 使用率 %")
		private double usedRate;

	}

	private static class JVMOldInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.old.init", desc = "old 初始内存大小(M)")
		private double init;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.old.max", desc = "old 最大内存大小(M)")
		private double max;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.old.used", desc = "old 已使用内存大小(M)")
		private double used;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.old.committed", desc = "old 已申请内存大小(M)")
		private double committed;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.old.usedRate", desc = "old 使用率 %")
		private double usedRate;

	}

	private static class JVMSurvivorInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.survivor.init", desc = "survivor 初始内存大小(M)")
		private double init;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.survivor.max", desc = "survivor 最大内存大小(M)")
		private double max;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.survivor.used", desc = "survivor 已使用内存大小(M)")
		private double used;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.survivor.committed", desc = "survivor 已申请内存大小(M)")
		private double committed;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.survivor.usedRate", desc = "survivor 使用率 %")
		private double usedRate;
	}

	private static class JVMEdenInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.eden.init", desc = "eden 初始内存大小(M)")
		private double init;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.eden.max", desc = "eden 最大内存大小(M)")
		private double max;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.eden.used", desc = "eden 已使用内存大小(M)")
		private double used;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.eden.committed", desc = "eden 已申请内存大小(M)")
		private double committed;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.eden.usedRate", desc = "eden 使用率 %")
		private double usedRate;

	}

	private static class JVMCodeCacheInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.codeCache.init", desc = "codeCache 初始内存大小(M)")
		private double init;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.codeCache.max", desc = "codeCache 最大内存大小(M)")
		private double max;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.codeCache.used", desc = "codeCache 已使用内存大小(M)")
		private double used;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.codeCache.committed", desc = "codeCache 已申请内存大小(M)")
		private double committed;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.codeCache.usedRate", desc = "codeCache 使用率 %")
		private double usedRate;
	}

	private static class JVMMetaspaceInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.metaspace.init", desc = "metaspace 初始内存大小(M)")
		private double init;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.metaspace.max", desc = "metaspace 最大内存大小(M)")
		private double max;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.metaspace.used", desc = "metaspace 已使用内存大小(M)")
		private double used;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.metaspace.committed", desc = "metaspace 已申请内存大小(M)")
		private double committed;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.metaspace.usedRate", desc = "metaspace 使用率 %")
		private double usedRate;
	}

	private static class JVMCompressedClassSpaceInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.compressedClassSpace.init", desc = "Compressed Class Space 初始内存大小(M)")
		private double init;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.compressedClassSpace.max", desc = "Compressed Class Space 最大内存大小(M)")
		private double max;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.compressedClassSpace.used", desc = "Compressed Class Space 已使用内存大小(M)")
		private double used;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.compressedClassSpace.committed", desc = "Compressed Class Space 已申请内存大小(M)")
		private double committed;
		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm.gen.compressedClassSpace.usedRate", desc = "Compressed Class Space 使用率 %")
		private double usedRate;
	}
}
