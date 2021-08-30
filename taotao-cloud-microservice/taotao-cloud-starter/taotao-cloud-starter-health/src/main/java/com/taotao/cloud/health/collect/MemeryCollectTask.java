package com.taotao.cloud.health.collect;


import com.sun.management.OperatingSystemMXBean;
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.health.model.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 19:07
 **/
public class MemeryCollectTask extends AbstractCollectTask {

	OperatingSystemMXBean systemMXBean;
	CollectTaskProperties properties;

	public MemeryCollectTask(CollectTaskProperties properties) {
		this.systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getMemeryTimeSpan();
	}

	@Override
	public String getDesc() {
		return "内存监测";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.memery.info";
	}

	@Override
	public boolean getEnabled() {
		return properties.isMemeryEnabled();
	}

	@Override
	protected Object getData() {
		JvmInfo jvmInfo = new JvmInfo();

		Runtime rt = Runtime.getRuntime();
		jvmInfo.setTotalInfo(new JvmTotalInfo());
		JvmTotalInfo totalInfo = jvmInfo.getTotalInfo();

		totalInfo.setTotal(rt.totalMemory() / byteToMb);
		totalInfo.setFree(rt.freeMemory() / byteToMb);
		totalInfo.setMax(rt.maxMemory() / byteToMb);
		totalInfo.setUse(totalInfo.getTotal() - totalInfo.getFree());

		List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
		if (pools != null && !pools.isEmpty()) {
			for (MemoryPoolMXBean pool : pools) {
				String name = pool.getName();
				Object jvmGen = null;
				if (name.contains("Eden")) {
					jvmInfo.setEdenInfo(new JVMEdenInfo());
					jvmGen = jvmInfo.getEdenInfo();
				} else if (name.contains("Code Cache")) {
					jvmInfo.setGenCodeCache(new JVMCodeCacheInfo());
					jvmGen = jvmInfo.getGenCodeCache();
				} else if (name.contains("Old")) {
					jvmInfo.setGenOldInfo(new JVMOldInfo());
					jvmGen = jvmInfo.getGenOldInfo();
				} else if (name.contains("Perm")) {
					jvmInfo.setGenPermInfo(new JVMPermInfo());
					jvmGen = jvmInfo.getGenPermInfo();
				} else if (name.contains("Survivor")) {
					jvmInfo.setSurvivorInfo(new JVMSurvivorInfo());
					jvmGen = jvmInfo.getSurvivorInfo();
				} else if (name.contains("Metaspace")) {
					jvmInfo.setGenMetaspace(new JVMMetaspaceInfo());
					jvmGen = jvmInfo.getGenMetaspace();
				} else if (name.contains("Compressed Class Space")) {
					jvmInfo.setGenCompressedClassSpace(new JVMCompressedClassSpaceInfo());
					jvmGen = jvmInfo.getGenCompressedClassSpace();
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
		systemInfo.setFree(systemMXBean.getFreePhysicalMemorySize() / byteToMb);
		systemInfo.setTotal(systemMXBean.getTotalPhysicalMemorySize() / byteToMb);
		systemInfo.setUse(systemInfo.getTotal() - systemInfo.getFree());

		return new MemeryInfo(jvmInfo, systemInfo);
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

		public JvmInfo() {
		}

		public JvmInfo(JvmTotalInfo totalInfo,
			JVMEdenInfo edenInfo,
			JVMSurvivorInfo survivorInfo,
			JVMOldInfo genOldInfo,
			JVMPermInfo genPermInfo,
			JVMCodeCacheInfo genCodeCache,
			JVMMetaspaceInfo genMetaspace,
			JVMCompressedClassSpaceInfo genCompressedClassSpace) {
			this.totalInfo = totalInfo;
			this.edenInfo = edenInfo;
			this.survivorInfo = survivorInfo;
			this.genOldInfo = genOldInfo;
			this.genPermInfo = genPermInfo;
			this.genCodeCache = genCodeCache;
			this.genMetaspace = genMetaspace;
			this.genCompressedClassSpace = genCompressedClassSpace;
		}

		public JvmTotalInfo getTotalInfo() {
			return totalInfo;
		}

		public void setTotalInfo(JvmTotalInfo totalInfo) {
			this.totalInfo = totalInfo;
		}

		public JVMEdenInfo getEdenInfo() {
			return edenInfo;
		}

		public void setEdenInfo(JVMEdenInfo edenInfo) {
			this.edenInfo = edenInfo;
		}

		public JVMSurvivorInfo getSurvivorInfo() {
			return survivorInfo;
		}

		public void setSurvivorInfo(
			JVMSurvivorInfo survivorInfo) {
			this.survivorInfo = survivorInfo;
		}

		public JVMOldInfo getGenOldInfo() {
			return genOldInfo;
		}

		public void setGenOldInfo(JVMOldInfo genOldInfo) {
			this.genOldInfo = genOldInfo;
		}

		public JVMPermInfo getGenPermInfo() {
			return genPermInfo;
		}

		public void setGenPermInfo(JVMPermInfo genPermInfo) {
			this.genPermInfo = genPermInfo;
		}

		public JVMCodeCacheInfo getGenCodeCache() {
			return genCodeCache;
		}

		public void setGenCodeCache(
			JVMCodeCacheInfo genCodeCache) {
			this.genCodeCache = genCodeCache;
		}

		public JVMMetaspaceInfo getGenMetaspace() {
			return genMetaspace;
		}

		public void setGenMetaspace(
			JVMMetaspaceInfo genMetaspace) {
			this.genMetaspace = genMetaspace;
		}

		public JVMCompressedClassSpaceInfo getGenCompressedClassSpace() {
			return genCompressedClassSpace;
		}

		public void setGenCompressedClassSpace(
			JVMCompressedClassSpaceInfo genCompressedClassSpace) {
			this.genCompressedClassSpace = genCompressedClassSpace;
		}
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

		public JvmTotalInfo() {
		}

		public JvmTotalInfo(double use, double free, double max, double total) {
			this.use = use;
			this.free = free;
			this.max = max;
			this.total = total;
		}

		public double getUse() {
			return use;
		}

		public void setUse(double use) {
			this.use = use;
		}

		public double getFree() {
			return free;
		}

		public void setFree(double free) {
			this.free = free;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max;
		}

		public double getTotal() {
			return total;
		}

		public void setTotal(double total) {
			this.total = total;
		}
	}

	private static class SystemInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.os.use", desc = "Os内存已用空间(M)")
		private double use;
		@FieldReport(name = "taotao.cloud.health.collect.memery.os.free", desc = "Os内存可用空间(M)")
		private double free;
		@FieldReport(name = "taotao.cloud.health.collect.memery.os.total", desc = "Os内存总空间(M)")
		private double total;

		public SystemInfo() {
		}

		public SystemInfo(double use, double free, double total) {
			this.use = use;
			this.free = free;
			this.total = total;
		}

		public double getUse() {
			return use;
		}

		public void setUse(double use) {
			this.use = use;
		}

		public double getFree() {
			return free;
		}

		public void setFree(double free) {
			this.free = free;
		}

		public double getTotal() {
			return total;
		}

		public void setTotal(double total) {
			this.total = total;
		}
	}

	private static class MemeryInfo {

		@FieldReport(name = "taotao.cloud.health.collect.memery.jvm", desc = "JVM内存空间(M)")
		private JvmInfo jvmInfo = new JvmInfo();
		@FieldReport(name = "taotao.cloud.health.collect.memery.system", desc = "Os内存空间(M)")
		private SystemInfo systemInfo = new SystemInfo();

		public MemeryInfo() {
		}

		public MemeryInfo(JvmInfo jvmInfo,
			SystemInfo systemInfo) {
			this.jvmInfo = jvmInfo;
			this.systemInfo = systemInfo;
		}

		public JvmInfo getJvmInfo() {
			return jvmInfo;
		}

		public void setJvmInfo(JvmInfo jvmInfo) {
			this.jvmInfo = jvmInfo;
		}

		public SystemInfo getSystemInfo() {
			return systemInfo;
		}

		public void setSystemInfo(SystemInfo systemInfo) {
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

		public JVMPermInfo() {
		}

		public JVMPermInfo(double init, double max, double used, double committed,
			double usedRate) {
			this.init = init;
			this.max = max;
			this.used = used;
			this.committed = committed;
			this.usedRate = usedRate;
		}

		public double getInit() {
			return init;
		}

		public void setInit(double init) {
			this.init = init;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max;
		}

		public double getUsed() {
			return used;
		}

		public void setUsed(double used) {
			this.used = used;
		}

		public double getCommitted() {
			return committed;
		}

		public void setCommitted(double committed) {
			this.committed = committed;
		}

		public double getUsedRate() {
			return usedRate;
		}

		public void setUsedRate(double usedRate) {
			this.usedRate = usedRate;
		}
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

		public JVMOldInfo() {
		}

		public JVMOldInfo(double init, double max, double used, double committed, double usedRate) {
			this.init = init;
			this.max = max;
			this.used = used;
			this.committed = committed;
			this.usedRate = usedRate;
		}

		public double getInit() {
			return init;
		}

		public void setInit(double init) {
			this.init = init;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max;
		}

		public double getUsed() {
			return used;
		}

		public void setUsed(double used) {
			this.used = used;
		}

		public double getCommitted() {
			return committed;
		}

		public void setCommitted(double committed) {
			this.committed = committed;
		}

		public double getUsedRate() {
			return usedRate;
		}

		public void setUsedRate(double usedRate) {
			this.usedRate = usedRate;
		}
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

		public JVMSurvivorInfo() {
		}

		public JVMSurvivorInfo(double init, double max, double used, double committed,
			double usedRate) {
			this.init = init;
			this.max = max;
			this.used = used;
			this.committed = committed;
			this.usedRate = usedRate;
		}

		public double getInit() {
			return init;
		}

		public void setInit(double init) {
			this.init = init;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max;
		}

		public double getUsed() {
			return used;
		}

		public void setUsed(double used) {
			this.used = used;
		}

		public double getCommitted() {
			return committed;
		}

		public void setCommitted(double committed) {
			this.committed = committed;
		}

		public double getUsedRate() {
			return usedRate;
		}

		public void setUsedRate(double usedRate) {
			this.usedRate = usedRate;
		}
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


		public JVMEdenInfo() {
		}

		public JVMEdenInfo(double init, double max, double used, double committed,
			double usedRate) {
			this.init = init;
			this.max = max;
			this.used = used;
			this.committed = committed;
			this.usedRate = usedRate;
		}

		public double getInit() {
			return init;
		}

		public void setInit(double init) {
			this.init = init;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max;
		}

		public double getUsed() {
			return used;
		}

		public void setUsed(double used) {
			this.used = used;
		}

		public double getCommitted() {
			return committed;
		}

		public void setCommitted(double committed) {
			this.committed = committed;
		}

		public double getUsedRate() {
			return usedRate;
		}

		public void setUsedRate(double usedRate) {
			this.usedRate = usedRate;
		}
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

		public JVMCodeCacheInfo() {
		}

		public JVMCodeCacheInfo(double init, double max, double used, double committed,
			double usedRate) {
			this.init = init;
			this.max = max;
			this.used = used;
			this.committed = committed;
			this.usedRate = usedRate;
		}

		public double getInit() {
			return init;
		}

		public void setInit(double init) {
			this.init = init;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max;
		}

		public double getUsed() {
			return used;
		}

		public void setUsed(double used) {
			this.used = used;
		}

		public double getCommitted() {
			return committed;
		}

		public void setCommitted(double committed) {
			this.committed = committed;
		}

		public double getUsedRate() {
			return usedRate;
		}

		public void setUsedRate(double usedRate) {
			this.usedRate = usedRate;
		}
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

		public JVMMetaspaceInfo() {
		}

		public JVMMetaspaceInfo(double init, double max, double used, double committed,
			double usedRate) {
			this.init = init;
			this.max = max;
			this.used = used;
			this.committed = committed;
			this.usedRate = usedRate;
		}

		public double getInit() {
			return init;
		}

		public void setInit(double init) {
			this.init = init;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max;
		}

		public double getUsed() {
			return used;
		}

		public void setUsed(double used) {
			this.used = used;
		}

		public double getCommitted() {
			return committed;
		}

		public void setCommitted(double committed) {
			this.committed = committed;
		}

		public double getUsedRate() {
			return usedRate;
		}

		public void setUsedRate(double usedRate) {
			this.usedRate = usedRate;
		}
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

		public JVMCompressedClassSpaceInfo() {
		}

		public JVMCompressedClassSpaceInfo(double init, double max, double used, double committed,
			double usedRate) {
			this.init = init;
			this.max = max;
			this.used = used;
			this.committed = committed;
			this.usedRate = usedRate;
		}

		public double getInit() {
			return init;
		}

		public void setInit(double init) {
			this.init = init;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max;
		}

		public double getUsed() {
			return used;
		}

		public void setUsed(double used) {
			this.used = used;
		}

		public double getCommitted() {
			return committed;
		}

		public void setCommitted(double committed) {
			this.committed = committed;
		}

		public double getUsedRate() {
			return usedRate;
		}

		public void setUsedRate(double usedRate) {
			this.usedRate = usedRate;
		}
	}
}
