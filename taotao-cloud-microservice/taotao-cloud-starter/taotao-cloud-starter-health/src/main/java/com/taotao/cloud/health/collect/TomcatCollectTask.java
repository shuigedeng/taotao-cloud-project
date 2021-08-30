package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.health.model.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServer;


/**
 * @author: chejiangyi
 * @version: 2019-08-03 11:59
 **/
public class TomcatCollectTask extends AbstractCollectTask {

	private CollectTaskProperties collectTaskProperties;

	public TomcatCollectTask(CollectTaskProperties collectTaskProperties) {
		this.collectTaskProperties = collectTaskProperties;
	}

	@Override
	public int getTimeSpan() {
		return collectTaskProperties.getTomcatTimeSpan();
	}

	@Override
	public String getDesc() {
		return "tomcat性能采集";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.tomcat.info";
	}

	@Override
	public boolean getEnabled() {
		return collectTaskProperties.isTomcatEnabled();
	}

	@Override
	protected Object getData() {
		ConfigurableWebServerApplicationContext context = ContextUtil.getConfigurableWebServerApplicationContext();
		if (context != null) {
			WebServer webServer = context.getWebServer();
			if (webServer instanceof TomcatWebServer) {

				Object getTomcat = ReflectionUtil.callMethod(webServer, "getTomcat", null);
				Object getConnector = ReflectionUtil.callMethod(getTomcat, "getConnector", null);
				Object getProtocolHandler = ReflectionUtil.callMethod(getConnector,
					"getProtocolHandler", null);
				Object executor = ReflectionUtil.callMethod(getProtocolHandler, "getExecutor",
					null);

				Class<?> poolCls = ReflectionUtil.tryClassForName(
					"org.apache.tomcat.util.threads.ThreadPoolExecutor");

				if (executor != null && poolCls.isAssignableFrom(executor.getClass())) {
					if (executor instanceof ThreadPoolExecutor) {
						TomcatInfo tomcatInfo = new TomcatInfo();
						ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;

						tomcatInfo.activeCount = pool.getActiveCount();
						tomcatInfo.corePoolSize = pool.getCorePoolSize();
						tomcatInfo.poolSizeCount = pool.getPoolSize();
						tomcatInfo.poolSizeMax = pool.getMaximumPoolSize();
						tomcatInfo.poolSizeLargest = pool.getLargestPoolSize();
						tomcatInfo.queueSize = pool.getQueue().size();
						tomcatInfo.taskCount = pool.getTaskCount();
						tomcatInfo.taskCompleted = pool.getCompletedTaskCount();
						return tomcatInfo;
					}
				}
			}

		}
		return null;
	}

	private static class TomcatInfo {

		@FieldReport(name = "taotao.cloud.health.collect.tomcat.threadPool.active.count", desc = "tomcat 线程池活动线程数")
		private Integer activeCount;
		@FieldReport(name = "taotao.cloud.health.collect.tomcat.threadPool.core.poolSize", desc = "tomcat 线程池核心线程数")
		private Integer corePoolSize;
		@FieldReport(name = "taotao.cloud.health.collect.tomcat.threadPool.poolSize.largest", desc = "tomcat 线程池历史最大线程数")
		private Integer poolSizeLargest;
		@FieldReport(name = "taotao.cloud.health.collect.tomcat.threadPool.poolSize.max", desc = "tomcat 线程池最大线程数")
		private Integer poolSizeMax;
		@FieldReport(name = "taotao.cloud.health.collect.tomcat.threadPool.poolSize.count", desc = "tomcat 线程池当前线程数")
		private Integer poolSizeCount;
		@FieldReport(name = "taotao.cloud.health.collect.tomcat.threadPool.queue.size", desc = "tomcat 线程池当前排队等待任务数")
		private Integer queueSize;
		@FieldReport(name = "taotao.cloud.health.collect.tomcat.threadPool.task.count", desc = "tomcat 线程池历史任务数")
		private Long taskCount;
		@FieldReport(name = "taotao.cloud.health.collect.tomcat.threadPool.task.completed", desc = "tomcat 线程池已完成任务数")
		private Long taskCompleted;

//        @FieldReport(name = "tomcat.threadPool.task.hook.error", desc = "tomcat 线程池拦截上一次每秒出错次数")
//        private Integer taskHookError;
//        @FieldReport(name = "tomcat.threadPool.task.hook.success", desc = "tomcat 线程池拦截上一次每秒成功次数")
//        private Integer taskHookSuccess;
//        @FieldReport(name = "tomcat.threadPool.task.hook.current", desc = "tomcat 线程池拦截当前执行任务数")
//        private Integer taskHookCurrent;
//        @FieldReport(name = "tomcat.threadPool.task.hook.list", desc = "tomcat 线程池拦截历史最大耗时任务列表")
//        private String taskHookList;

		public TomcatInfo() {
		}

		public TomcatInfo(Integer activeCount, Integer corePoolSize, Integer poolSizeLargest,
			Integer poolSizeMax, Integer poolSizeCount, Integer queueSize, Long taskCount,
			Long taskCompleted) {
			this.activeCount = activeCount;
			this.corePoolSize = corePoolSize;
			this.poolSizeLargest = poolSizeLargest;
			this.poolSizeMax = poolSizeMax;
			this.poolSizeCount = poolSizeCount;
			this.queueSize = queueSize;
			this.taskCount = taskCount;
			this.taskCompleted = taskCompleted;
		}

		public Integer getActiveCount() {
			return activeCount;
		}

		public void setActiveCount(Integer activeCount) {
			this.activeCount = activeCount;
		}

		public Integer getCorePoolSize() {
			return corePoolSize;
		}

		public void setCorePoolSize(Integer corePoolSize) {
			this.corePoolSize = corePoolSize;
		}

		public Integer getPoolSizeLargest() {
			return poolSizeLargest;
		}

		public void setPoolSizeLargest(Integer poolSizeLargest) {
			this.poolSizeLargest = poolSizeLargest;
		}

		public Integer getPoolSizeMax() {
			return poolSizeMax;
		}

		public void setPoolSizeMax(Integer poolSizeMax) {
			this.poolSizeMax = poolSizeMax;
		}

		public Integer getPoolSizeCount() {
			return poolSizeCount;
		}

		public void setPoolSizeCount(Integer poolSizeCount) {
			this.poolSizeCount = poolSizeCount;
		}

		public Integer getQueueSize() {
			return queueSize;
		}

		public void setQueueSize(Integer queueSize) {
			this.queueSize = queueSize;
		}

		public Long getTaskCount() {
			return taskCount;
		}

		public void setTaskCount(Long taskCount) {
			this.taskCount = taskCount;
		}

		public Long getTaskCompleted() {
			return taskCompleted;
		}

		public void setTaskCompleted(Long taskCompleted) {
			this.taskCompleted = taskCompleted;
		}
	}
}
