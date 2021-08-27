package com.taotao.cloud.health.collect;


import static com.taotao.cloud.health.utils.ProcessUtils.getProcessID;

import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.health.base.AbstractCollectTask;
import com.taotao.cloud.health.base.FieldReport;
import com.taotao.cloud.health.utils.ConvertUtils;
import com.taotao.cloud.health.utils.ProcessUtils;

public class NetworkCollectTask extends AbstractCollectTask {

	public NetworkCollectTask() {

	}

	@Override
	public int getTimeSpan() {
		return PropertyUtil.getPropertyCache("bsf.health.network.timeSpan", 10);
	}

	@Override
	public String getDesc() {
		return "network采集";
	}

	@Override
	public String getName() {
		return "network.info";
	}

	@Override
	public boolean getEnabled() {
		return PropertyUtil.getPropertyCache("bsf.health.network.enabled", true);
	}


	@Override
	protected Object getData() {
		NetworkInfo ioInfo = new NetworkInfo();
		ioInfo.processSysTcpListenNum = ConvertUtils.convert(ProcessUtils.execCmd(
				"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1 |egrep -w 'LISTEN' |wc -l"),
			Long.class);
		ioInfo.processSysTcpEstablishedNum = ConvertUtils.convert(ProcessUtils.execCmd(
				"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1 |egrep -w 'ESTABLISHED' |wc -l"),
			Long.class);
		ioInfo.processSysTcpTimeWaitNum = ConvertUtils.convert(ProcessUtils.execCmd(
				"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1 |egrep -w 'TIME_WAIT' |wc -l"),
			Long.class);
		ioInfo.processTcpListenNum = ConvertUtils.convert(ProcessUtils.execCmd(
			"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1  |egrep -w '$PID' |egrep -w 'LISTEN' |wc -l".replaceAll(
				"\\$PID", getProcessID())), Long.class);
		ioInfo.processTcpEstablishedNum = ConvertUtils.convert(ProcessUtils.execCmd(
			"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1  |egrep -w '$PID' |egrep -w 'ESTABLISHED' |wc -l".replaceAll(
				"\\$PID", getProcessID())), Long.class);
		ioInfo.processTcpTimeWaitNum = ConvertUtils.convert(ProcessUtils.execCmd(
			"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1  |egrep -w '$PID' |egrep -w 'TIME_WAIT' |wc -l".replaceAll(
				"\\$PID", getProcessID())), Long.class);
		return ioInfo;
	}


	private static class NetworkInfo {

		@FieldReport(name = "network.process.tcp.listen.number", desc = "当前进程TCP LISTEN状态连接数")
		private long processTcpListenNum;
		@FieldReport(name = "network.process.tcp.established.number", desc = "当前进程TCP ESTABLISHED状态连接数")
		private long processTcpEstablishedNum;
		@FieldReport(name = "network.process.tcp.time_wait.number", desc = "当前进程TCP TIME_WAIT连接数")
		private long processTcpTimeWaitNum;
		@FieldReport(name = "network.sys.tcp.listen.number", desc = "系统TCP LISTEN状态连接数")
		private long processSysTcpListenNum;
		@FieldReport(name = "network.sys.tcp.established.number", desc = "系统TCP ESTABLISHED状态连接数")
		private long processSysTcpEstablishedNum;
		@FieldReport(name = "network.sys.tcp.time_wait.number", desc = "系统TCP TIME_WAIT连接数")
		private long processSysTcpTimeWaitNum;

		public NetworkInfo() {
		}

		public NetworkInfo(long processTcpListenNum, long processTcpEstablishedNum,
			long processTcpTimeWaitNum, long processSysTcpListenNum,
			long processSysTcpEstablishedNum,
			long processSysTcpTimeWaitNum) {
			this.processTcpListenNum = processTcpListenNum;
			this.processTcpEstablishedNum = processTcpEstablishedNum;
			this.processTcpTimeWaitNum = processTcpTimeWaitNum;
			this.processSysTcpListenNum = processSysTcpListenNum;
			this.processSysTcpEstablishedNum = processSysTcpEstablishedNum;
			this.processSysTcpTimeWaitNum = processSysTcpTimeWaitNum;
		}

		public long getProcessTcpListenNum() {
			return processTcpListenNum;
		}

		public void setProcessTcpListenNum(long processTcpListenNum) {
			this.processTcpListenNum = processTcpListenNum;
		}

		public long getProcessTcpEstablishedNum() {
			return processTcpEstablishedNum;
		}

		public void setProcessTcpEstablishedNum(long processTcpEstablishedNum) {
			this.processTcpEstablishedNum = processTcpEstablishedNum;
		}

		public long getProcessTcpTimeWaitNum() {
			return processTcpTimeWaitNum;
		}

		public void setProcessTcpTimeWaitNum(long processTcpTimeWaitNum) {
			this.processTcpTimeWaitNum = processTcpTimeWaitNum;
		}

		public long getProcessSysTcpListenNum() {
			return processSysTcpListenNum;
		}

		public void setProcessSysTcpListenNum(long processSysTcpListenNum) {
			this.processSysTcpListenNum = processSysTcpListenNum;
		}

		public long getProcessSysTcpEstablishedNum() {
			return processSysTcpEstablishedNum;
		}

		public void setProcessSysTcpEstablishedNum(long processSysTcpEstablishedNum) {
			this.processSysTcpEstablishedNum = processSysTcpEstablishedNum;
		}

		public long getProcessSysTcpTimeWaitNum() {
			return processSysTcpTimeWaitNum;
		}

		public void setProcessSysTcpTimeWaitNum(long processSysTcpTimeWaitNum) {
			this.processSysTcpTimeWaitNum = processSysTcpTimeWaitNum;
		}
	}


}
