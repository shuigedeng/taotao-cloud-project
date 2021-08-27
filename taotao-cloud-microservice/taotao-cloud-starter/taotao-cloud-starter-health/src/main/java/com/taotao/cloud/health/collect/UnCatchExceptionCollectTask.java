package com.taotao.cloud.health.collect;


import com.taotao.cloud.common.utils.ExceptionUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.health.base.AbstractCollectTask;
import com.taotao.cloud.health.base.EnumWarnType;
import com.taotao.cloud.health.base.FieldReport;
import com.taotao.cloud.health.base.Report;
import com.taotao.cloud.health.config.HealthProperties;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author: chejiangyi
 * @version: 2019-07-26 13:36
 **/
public class UnCatchExceptionCollectTask extends AbstractCollectTask {

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

	private Throwable lastException = null;

	@Override
	public String getDesc() {
		return "全局未捕获异常拦截监测";
	}

	@Override
	public String getName() {
		return "uncatch.info";
	}

	@Override
	public boolean getEnabled() {
		return PropertyUtil.getPropertyCache("bsf.health.uncatch.enabled", true);
	}

	@Override
	public Report getReport() {
		return new Report(
			new UnCatchInfo(StringUtil.nullToEmpty(ExceptionUtil.trace2String(lastException))));
	}


	public static class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

		private Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler = null;
		private UnCatchExceptionCollectTask unCatchExceptionCheckTask = null;

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
					LogUtil.error(HealthProperties.Project,
						"未捕获错误", e);
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

		@FieldReport(name = "uncatch.trace", desc = "未捕获错误堆栈")
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
