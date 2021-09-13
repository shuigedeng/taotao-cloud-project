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


import static com.taotao.cloud.health.utils.ProcessUtils.getProcessID;

import com.taotao.cloud.common.utils.BeanUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import com.taotao.cloud.health.utils.ProcessUtils;

/**
 * NetworkCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:15:42
 */
public class NetworkCollectTask extends AbstractCollectTask {

	private CollectTaskProperties properties;

	public NetworkCollectTask(CollectTaskProperties properties) {
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getNetworkTimeSpan();
	}

	@Override
	public String getDesc() {
		return "network采集";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.network.info";
	}

	@Override
	public boolean getEnabled() {
		return properties.isNetworkEnabled();
	}


	@Override
	protected Object getData() {
		try {
			NetworkInfo ioInfo = new NetworkInfo();
			ioInfo.processSysTcpListenNum = BeanUtil.convert(ProcessUtils.execCmd(
					"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1 |egrep -w 'LISTEN' |wc -l"),
				Long.class);
			ioInfo.processSysTcpEstablishedNum = BeanUtil.convert(ProcessUtils.execCmd(
					"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1 |egrep -w 'ESTABLISHED' |wc -l"),
				Long.class);
			ioInfo.processSysTcpTimeWaitNum = BeanUtil.convert(ProcessUtils.execCmd(
					"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1 |egrep -w 'TIME_WAIT' |wc -l"),
				Long.class);
			ioInfo.processTcpListenNum = BeanUtil.convert(ProcessUtils.execCmd(
				"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1  |egrep -w '$PID' |egrep -w 'LISTEN' |wc -l".replaceAll(
					"\\$PID", getProcessID())), Long.class);
			ioInfo.processTcpEstablishedNum = BeanUtil.convert(ProcessUtils.execCmd(
				"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1  |egrep -w '$PID' |egrep -w 'ESTABLISHED' |wc -l".replaceAll(
					"\\$PID", getProcessID())), Long.class);
			ioInfo.processTcpTimeWaitNum = BeanUtil.convert(ProcessUtils.execCmd(
				"netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1  |egrep -w '$PID' |egrep -w 'TIME_WAIT' |wc -l".replaceAll(
					"\\$PID", getProcessID())), Long.class);
			return ioInfo;
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}


	private static class NetworkInfo {

		@FieldReport(name = "taotao.cloud.health.collect.network.process.tcp.listen.number", desc = "当前进程TCP LISTEN状态连接数")
		private long processTcpListenNum;
		@FieldReport(name = "taotao.cloud.health.collect.network.process.tcp.established.number", desc = "当前进程TCP ESTABLISHED状态连接数")
		private long processTcpEstablishedNum;
		@FieldReport(name = "taotao.cloud.health.collect.network.process.tcp.time_wait.number", desc = "当前进程TCP TIME_WAIT连接数")
		private long processTcpTimeWaitNum;
		@FieldReport(name = "taotao.cloud.health.collect.network.sys.tcp.listen.number", desc = "系统TCP LISTEN状态连接数")
		private long processSysTcpListenNum;
		@FieldReport(name = "taotao.cloud.health.collect.network.sys.tcp.established.number", desc = "系统TCP ESTABLISHED状态连接数")
		private long processSysTcpEstablishedNum;
		@FieldReport(name = "taotao.cloud.health.collect.network.sys.tcp.time_wait.number", desc = "系统TCP TIME_WAIT连接数")
		private long processSysTcpTimeWaitNum;
	}

}
