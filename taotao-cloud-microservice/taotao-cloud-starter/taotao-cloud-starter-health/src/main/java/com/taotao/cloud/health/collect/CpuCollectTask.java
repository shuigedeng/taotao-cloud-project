package com.taotao.cloud.health.collect;

import com.sun.management.OperatingSystemMXBean;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.health.base.AbstractCollectTask;
import com.taotao.cloud.health.base.FieldReport;
import java.lang.management.ManagementFactory;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 19:07
 **/
public class CpuCollectTask extends AbstractCollectTask {

	OperatingSystemMXBean sysembean;

	public CpuCollectTask() {
		sysembean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	}

	@Override
	public int getTimeSpan() {
		return PropertyUtil.getPropertyCache("bsf.health.cpu.timeSpan", 10);
	}

	@Override
	public String getDesc() {
		return "cpu采集";
	}

	@Override
	public String getName() {
		return "cpu.info";
	}

	@Override
	public boolean getEnabled() {
		return PropertyUtil.getPropertyCache("bsf.health.cpu.enabled", true);
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

		@FieldReport(name = "cpu.process", desc = "进程cpu负载")
		private double processCpuLoad;
		@FieldReport(name = "cpu.system", desc = "系统cpu负载")
		private double systemCpuLoad;
		@FieldReport(name = "cpu.core.num", desc = "系统cpu核心数")
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
