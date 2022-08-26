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


import com.taotao.cloud.common.utils.exception.ExceptionUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.monitor.annotation.FieldReport;
import com.taotao.cloud.monitor.collect.AbstractCollectTask;
import com.taotao.cloud.monitor.collect.CollectInfo;
import com.taotao.cloud.monitor.properties.CollectTaskProperties;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;

/**
 * ThreadCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:16:17
 */
public class ThreadCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.monitor.collect.thread";

	private final ThreadMXBean threadMXBean;
	private final CollectTaskProperties properties;
	private HashMap<Long, Long> lastThreadUserTime = new HashMap<>();

	public ThreadCollectTask(CollectTaskProperties properties) {
		threadMXBean = ManagementFactory.getThreadMXBean();
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getMonitorThreadTimeSpan();
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
		return properties.isMonitorThreadEnabled();
	}


	@Override
	protected CollectInfo getData() {
		try {
			ThreadInfo threadInfo = new ThreadInfo();
			long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
			threadInfo.deadlockedThreadCount = (deadlockedThreads == null ? 0
				: deadlockedThreads.length);
			threadInfo.threadCount = threadMXBean.getThreadCount();
			java.lang.management.ThreadInfo runable = null;
			java.lang.management.ThreadInfo wait = null;
			java.lang.management.ThreadInfo block = null;

			HashMap<Long, Long> treadUserTime = new HashMap<>();
			for (java.lang.management.ThreadInfo info : threadMXBean.dumpAllThreads(false, false)) {
				treadUserTime.put(info.getThreadId(),
					threadMXBean.getThreadUserTime(info.getThreadId()));
				if (info.getThreadState() == Thread.State.RUNNABLE) {
					threadInfo.runnableThreadCount += 1;
					if (runable == null) {
						runable = info;
					} else {
						Long lastvalue = lastThreadUserTime.get(info.getThreadId());
						lastvalue = (lastvalue == null ? 0L : lastvalue);

						Long runablevalue = lastThreadUserTime.get(runable.getThreadId());
						runablevalue = (runablevalue == null ? 0L : runablevalue);
						if ((threadMXBean.getThreadUserTime(runable.getThreadId()) - runablevalue)
							< (
							threadMXBean.getThreadUserTime(info.getThreadId()) - lastvalue)) {
							runable = info;
						}
					}
				} else if (info.getThreadState() == Thread.State.BLOCKED) {
					threadInfo.blockedThreadCount += 1;
					if (block == null) {
						block = info;
					} else if (block.getBlockedTime() < info.getBlockedTime()) {
						block = info;
					}
				} else if (info.getThreadState() == Thread.State.WAITING) {
					threadInfo.waitingThreadCount += 1;
					if (wait == null) {
						wait = info;
					} else if (wait.getWaitedTime() < info.getWaitedTime()) {
						wait = info;
					}
				}
			}
			lastThreadUserTime = treadUserTime;
			if (runable != null) {
				threadInfo.maxRunnableDetail = ExceptionUtil.trace2String(runable.getStackTrace());
			}
			if (wait != null) {
				threadInfo.maxWaitingDetail = ExceptionUtil.trace2String(wait.getStackTrace());
			}
			if (block != null) {
				threadInfo.maxBlockedDetail = ExceptionUtil.trace2String(block.getStackTrace());
			}
			return threadInfo;
		} catch (Exception e) {
			if(LogUtil.isErrorEnabled()){
				LogUtil.error(e);
			}
		}
		return null;
	}


	private static class ThreadInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".deadlocked.count", desc = "死锁线程数")
		private Integer deadlockedThreadCount = 0;
		@FieldReport(name = TASK_NAME + ".total", desc = "线程总数")
		private Integer threadCount = 0;
		@FieldReport(name = TASK_NAME + ".runnable.count", desc = "运行线程总数")
		private Integer runnableThreadCount = 0;
		@FieldReport(name = TASK_NAME + ".blocked.count", desc = "阻塞线程总数")
		private Integer blockedThreadCount = 0;
		@FieldReport(name = TASK_NAME + ".waiting.count", desc = "等待线程总数")
		private Integer waitingThreadCount = 0;
		@FieldReport(name = TASK_NAME + ".runnable.max.detail", desc = "最近运行最耗时的线程详情")
		private String maxRunnableDetail = "";
		@FieldReport(name = TASK_NAME + ".blocked.max.detail", desc = "阻塞最耗时的线程详情")
		private String maxBlockedDetail = "";
		@FieldReport(name = TASK_NAME + ".waiting.max.detail", desc = "等待最耗时的线程详情")
		private String maxWaitingDetail = "";
	}
}
