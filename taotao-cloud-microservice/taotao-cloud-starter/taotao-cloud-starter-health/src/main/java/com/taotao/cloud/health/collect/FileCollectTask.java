//package com.taotao.cloud.health.collect;
//
//
//import com.taotao.cloud.common.utils.ReflectionUtil;
//import com.taotao.cloud.core.base.Collector;
//import com.taotao.cloud.core.base.Collector.Hook;
//import com.taotao.cloud.core.utils.PropertyUtil;
//import com.taotao.cloud.health.base.FieldReport;
//
///**
// * @author Huang Zhaoping
// */
//public class FileCollectTask extends AbstractCollectTask {
//
//	@Override
//	public int getTimeSpan() {
//		return PropertyUtil.getPropertyCache("bsf.health.file.timeSpan", 20);
//	}
//
//	@Override
//	public boolean getEnabled() {
//		return PropertyUtil.getPropertyCache("bsf.health.file.enabled", true);
//	}
//
//	@Override
//	public String getDesc() {
//		return "File服务性能采集";
//	}
//
//	@Override
//	public String getName() {
//		return "file.info";
//	}
//
//	@Override
//	protected Object getData() {
//		FileInfo data = new FileInfo();
//		if (!PropertyUtil.getPropertyCache("bsf.file.enabled", false)) {
//			data.provider = "file";
//			Hook hook = (Collector.Hook) ReflectionUtil.callMethod(
//				ReflectionUtil.classForName("com.yh.csx.bsf.file.impl.FileProviderMonitor"), "hook",
//				null);
//			data.hookCurrent = hook.getCurrent();
//			data.hookError = hook.getLastErrorPerSecond();
//			data.hookSuccess = hook.getLastSuccessPerSecond();
//			data.hookList = hook.getMaxTimeSpanList().toText();
//			data.hookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
//		}
//		return data;
//	}
//
//	private static class FileInfo {
//
//		@FieldReport(name = "file.provider", desc = "File服务提供者")
//		private String provider;
//		@FieldReport(name = "file.hook.error", desc = "File服务拦截上一次每秒出错次数")
//		private Long hookError;
//		@FieldReport(name = "file.hook.success", desc = "File服务拦截上一次每秒成功次数")
//		private Long hookSuccess;
//		@FieldReport(name = "file.hook.current", desc = "File服务拦截当前执行任务数")
//		private Long hookCurrent;
//		@FieldReport(name = "file.hook.list.detail", desc = "File服务拦截历史最大耗时任务列表")
//		private String hookList;
//		@FieldReport(name = "file.hook.list.minute.detail", desc = "File服务拦截历史最大耗时任务列表(每分钟)")
//		private String hookListPerMinute;
//
//		public FileInfo() {
//		}
//
//		public FileInfo(Long hookError, Long hookSuccess, Long hookCurrent, String hookList,
//			String hookListPerMinute) {
//			this.hookError = hookError;
//			this.hookSuccess = hookSuccess;
//			this.hookCurrent = hookCurrent;
//			this.hookList = hookList;
//			this.hookListPerMinute = hookListPerMinute;
//		}
//
//		public String getProvider() {
//			return provider;
//		}
//
//		public void setProvider(String provider) {
//			this.provider = provider;
//		}
//
//		public Long getHookError() {
//			return hookError;
//		}
//
//		public void setHookError(Long hookError) {
//			this.hookError = hookError;
//		}
//
//		public Long getHookSuccess() {
//			return hookSuccess;
//		}
//
//		public void setHookSuccess(Long hookSuccess) {
//			this.hookSuccess = hookSuccess;
//		}
//
//		public Long getHookCurrent() {
//			return hookCurrent;
//		}
//
//		public void setHookCurrent(Long hookCurrent) {
//			this.hookCurrent = hookCurrent;
//		}
//
//		public String getHookList() {
//			return hookList;
//		}
//
//		public void setHookList(String hookList) {
//			this.hookList = hookList;
//		}
//
//		public String getHookListPerMinute() {
//			return hookListPerMinute;
//		}
//
//		public void setHookListPerMinute(String hookListPerMinute) {
//			this.hookListPerMinute = hookListPerMinute;
//		}
//	}
//}
