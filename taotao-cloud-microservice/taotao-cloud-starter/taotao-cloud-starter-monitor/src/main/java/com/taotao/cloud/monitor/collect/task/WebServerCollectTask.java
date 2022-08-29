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

import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.reflect.ReflectionUtils;
import com.taotao.cloud.monitor.annotation.FieldReport;
import com.taotao.cloud.monitor.collect.AbstractCollectTask;
import com.taotao.cloud.monitor.collect.CollectInfo;
import com.taotao.cloud.monitor.properties.CollectTaskProperties;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
import org.springframework.boot.web.embedded.jetty.JettyWebServer;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.embedded.undertow.UndertowWebServer;
import org.springframework.boot.web.server.WebServer;

/**
 * TomcatCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:17:03
 */
public class WebServerCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.monitor.collect.webserver";

	private final CollectTaskProperties collectTaskProperties;

	public WebServerCollectTask(CollectTaskProperties collectTaskProperties) {
		this.collectTaskProperties = collectTaskProperties;
	}

	@Override
	public int getTimeSpan() {
		return collectTaskProperties.getWebServerTimeSpan();
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
		return collectTaskProperties.getWebServerEnabled();
	}

	@Override
	protected CollectInfo getData() {
		try {
			ConfigurableWebServerApplicationContext context = ContextUtils.getConfigurableWebServerApplicationContext();
			if (context != null) {
				WebServer webServer = context.getWebServer();
				if (webServer instanceof TomcatWebServer) {

					Object getTomcat = ReflectionUtils.callMethod(webServer, "getTomcat", null);
					Object getConnector = ReflectionUtils.callMethod(getTomcat, "getConnector",
						null);
					Object getProtocolHandler = ReflectionUtils.callMethod(getConnector,
						"getProtocolHandler", null);
					Object executor = ReflectionUtils.callMethod(getProtocolHandler, "getExecutor",
						null);

					Class<?> poolCls = ReflectionUtils.tryClassForName(
						"org.apache.tomcat.util.threads.ThreadPoolExecutor");

					if (executor != null && poolCls.isAssignableFrom(executor.getClass())) {
						if (executor instanceof ThreadPoolExecutor pool) {
							TomcatInfo tomcatInfo = new TomcatInfo();

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

				if (webServer instanceof UndertowWebServer) {
					UndertowInfo info = new UndertowInfo();

					Object undertow = ReflectionUtils.tryGetValue(webServer, "undertow");
					info.bufferSize = ReflectionUtils.tryGetValue(undertow, "bufferSize");
					info.ioThreads = ReflectionUtils.tryGetValue(undertow, "ioThreads");
					info.workerThreads = ReflectionUtils.tryGetValue(undertow, "workerThreads");
					info.listeners = ReflectionUtils.tryGetValue(undertow, "listeners.size");
					info.listenerInfo = ReflectionUtils.tryGetValue(undertow, "listenerInfo.size");
					info.internalWorker = ReflectionUtils.tryGetValue(undertow, "internalWorker");
					info.byteBufferPool = ReflectionUtils.tryGetValue(undertow,
						"byteBufferPool.getBufferSize");
					info.channels = ReflectionUtils.tryGetValue(undertow, "channels.size");
					info.workerOptions = ReflectionUtils.tryGetValue(undertow, "workerOptions.size");
					info.socketOptions = ReflectionUtils.tryGetValue(undertow, "socketOptions.size");
					info.serverOptions = ReflectionUtils.tryGetValue(undertow, "serverOptions.size");
					info.xinoName = ReflectionUtils.tryGetValue(undertow, "getXnio.getName");

					Object mxBean = ReflectionUtils.tryGetValue(undertow, "getWorker.getMXBean");
					info.workName = ReflectionUtils.tryGetValue(mxBean, "getName");
					info.workerCorePoolSize = ReflectionUtils.tryGetValue(mxBean,
						"getCoreWorkerPoolSize");
					info.workerMaxPoolSize = ReflectionUtils.tryGetValue(mxBean,
						"getMaxWorkerPoolSize");
					info.workerIoThreadCount = ReflectionUtils.tryGetValue(mxBean,
						"getIoThreadCount");
					info.workerBusyThreadCount = ReflectionUtils.tryGetValue(mxBean,
						"getBusyWorkerThreadCount");
					info.workerProviderName = ReflectionUtils.tryGetValue(mxBean, "getProviderName");
					info.workerServerMXBeans = ReflectionUtils.tryGetValue(mxBean,
						"getServerMXBeans.size");
					info.workerPoolSize = ReflectionUtils.tryGetValue(mxBean, "getWorkerPoolSize");
					info.workerQueueSize = ReflectionUtils.tryGetValue(mxBean, "getWorkerQueueSize");

					return info;
				}

				if(webServer instanceof NettyWebServer){
					Object httpServer = ReflectionUtils.tryGetValue(webServer, "httpServer");
					//Object metricsRecorder = ReflectionUtil.tryGetValue(httpServer, "configuration.metricsRecorder");
					//LogUtil.info("sdfsdfasdfasdf");
				}

				if(webServer instanceof JettyWebServer){
					//todo
				}
			}
		} catch (Exception e) {
			if(LogUtils.isErrorEnabled()){
				LogUtils.error(e);
			}
		}
		return null;
	}

	private static class UndertowInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".buffer.size", desc = "undertow buffer大小")
		private Integer bufferSize = 0;
		@FieldReport(name = TASK_NAME + ".io.threads", desc = "undertow io线程数")
		private Integer ioThreads = 0;
		@FieldReport(name = TASK_NAME + ".worker.threads", desc = "undertow 工作线程数")
		private Integer workerThreads = 0;
		@FieldReport(name = TASK_NAME + ".listeners.size", desc = "undertow listeners数量")
		private Integer listeners = 0;
		@FieldReport(name = TASK_NAME + ".listenerinfo.size", desc = "undertow listenerInfo数量")
		private Integer listenerInfo = 0;
		@FieldReport(name = TASK_NAME + ".internal.worker", desc = "undertow 是否内部work")
		private Boolean internalWorker = false;
		@FieldReport(name = TASK_NAME + ".byte.buffer.pool", desc = "undertow byteBufferPool大小")
		private Integer byteBufferPool = 0;
		@FieldReport(name = TASK_NAME + ".channels.size", desc = "undertow 管道数量")
		private Integer channels = 0;
		@FieldReport(name = TASK_NAME + ".worker.options.size", desc = "undertow workerOptions数量")
		private Integer workerOptions = 0;
		@FieldReport(name = TASK_NAME + ".socket.options.size", desc = "undertow socketOptions数量")
		private Integer socketOptions = 0;
		@FieldReport(name = TASK_NAME + ".server.options.size", desc = "undertow serverOptions数量")
		private Integer serverOptions = 0;
		@FieldReport(name = TASK_NAME + ".xino.name", desc = "undertow xion 名称")
		private String xinoName = "";
		@FieldReport(name = TASK_NAME + ".work.name", desc = "undertow work 名称")
		private String workName = "";
		@FieldReport(name = TASK_NAME + ".worker.core.pool.size", desc = "undertow 核心工作线程池大小")
		private Integer workerCorePoolSize = 0;
		@FieldReport(name = TASK_NAME + ".worker.max.pool.size", desc = "undertow 最大工作线程池大小")
		private Integer workerMaxPoolSize = 0;
		@FieldReport(name = TASK_NAME + ".worker.io.thread.count", desc = "undertow I/O 线程数")
		private Integer workerIoThreadCount = 0;
		@FieldReport(name = TASK_NAME + ".worker.busy.thread.count", desc = "undertow 工作池中繁忙线程数")
		private Integer workerBusyThreadCount = 0;
		@FieldReport(name = TASK_NAME + ".worker.provider.name", desc = "undertow work提供者名称")
		private String workerProviderName = "";
		@FieldReport(name = TASK_NAME
			+ ".worker.werver.mxbeans.size", desc = "undertow server mxbean数量")
		private Integer workerServerMXBeans = 0;
		@FieldReport(name = TASK_NAME + ".worker.pool.size", desc = "undertow 获取工作池中线程数")
		private Integer workerPoolSize = 0;
		@FieldReport(name = TASK_NAME + ".worker.queue.size", desc = "undertow 工作队列中任务数量")
		private Integer workerQueueSize = 0;
	}

	private static class TomcatInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".active.count", desc = "tomcat 线程池活动线程数")
		private Integer activeCount = 0;
		@FieldReport(name = TASK_NAME + ".core.poolSize", desc = "tomcat 线程池核心线程数")
		private Integer corePoolSize = 0;
		@FieldReport(name = TASK_NAME + ".poolSize.largest", desc = "tomcat 线程池历史最大线程数")
		private Integer poolSizeLargest = 0;
		@FieldReport(name = TASK_NAME + ".poolSize.max", desc = "tomcat 线程池最大线程数")
		private Integer poolSizeMax = 0;
		@FieldReport(name = TASK_NAME + ".poolSize.count", desc = "tomcat 线程池当前线程数")
		private Integer poolSizeCount = 0;
		@FieldReport(name = TASK_NAME + ".queue.size", desc = "tomcat 线程池当前排队等待任务数")
		private Integer queueSize = 0;
		@FieldReport(name = TASK_NAME + ".task.count", desc = "tomcat 线程池历史任务数")
		private Long taskCount = 0L;
		@FieldReport(name = TASK_NAME + ".task.completed", desc = "tomcat 线程池已完成任务数")
		private Long taskCompleted = 0L;

//        @FieldReport(name = "tomcat.threadPool.task.hook.error", desc = "tomcat 线程池拦截上一次每秒出错次数")
//        private Integer taskHookError;
//        @FieldReport(name = "tomcat.threadPool.task.hook.success", desc = "tomcat 线程池拦截上一次每秒成功次数")
//        private Integer taskHookSuccess;
//        @FieldReport(name = "tomcat.threadPool.task.hook.current", desc = "tomcat 线程池拦截当前执行任务数")
//        private Integer taskHookCurrent;
//        @FieldReport(name = "tomcat.threadPool.task.hook.list", desc = "tomcat 线程池拦截历史最大耗时任务列表")
//        private String taskHookList;
	}
}
