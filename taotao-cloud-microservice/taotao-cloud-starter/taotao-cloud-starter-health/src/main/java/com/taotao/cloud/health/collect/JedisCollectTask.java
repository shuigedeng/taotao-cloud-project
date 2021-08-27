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
 * Redis性能参数收集
 *
 * @author: chejiangyi
 * @version: 2019-08-03 21:17
 **/
public class JedisCollectTask extends AbstractCollectTask {

	public JedisCollectTask() {

	}

	@Override
	public int getTimeSpan() {
		return PropertyUtil.getPropertyCache("bsf.health.jedis.timeSpan", 20);
	}

	@Override
	public String getDesc() {
		return "jedis性能采集";
	}

	@Override
	public String getName() {
		return "jedis.info";
	}

	@Override
	public boolean getEnabled() {
		return PropertyUtil.getPropertyCache("bsf.health.jedis.enabled", true);
	}

	@Override
	protected Object getData() {
		Object item = ContextUtil.getBean(
			ReflectionUtil.classForName("com.yh.csx.bsf.redis.impl.RedisClusterMonitor"), false);
		if (item != null) {
			try {
				ReflectionUtil.callMethod(item, "collect", null);
				JedisInfo info = new JedisInfo();
				String name = "jedis.cluster";
				info.detail = (String) Collector.Default.value(name + ".pool.detail").get();
				info.wait = (Integer) Collector.Default.value(name + ".pool.wait").get();
				info.active = (Integer) Collector.Default.value(name + ".pool.active").get();
				info.idle = (Integer) Collector.Default.value(name + ".pool.idle").get();
				info.lockInfo = (String) Collector.Default.value(name + ".lock.error.detail").get();
				Hook hook = Collector.Default.hook(name + ".hook");
				if (hook != null) {
					info.hookCurrent = hook.getCurrent();
					info.hookError = hook.getLastErrorPerSecond();
					info.hookSuccess = hook.getLastSuccessPerSecond();
					info.hookList = hook.getMaxTimeSpanList().toText();
					info.hookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
				}
				return info;
			} catch (Exception exp) {
				throw new HealthException(exp);
			}
		}
		return null;
	}

	private static class JedisInfo {

		@FieldReport(name = "jedis.cluster.pool.wait", desc = "jedis集群排队等待的请求数")
		private Integer wait;
		@FieldReport(name = "jedis.cluster.pool.active", desc = "jedis集群活动使用的请求数")
		private Integer active;
		@FieldReport(name = "jedis.cluster.pool.idle", desc = "jedis集群空闲的请求数")
		private Integer idle;
		@FieldReport(name = "jedis.cluster.pool.detail", desc = "jedis集群连接池详情")
		private String detail;
		@FieldReport(name = "jedis.cluster.hook.error", desc = "jedis集群拦截上一次每秒出错次数")
		private Long hookError;
		@FieldReport(name = "jedis.cluster.hook.success", desc = "jedis集群拦截上一次每秒成功次数")
		private Long hookSuccess;
		@FieldReport(name = "jedis.cluster.hook.current", desc = "jedis集群拦截当前执行任务数")
		private Long hookCurrent;
		@FieldReport(name = "jedis.cluster.hook.list.detail", desc = "jedis集群拦截历史最大耗时任务列表")
		private String hookList;
		@FieldReport(name = "jedis.cluster.hook.list.minute.detail", desc = "jedis集群拦截历史最大耗时任务列表(每分钟)")
		private String hookListPerMinute;
		@FieldReport(name = "jedis.cluster.lock.error.detail", desc = "jedis集群分布式锁异常信息")
		private String lockInfo;

		public JedisInfo() {
		}

		public JedisInfo(Integer wait, Integer active, Integer idle, String detail,
			Long hookError, Long hookSuccess, Long hookCurrent, String hookList,
			String hookListPerMinute, String lockInfo) {
			this.wait = wait;
			this.active = active;
			this.idle = idle;
			this.detail = detail;
			this.hookError = hookError;
			this.hookSuccess = hookSuccess;
			this.hookCurrent = hookCurrent;
			this.hookList = hookList;
			this.hookListPerMinute = hookListPerMinute;
			this.lockInfo = lockInfo;
		}

		public Integer getWait() {
			return wait;
		}

		public void setWait(Integer wait) {
			this.wait = wait;
		}

		public Integer getActive() {
			return active;
		}

		public void setActive(Integer active) {
			this.active = active;
		}

		public Integer getIdle() {
			return idle;
		}

		public void setIdle(Integer idle) {
			this.idle = idle;
		}

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}

		public Long getHookError() {
			return hookError;
		}

		public void setHookError(Long hookError) {
			this.hookError = hookError;
		}

		public Long getHookSuccess() {
			return hookSuccess;
		}

		public void setHookSuccess(Long hookSuccess) {
			this.hookSuccess = hookSuccess;
		}

		public Long getHookCurrent() {
			return hookCurrent;
		}

		public void setHookCurrent(Long hookCurrent) {
			this.hookCurrent = hookCurrent;
		}

		public String getHookList() {
			return hookList;
		}

		public void setHookList(String hookList) {
			this.hookList = hookList;
		}

		public String getHookListPerMinute() {
			return hookListPerMinute;
		}

		public void setHookListPerMinute(String hookListPerMinute) {
			this.hookListPerMinute = hookListPerMinute;
		}

		public String getLockInfo() {
			return lockInfo;
		}

		public void setLockInfo(String lockInfo) {
			this.lockInfo = lockInfo;
		}
	}
}
