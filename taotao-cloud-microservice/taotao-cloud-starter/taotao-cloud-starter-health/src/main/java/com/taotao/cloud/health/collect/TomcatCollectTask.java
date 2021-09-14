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

import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.model.CollectInfo;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServer;

/**
 * TomcatCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:17:03
 */
public class TomcatCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.health.collect.tomcat";

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
		return this.getClass().getName();
	}

	@Override
	public String getName() {
		return TASK_NAME;
	}

	@Override
	public boolean getEnabled() {
		return collectTaskProperties.isTomcatEnabled();
	}

	@Override
	protected CollectInfo getData() {
		try {
			ConfigurableWebServerApplicationContext context = ContextUtil.getConfigurableWebServerApplicationContext();
			if (context != null) {
				WebServer webServer = context.getWebServer();
				if (webServer instanceof TomcatWebServer) {

					Object getTomcat = ReflectionUtil.callMethod(webServer, "getTomcat", null);
					Object getConnector = ReflectionUtil.callMethod(getTomcat, "getConnector",
						null);
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
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}

	private static class TomcatInfo implements CollectInfo{

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
