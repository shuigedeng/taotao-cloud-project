package com.taotao.cloud.health.collect;


import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.ExceptionUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.health.model.EnumWarnType;
import com.taotao.cloud.health.model.FieldReport;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author: chejiangyi
 * @version: 2019-07-26 13:36
 **/
public class UnCatchExceptionCollectTask extends AbstractCollectTask {

	private Throwable lastException = null;
	private CollectTaskProperties properties;

	public UnCatchExceptionCollectTask(CollectTaskProperties properties) {
		this.properties = properties;
	}

	public UnCatchExceptionCollectTask() {
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
		return "全局未捕获异常拦截监测";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.uncatch.info";
	}

	@Override
	public boolean getEnabled() {
		return properties.isUncatchEnabled();
	}

	@Override
	public Report getReport() {
		return new Report(
			new UnCatchInfo(StringUtil.nullToEmpty(ExceptionUtil.trace2String(lastException))));
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
					AbstractCollectTask.notifyMessage(EnumWarnType.ERROR, "未捕获错误",
						ExceptionUtil.trace2String(e));
					LogUtil.error(UnCatchExceptionCollectTask.class, StarterName.HEALTH_STARTER, e,
						"未捕获错误");
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
		//解除异常处理
		UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
		if (handler instanceof DefaultUncaughtExceptionHandler) {
			Thread.setDefaultUncaughtExceptionHandler(
				((DefaultUncaughtExceptionHandler) handler).lastUncaughtExceptionHandler);
		}
	}

	private static class UnCatchInfo {

		@FieldReport(name = "taotao.cloud.health.collect.uncatch.trace", desc = "未捕获错误堆栈")
		private String trace;

		public UnCatchInfo() {
		}

		public UnCatchInfo(String trace) {
			this.trace = trace;
		}

		public String getTrace() {
			return trace;
		}

		public void setTrace(String trace) {
			this.trace = trace;
		}
	}
}
