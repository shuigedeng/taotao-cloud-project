/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.monitor.collect.task;

import static com.taotao.cloud.monitor.utils.ProcessUtils.getProcessID;

import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.monitor.annotation.FieldReport;
import com.taotao.cloud.monitor.collect.AbstractCollectTask;
import com.taotao.cloud.monitor.collect.CollectInfo;
import com.taotao.cloud.monitor.enums.WarnTypeEnum;
import com.taotao.cloud.monitor.properties.CollectTaskProperties;
import com.taotao.cloud.monitor.utils.ProcessUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * IO性能参数收集
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:47:39
 */

public class IOCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.monitor.collect.io";

	private final CollectTaskProperties properties;

	public IOCollectTask(CollectTaskProperties properties) {
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getIoTimeSpan();
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
		return properties.isIoEnabled();
	}

	@Override
	protected CollectInfo getData() {
		try {
			IoInfo ioInfo = new IoInfo();
			File file = new File(".");

			ioInfo.currentDirUsableSize = file.getUsableSpace() / byteToMb;
			ioInfo.currentDirTotalSize = file.getTotalSpace() / byteToMb;
			ioInfo.currentDir = file.getAbsolutePath();

			long processReadSize = BeanUtil.convert(ProcessUtils.execCmd(
				"cat /proc/$PID/io |egrep -E 'read_bytes'|awk '{print $2}'"
					.replaceAll("\\$PID", getProcessID())), Long.class);
			ioInfo.processReadSize =
				processReadSize > 0 ? processReadSize / byteToMb : processReadSize;

			long processWriteSize = BeanUtil.convert(ProcessUtils.execCmd(
				"cat /proc/$PID/io |egrep -E '^write_bytes'|awk '{print $2}'"
					.replaceAll("\\$PID", getProcessID())), Long.class);
			ioInfo.processWriteSize =
				processWriteSize > 0 ? processWriteSize / byteToMb : processWriteSize;

			ioInfo.processWa = BeanUtil.convert(
				ProcessUtils.execCmd("top -bn1 | sed -n '3p'|cut -d, -f5 |awk '{print $1}'"),
				Double.class);

			return ioInfo;
		} catch (Exception e) {
			if(LogUtil.isErrorEnabled()){
				LogUtil.error(e);
			}
		}
		return null;
	}

	public static boolean getIsAutoClear() {
		return true;
	}

	public static void clearLog() {
		if (getIsAutoClear()) {
			StringBuilder info = new StringBuilder();
			String[] logs = {"catlogs", "out.log", "app.log", "pinpoint-agent/log", "logs", "log"};
			for (String log : logs) {
				String result = clearFile(log);
				if (!result.isEmpty()) {
					info.append(log).append("[").append(result).append("];");
				}
			}

			AbstractCollectTask.notifyMessage(WarnTypeEnum.INFO, "自动清理日志成功", info.toString());
		}
	}

	private static String clearFile(String filepath) {
		File f = new File(filepath);
		if (!f.exists()) {
			return "";
		}

		try {
			if (f.isFile()) {
				try {
					try (FileOutputStream out = new FileOutputStream(f)) {
						out.write(new byte[1]);
					}
				} catch (Exception e) {
					LogUtil.error(e);
				}
			} else if (f.isDirectory()) {
				File[] files = f.listFiles();
				for (File file : files) {
					clearFile(file.getPath());
				}
			}

			f.delete();
			return "清理完毕";
		} catch (Exception e) {
			LogUtil.error(e);
			return "清理出错";
		}
	}

	private static class IoInfo implements CollectInfo{

		@FieldReport(name = TASK_NAME + ".current.dir.usable.size", desc = "当前目录可用大小(M)")
		private Long currentDirUsableSize = 0L;
		@FieldReport(name = TASK_NAME + ".current.dir.total.size", desc = "当前目录总大小(M)")
		private Long currentDirTotalSize = 0L;
		@FieldReport(name = TASK_NAME + ".current.dir.path", desc = "当前目录路径")
		private String currentDir ="";
		@FieldReport(name = TASK_NAME + ".process.read.size", desc = "当前进程的读io(B)")
		private Long processReadSize = 0L;
		@FieldReport(name = TASK_NAME + ".process.write.size", desc = "当前进程的写io(B)")
		private Long processWriteSize = 0L;
		@FieldReport(name = TASK_NAME + ".process.wa", desc = "磁盘wa百分比")
		private Double processWa = 0.0;
	}
}
