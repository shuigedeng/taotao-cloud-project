package com.taotao.cloud.health.collect;


import com.taotao.cloud.common.base.Collector;
import com.taotao.cloud.common.base.Collector.Hook;
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.health.base.AbstractCollectTask;
import com.taotao.cloud.health.base.FieldReport;
import com.taotao.cloud.health.base.HealthException;

/**
 * bsf eureka 客户端性能采集
 *
 * @author: chejiangyi
 * @version: 2019-07-31 16:32
 **/
public class BsfEurekaCollectTask extends AbstractCollectTask {

	public BsfEurekaCollectTask() {
	}

	@Override
	public int getTimeSpan() {
		return PropertyUtil.getPropertyCache("bsf.health.eureka.timeSpan", 20);
	}

	@Override
	public String getDesc() {
		return "eureka性能采集";
	}

	@Override
	public String getName() {
		return "eureka.info";
	}

	@Override
	public boolean getEnabled() {
		return PropertyUtil.getPropertyCache("bsf.health.eureka.enabled", true);
	}

	@Override
	protected Object getData() {
		Object item = ContextUtil.getBean(
			ReflectionUtil.classForName("com.yh.csx.bsf.eureka.client.EurekaMonitor"), false);
		if (item != null) {
			try {
				ReflectionUtil.callMethod(item, "collect", null);
				EurekaClientInfo info = new EurekaClientInfo();
				info.clientLeased = (Integer) Collector.Default.value(
					"bsf.eureka.client.pool.leased").get();
				info.clientAvailable = (Integer) Collector.Default.value(
					"bsf.eureka.client.pool.available").get();
				info.clientMax = (Integer) Collector.Default.value("bsf.eureka.client.pool.max")
					.get();
				info.clientPending = (Integer) Collector.Default.value(
					"bsf.eureka.client.pool.pending").get();
				info.clientDetail = (String) Collector.Default.value(
					"bsf.eureka.client.pool.detail").get();
				info.instanceStatus = (String) Collector.Default.value("bsf.eureka.instance.status")
					.get();
				Hook hook = Collector.Default.hook("bsf.eureka.client.hook");
				if (hook != null) {
					info.clientHookCurrent = hook.getCurrent();
					info.clientHookError = hook.getLastErrorPerSecond();
					info.clientHookSuccess = hook.getLastSuccessPerSecond();
					info.clientHookList = hook.getMaxTimeSpanList().toText();
					info.clientHookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
				}
				return info;
			} catch (Exception exp) {
				throw new HealthException(exp);
			}
		}
		return null;
	}


	private static class EurekaClientInfo {

		@FieldReport(name = "eureka.client.pool.leased", desc = "eureka client租借http连接数")
		private Integer clientLeased;
		@FieldReport(name = "eureka.client.pool.available", desc = "eureka client有效http连接数")
		private Integer clientAvailable;
		@FieldReport(name = "eureka.client.pool.max", desc = "eureka client最大http连接数")
		private Integer clientMax;
		@FieldReport(name = "eureka.client.pool.pending", desc = "eureka client等待http连接数")
		private Integer clientPending;
		@FieldReport(name = "eureka.client.pool.detail", desc = "eureka client详情")
		private String clientDetail;
		@FieldReport(name = "eureka.instance.status", desc = "eureka 实例状态")
		private String instanceStatus;
		@FieldReport(name = "eureka.client.hook.error", desc = "eureka client拦截上一次每秒出错次数")
		private Long clientHookError;
		@FieldReport(name = "eureka.client.hook.success", desc = "eureka client拦截上一次每秒成功次数")
		private Long clientHookSuccess;
		@FieldReport(name = "eureka.client.hook.current", desc = "eureka client拦截当前执行任务数")
		private Long clientHookCurrent;
		@FieldReport(name = "eureka.client.hook.list.detail", desc = "eureka client拦截历史最大耗时任务列表")
		private String clientHookList;
		@FieldReport(name = "eureka.client.hook.list.minute.detail", desc = "eureka client拦截历史最大耗时任务列表(每分钟)")
		private String clientHookListPerMinute;

		public EurekaClientInfo() {
		}

		public EurekaClientInfo(Integer clientLeased, Integer clientAvailable, Integer clientMax,
			Integer clientPending, String clientDetail, String instanceStatus,
			Long clientHookError, Long clientHookSuccess, Long clientHookCurrent,
			String clientHookList, String clientHookListPerMinute) {
			this.clientLeased = clientLeased;
			this.clientAvailable = clientAvailable;
			this.clientMax = clientMax;
			this.clientPending = clientPending;
			this.clientDetail = clientDetail;
			this.instanceStatus = instanceStatus;
			this.clientHookError = clientHookError;
			this.clientHookSuccess = clientHookSuccess;
			this.clientHookCurrent = clientHookCurrent;
			this.clientHookList = clientHookList;
			this.clientHookListPerMinute = clientHookListPerMinute;
		}

		public Integer getClientLeased() {
			return clientLeased;
		}

		public void setClientLeased(Integer clientLeased) {
			this.clientLeased = clientLeased;
		}

		public Integer getClientAvailable() {
			return clientAvailable;
		}

		public void setClientAvailable(Integer clientAvailable) {
			this.clientAvailable = clientAvailable;
		}

		public Integer getClientMax() {
			return clientMax;
		}

		public void setClientMax(Integer clientMax) {
			this.clientMax = clientMax;
		}

		public Integer getClientPending() {
			return clientPending;
		}

		public void setClientPending(Integer clientPending) {
			this.clientPending = clientPending;
		}

		public String getClientDetail() {
			return clientDetail;
		}

		public void setClientDetail(String clientDetail) {
			this.clientDetail = clientDetail;
		}

		public String getInstanceStatus() {
			return instanceStatus;
		}

		public void setInstanceStatus(String instanceStatus) {
			this.instanceStatus = instanceStatus;
		}

		public Long getClientHookError() {
			return clientHookError;
		}

		public void setClientHookError(Long clientHookError) {
			this.clientHookError = clientHookError;
		}

		public Long getClientHookSuccess() {
			return clientHookSuccess;
		}

		public void setClientHookSuccess(Long clientHookSuccess) {
			this.clientHookSuccess = clientHookSuccess;
		}

		public Long getClientHookCurrent() {
			return clientHookCurrent;
		}

		public void setClientHookCurrent(Long clientHookCurrent) {
			this.clientHookCurrent = clientHookCurrent;
		}

		public String getClientHookList() {
			return clientHookList;
		}

		public void setClientHookList(String clientHookList) {
			this.clientHookList = clientHookList;
		}

		public String getClientHookListPerMinute() {
			return clientHookListPerMinute;
		}

		public void setClientHookListPerMinute(String clientHookListPerMinute) {
			this.clientHookListPerMinute = clientHookListPerMinute;
		}
	}
}
