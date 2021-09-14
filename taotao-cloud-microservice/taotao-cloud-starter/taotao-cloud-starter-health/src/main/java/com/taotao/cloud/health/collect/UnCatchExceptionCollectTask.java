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

import com.taotao.cloud.common.utils.ExceptionUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.enums.WarnTypeEnum;
import com.taotao.cloud.health.model.CollectInfo;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * UnCatchExceptionCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:17:37
 */
public class UnCatchExceptionCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.health.collect.unCatchException";

	private Throwable lastException = null;
	private final CollectTaskProperties properties;

	public UnCatchExceptionCollectTask(CollectTaskProperties properties) {
		this.properties = properties;

		//注入异常处理
		UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
		if (!(handler instanceof DefaultUncaughtExceptionHandler)) {
			Thread.setDefaultUncaughtExceptionHandler(
				new DefaultUncaughtExceptionHandler(this, handler));
		}
	}

	@Override
	public int getTimeSpan() {
		return -1;
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
		return properties.isUncatchEnabled();
	}

	@Override
	protected CollectInfo getData() {
		return new UnCatchInfo(StringUtil.nullToEmpty(ExceptionUtil.trace2String(lastException)));
	}

	public static class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

		private Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler;
		private UnCatchExceptionCollectTask unCatchExceptionCheckTask;

		public DefaultUncaughtExceptionHandler(
			UnCatchExceptionCollectTask unCatchExceptionCheckTask,
			Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler) {
			this.unCatchExceptionCheckTask = unCatchExceptionCheckTask;
			this.lastUncaughtExceptionHandler = lastUncaughtExceptionHandler;
		}

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			try {
				if (e != null) {
					this.unCatchExceptionCheckTask.lastException = e;
					AbstractCollectTask.notifyMessage(WarnTypeEnum.ERROR, "未捕获错误",
						ExceptionUtil.trace2String(e));
					LogUtil.error(e, "未捕获错误");
				}
			} catch (Exception e2) {

			}
			if (lastUncaughtExceptionHandler != null) {
				lastUncaughtExceptionHandler.uncaughtException(t, e);
			}
		}
	}

	@Override
	public void close() throws Exception {
		UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
		if (handler instanceof DefaultUncaughtExceptionHandler) {
			Thread.setDefaultUncaughtExceptionHandler(
				((DefaultUncaughtExceptionHandler) handler).lastUncaughtExceptionHandler);
		}
	}

	private static class UnCatchInfo implements CollectInfo{

		@FieldReport(name = TASK_NAME + ".trace", desc = "未捕获错误堆栈")
		private String trace;

		public UnCatchInfo(String trace) {
			this.trace = trace;
		}
	}
}
