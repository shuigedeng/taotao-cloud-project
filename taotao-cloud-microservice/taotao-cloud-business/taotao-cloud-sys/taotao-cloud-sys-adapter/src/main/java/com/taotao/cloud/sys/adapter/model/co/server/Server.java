package com.taotao.cloud.sys.adapter.model.co.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

/**
 * https://blog.csdn.net/weixin_43591980/article/details/116157814
 *
 * @since 2022/5/21 17:43
 */
public class Server {

	private static final int OSHI_WAIT_SECOND = 1000;

	/**
	 * CPU相关信息
	 */
	private Cpu cpu = new Cpu();

	/**
	 * 內存相关信息
	 */
	private Mem mem = new Mem();

	/**
	 * JVM相关信息
	 */
	private Jvm jvm = new Jvm();

	/**
	 * 服务器相关信息
	 */
	private Sys sys = new Sys();

	/**
	 * 磁盘相关信息
	 */
	private List<SysFile> sysFiles = new LinkedList<>();

	public Cpu getCpu() {

		return cpu;
	}

	public void setCpu(Cpu cpu) {

		this.cpu = cpu;
	}

	public Mem getMem() {

		return mem;
	}

	public void setMem(Mem mem) {

		this.mem = mem;
	}

	public Jvm getJvm() {

		return jvm;
	}

	public void setJvm(Jvm jvm) {

		this.jvm = jvm;
	}

	public Sys getSys() {

		return sys;
	}

	public void setSys(Sys sys) {

		this.sys = sys;
	}

	public List<SysFile> getSysFiles() {

		return sysFiles;
	}

	public void setSysFiles(List<SysFile> sysFiles) {

		this.sysFiles = sysFiles;
	}

	/**
	 * 获取服务器主机相关信息
	 */
	public void copyTo() throws Exception {

		// 获取系统信息

		SystemInfo si = new SystemInfo();

		// 根据SystemInfo获取硬件实例

		HardwareAbstractionLayer hal = si.getHardware();

		// 获取硬件CPU信息

		setCpuInfo(hal.getProcessor());

		// 获取硬件内存信息

		setMemInfo(hal.getMemory());

		// 设置服务器信息

		setSysInfo();

		// 设置Java虚拟机

		setJvmInfo();

		// 设置磁盘信息

		setSysFiles(si.getOperatingSystem());
	}

	/**
	 * 设置CPU信息
	 */
	private void setCpuInfo(CentralProcessor processor) {

		// CPU信息

		long[] prevTicks = processor.getSystemCpuLoadTicks();

		Util.sleep(OSHI_WAIT_SECOND);

		long[] ticks = processor.getSystemCpuLoadTicks();

		long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];

		long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];

		long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];

		long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];

		long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];

		long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];

		long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];

		long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];

		long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

		cpu.setCpuNum(processor.getLogicalProcessorCount()); // Cpu 核数

		cpu.setTotal(totalCpu); // CPU总的使用率

		cpu.setSys(cSys); // CPU系统使用率

		cpu.setUsed(user); // CPU用户使用率

		cpu.setWait(iowait); // CPU当前等待率

		cpu.setFree(idle); // CPU当前空闲率
	}

	/**
	 * 设置内存信息
	 */
	private void setMemInfo(GlobalMemory memory) {

		mem.setTotal(memory.getTotal()); // 总内存大小

		mem.setUsed(memory.getTotal() - memory.getAvailable()); // 已使用内存大小

		mem.setFree(memory.getAvailable()); // 空闲内存大小
	}

	/**
	 * 设置服务器信息
	 */
	private void setSysInfo() throws UnknownHostException {

		// 获取当前的系统属性

		Properties props = System.getProperties();

		sys.setComputerName(InetAddress.getLocalHost().getHostName()); // 获取主机名称

		sys.setComputerIp(InetAddress.getLocalHost().getHostAddress()); // 获取主机IP

		sys.setOsName(props.getProperty("os.name")); // 获取主机类型 Windows 10

		sys.setOsArch(props.getProperty("os.arch")); // 获取主机显卡类型 amd64

		sys.setUserDir(props.getProperty("user.dir")); // 获取项目所在路径 F:\git\ruoyi\RuoYi-Vue
	}

	/**
	 * 设置Java虚拟机
	 */
	private void setJvmInfo() {

		Properties props = System.getProperties();

		jvm.setTotal(Runtime.getRuntime().totalMemory()); // JVM总内存 625.5M

		jvm.setMax(Runtime.getRuntime().maxMemory()); // JVM已使用内存 347.99M

		jvm.setFree(Runtime.getRuntime().freeMemory()); // JVM空闲内存 277.51M

		jvm.setVersion(props.getProperty("java.version")); // jdk版本 1.8

		jvm.setHome(props.getProperty("java.home")); // JDK安装路径 C:\Program
		// Files\Java\jdk1.8.0_201\jre
	}

	/**
	 * 设置磁盘信息
	 */
	private void setSysFiles(OperatingSystem os) {

		// 根据 操作系统(OS) 获取 FileSystem

		FileSystem fileSystem = os.getFileSystem();

		// 根据 FileSystem 获取主机磁盘信息list集合

		List<OSFileStore> fsArray = fileSystem.getFileStores();

		for (OSFileStore fs : fsArray) {

			long free = fs.getUsableSpace(); // 磁盘空闲容量

			long total = fs.getTotalSpace(); // 磁盘总容量

			long used = total - free; // 磁盘已使用容量

			SysFile sysFile = new SysFile();

			sysFile.setDirName(fs.getMount()); // 磁盘符号 C:\

			sysFile.setSysTypeName(fs.getType()); // 磁盘类型 NTFS

			sysFile.setTypeName(fs.getName()); // 磁盘名称 本地固定磁盘 (C:)

			sysFile.setTotal(convertFileSize(total)); // 磁盘总容量

			sysFile.setFree(convertFileSize(free)); // 磁盘空闲容量

			sysFile.setUsed(convertFileSize(used)); // 磁盘已使用容量
			double usage = ((double) used / (double) total) * 100;
			sysFile.setUsage(usage); // 磁盘资源的使用率

			sysFiles.add(sysFile);
		}
	}

	/**
	 * 字节转换
	 *
	 * @param size 字节大小
	 * @return 转换后值
	 */
	public String convertFileSize(long size) {

		long kb = 1024;

		long mb = kb * 1024;

		long gb = mb * 1024;

		if (size >= gb) {

			return String.format("%.1f GB", (float) size / gb);

		} else if (size >= mb) {

			float f = (float) size / mb;

			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);

		} else if (size >= kb) {

			float f = (float) size / kb;

			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);

		} else {

			return String.format("%d B", size);
		}
	}

}
