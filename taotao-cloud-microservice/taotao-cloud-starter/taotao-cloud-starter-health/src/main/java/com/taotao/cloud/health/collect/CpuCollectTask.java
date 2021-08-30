package com.taotao.cloud.health.collect;

import com.sun.management.OperatingSystemMXBean;
import com.taotao.cloud.health.model.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.lang.management.ManagementFactory;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 19:07
 **/
public class CpuCollectTask extends AbstractCollectTask {

	OperatingSystemMXBean sysembean;
	CollectTaskProperties properties;

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
		return "cpu采集";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.cpu.info";
	}

	@Override
	public boolean getEnabled() {
		return properties.isCpuEnabled();
	}

	@Override
	protected Object getData() {
		CpuInfo info = new CpuInfo();
		info.setProcessCpuLoad(sysembean.getProcessCpuLoad());
		info.setSystemCpuLoad(sysembean.getSystemCpuLoad());
		info.setCpuCoreNumber(Runtime.getRuntime().availableProcessors());
		return info;
	}


	private static class CpuInfo {

		@FieldReport(name = "taotao.cloud.health.collect.cpu.process", desc = "进程cpu负载")
		private double processCpuLoad;
		@FieldReport(name = "taotao.cloud.health.collect.cpu.system", desc = "系统cpu负载")
		private double systemCpuLoad;
		@FieldReport(name = "taotao.cloud.health.collect.cpu.core.num", desc = "系统cpu核心数")
		private Integer cpuCoreNumber;

		public CpuInfo() {
		}

		public CpuInfo(double processCpuLoad, double systemCpuLoad, Integer cpuCoreNumber) {
			this.processCpuLoad = processCpuLoad;
			this.systemCpuLoad = systemCpuLoad;
			this.cpuCoreNumber = cpuCoreNumber;
		}

		public double getProcessCpuLoad() {
			return processCpuLoad;
		}

		public void setProcessCpuLoad(double processCpuLoad) {
			this.processCpuLoad = processCpuLoad;
		}

		public double getSystemCpuLoad() {
			return systemCpuLoad;
		}

		public void setSystemCpuLoad(double systemCpuLoad) {
			this.systemCpuLoad = systemCpuLoad;
		}

		public Integer getCpuCoreNumber() {
			return cpuCoreNumber;
		}

		public void setCpuCoreNumber(Integer cpuCoreNumber) {
			this.cpuCoreNumber = cpuCoreNumber;
		}
	}

}
