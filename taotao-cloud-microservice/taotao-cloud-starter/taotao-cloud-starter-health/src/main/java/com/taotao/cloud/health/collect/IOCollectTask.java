package com.taotao.cloud.health.collect;


import static com.taotao.cloud.health.utils.ProcessUtils.getProcessID;

import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.health.base.AbstractCollectTask;
import com.taotao.cloud.health.base.EnumWarnType;
import com.taotao.cloud.health.base.FieldReport;
import com.taotao.cloud.health.utils.ConvertUtils;
import com.taotao.cloud.health.utils.ProcessUtils;
import java.io.File;
import java.io.FileOutputStream;


/**
 * IO性能参数收集
 *
 * @author: chejiangyi
 * @version: 2019-07-24 18:19
 **/
public class IOCollectTask extends AbstractCollectTask {

	public static boolean getIsAutoClear() {
		return PropertyUtil.getPropertyCache("bsf.health.io.autoClear", true);
	}

	public IOCollectTask() {
	}

	@Override
	public int getTimeSpan() {
		return PropertyUtil.getPropertyCache("bsf.health.io.timeSpan", 10);
	}

	@Override
	public String getDesc() {
		return "io采集";
	}

	@Override
	public String getName() {
		return "io.info";
	}

	@Override
	public boolean getEnabled() {
		return PropertyUtil.getPropertyCache("bsf.health.io.enabled", true);
	}

	@Override
	protected Object getData() {
		IoInfo ioInfo = new IoInfo();
		File file = new File(".");
		ioInfo.currentDirUsableSize = file.getUsableSpace() / byteToMb;
		ioInfo.currentDirTotalSize = file.getTotalSpace() / byteToMb;
		ioInfo.currentDir = file.getAbsolutePath();
		long processReadSize = ConvertUtils.convert(ProcessUtils.execCmd(
			"cat /proc/$PID/io |egrep -E 'read_bytes'|awk '{print $2}'".replaceAll("\\$PID",
				getProcessID())), Long.class);
		ioInfo.processReadSize = processReadSize > 0 ? processReadSize / byteToMb : processReadSize;
		long processWriteSize = ConvertUtils.convert(ProcessUtils.execCmd(
			"cat /proc/$PID/io |egrep -E '^write_bytes'|awk '{print $2}'".replaceAll("\\$PID",
				getProcessID())), Long.class);
		ioInfo.processWriteSize =
			processWriteSize > 0 ? processWriteSize / byteToMb : processWriteSize;
		ioInfo.processWa = ConvertUtils.convert(
			ProcessUtils.execCmd("top -bn1 | sed -n '3p'|cut -d, -f5 |awk '{print $1}'"),
			Double.class);
		return ioInfo;
	}

	public static void clearlog() {
		if (getIsAutoClear()) {
			StringBuilder info = new StringBuilder();
			String[] logs = {"catlogs", "out.log", "app.log", "pinpoint-agent/log", "logs", "log"};
			for (String log : logs) {
				String result = clearfile(log);
				if (!result.isEmpty()) {
					info.append(log + "[" + result + "];");
				}
			}
			AbstractCollectTask.notifyMessage(EnumWarnType.INFO, "自动清理日志成功", info.toString());
		}
	}

	private static String clearfile(String filepath) {
		File f = new File(filepath);
		if (f == null || !f.exists()) {
			return "";
		}
		try {
			if (f.isFile()) {
				try {
					try (FileOutputStream out = new FileOutputStream(f)) {
						out.write(new byte[1]);
					}
				} catch (Exception e) {
				}
			} else if (f.isDirectory()) {
				File[] files = f.listFiles();
				for (int i = 0; i < files.length; i++) {
					clearfile(files[i].getPath());
				}
			}
			f.delete();
			return "清理完毕";
		} catch (Exception e) {
			return "清理出错";
		}
	}

	private static class IoInfo {

		@FieldReport(name = "io.current.dir.usable.size", desc = "当前目录可用大小(M)")
		private double currentDirUsableSize;
		@FieldReport(name = "io.current.dir.total.size", desc = "当前目录总大小(M)")
		private double currentDirTotalSize;
		@FieldReport(name = "io.current.dir.path", desc = "当前目录路径")
		private String currentDir;
		@FieldReport(name = "io.process.read.size", desc = "当前进程的读io(B)")
		private long processReadSize;
		@FieldReport(name = "io.process.write.size", desc = "当前进程的写io(B)")
		private long processWriteSize;
		@FieldReport(name = "io.process.wa", desc = "磁盘wa百分比")
		private double processWa;

		public IoInfo() {
		}

		public IoInfo(double currentDirUsableSize, double currentDirTotalSize, String currentDir,
			long processReadSize, long processWriteSize, double processWa) {
			this.currentDirUsableSize = currentDirUsableSize;
			this.currentDirTotalSize = currentDirTotalSize;
			this.currentDir = currentDir;
			this.processReadSize = processReadSize;
			this.processWriteSize = processWriteSize;
			this.processWa = processWa;
		}

		public double getCurrentDirUsableSize() {
			return currentDirUsableSize;
		}

		public void setCurrentDirUsableSize(double currentDirUsableSize) {
			this.currentDirUsableSize = currentDirUsableSize;
		}

		public double getCurrentDirTotalSize() {
			return currentDirTotalSize;
		}

		public void setCurrentDirTotalSize(double currentDirTotalSize) {
			this.currentDirTotalSize = currentDirTotalSize;
		}

		public String getCurrentDir() {
			return currentDir;
		}

		public void setCurrentDir(String currentDir) {
			this.currentDir = currentDir;
		}

		public long getProcessReadSize() {
			return processReadSize;
		}

		public void setProcessReadSize(long processReadSize) {
			this.processReadSize = processReadSize;
		}

		public long getProcessWriteSize() {
			return processWriteSize;
		}

		public void setProcessWriteSize(long processWriteSize) {
			this.processWriteSize = processWriteSize;
		}

		public double getProcessWa() {
			return processWa;
		}

		public void setProcessWa(double processWa) {
			this.processWa = processWa;
		}
	}


}
