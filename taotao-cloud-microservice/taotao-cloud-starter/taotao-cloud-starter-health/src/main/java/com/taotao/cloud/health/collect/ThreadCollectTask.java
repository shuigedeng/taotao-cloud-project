package com.taotao.cloud.health.collect;


import com.taotao.cloud.common.utils.ExceptionUtil;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;

/**
 * @author: chejiangyi
 * @version: 2019-07-24 18:46
 **/
public class ThreadCollectTask extends AbstractCollectTask {

	ThreadMXBean threadMXBean;
	CollectTaskProperties properties;
	private HashMap<Long, Long> lastThreadUserTime = new HashMap<>();

	public ThreadCollectTask(CollectTaskProperties properties) {
		threadMXBean = ManagementFactory.getThreadMXBean();
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getThreadTimeSpan();
	}

	@Override
	public String getDesc() {
		return "线程监测";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.thread.info";
	}

	@Override
	public boolean getEnabled() {
		return properties.isThreadEnabled();
	}


	@Override
	protected Object getData() {
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
				threadInfo.runableThreadCount += 1;
				if (runable == null) {
					runable = info;
				} else {
					Long lastvalue = lastThreadUserTime.get(info.getThreadId());
					lastvalue = (lastvalue == null ? 0L : lastvalue);

					Long runablevalue = lastThreadUserTime.get(runable.getThreadId());
					runablevalue = (runablevalue == null ? 0L : runablevalue);
					if ((threadMXBean.getThreadUserTime(runable.getThreadId()) - runablevalue) < (
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
			threadInfo.setMaxRunableDetail(ExceptionUtil.trace2String(runable.getStackTrace()));
		}
		if (wait != null) {
			threadInfo.setMaxWaitingDetail(ExceptionUtil.trace2String(wait.getStackTrace()));
		}
		if (block != null) {
			threadInfo.setMaxBlockedDetail(ExceptionUtil.trace2String(block.getStackTrace()));
		}
		return threadInfo;
	}


	private static class ThreadInfo {

		@FieldReport(name = "taotao.cloud.health.collect.thread.deadlocked.count", desc = "死锁线程数")
		private double deadlockedThreadCount;
		@FieldReport(name = "taotao.cloud.health.collect.thread.total", desc = "线程总数")
		private double threadCount;
		@FieldReport(name = "taotao.cloud.health.collect.thread.runable.count", desc = "运行线程总数")
		private double runableThreadCount;
		@FieldReport(name = "taotao.cloud.health.collect.thread.blocked.count", desc = "阻塞线程总数")
		private double blockedThreadCount;
		@FieldReport(name = "taotao.cloud.health.collect.thread.waiting.count", desc = "等待线程总数")
		private double waitingThreadCount;
		@FieldReport(name = "taotao.cloud.health.collect.thread.runable.max.detail", desc = "最近运行最耗时的线程详情")
		private String maxRunableDetail;
		@FieldReport(name = "taotao.cloud.health.collect.thread.blocked.max.detail", desc = "阻塞最耗时的线程详情")
		private String maxBlockedDetail;
		@FieldReport(name = "taotao.cloud.health.collect.thread.waiting.max.detail", desc = "等待最耗时的线程详情")
		private String maxWaitingDetail;

		public ThreadInfo() {
		}

		public ThreadInfo(double deadlockedThreadCount, double threadCount,
			double runableThreadCount,
			double blockedThreadCount, double waitingThreadCount, String maxRunableDetail,
			String maxBlockedDetail, String maxWaitingDetail) {
			this.deadlockedThreadCount = deadlockedThreadCount;
			this.threadCount = threadCount;
			this.runableThreadCount = runableThreadCount;
			this.blockedThreadCount = blockedThreadCount;
			this.waitingThreadCount = waitingThreadCount;
			this.maxRunableDetail = maxRunableDetail;
			this.maxBlockedDetail = maxBlockedDetail;
			this.maxWaitingDetail = maxWaitingDetail;
		}

		public double getDeadlockedThreadCount() {
			return deadlockedThreadCount;
		}

		public void setDeadlockedThreadCount(double deadlockedThreadCount) {
			this.deadlockedThreadCount = deadlockedThreadCount;
		}

		public double getThreadCount() {
			return threadCount;
		}

		public void setThreadCount(double threadCount) {
			this.threadCount = threadCount;
		}

		public double getRunableThreadCount() {
			return runableThreadCount;
		}

		public void setRunableThreadCount(double runableThreadCount) {
			this.runableThreadCount = runableThreadCount;
		}

		public double getBlockedThreadCount() {
			return blockedThreadCount;
		}

		public void setBlockedThreadCount(double blockedThreadCount) {
			this.blockedThreadCount = blockedThreadCount;
		}

		public double getWaitingThreadCount() {
			return waitingThreadCount;
		}

		public void setWaitingThreadCount(double waitingThreadCount) {
			this.waitingThreadCount = waitingThreadCount;
		}

		public String getMaxRunableDetail() {
			return maxRunableDetail;
		}

		public void setMaxRunableDetail(String maxRunableDetail) {
			this.maxRunableDetail = maxRunableDetail;
		}

		public String getMaxBlockedDetail() {
			return maxBlockedDetail;
		}

		public void setMaxBlockedDetail(String maxBlockedDetail) {
			this.maxBlockedDetail = maxBlockedDetail;
		}

		public String getMaxWaitingDetail() {
			return maxWaitingDetail;
		}

		public void setMaxWaitingDetail(String maxWaitingDetail) {
			this.maxWaitingDetail = maxWaitingDetail;
		}
	}
}
