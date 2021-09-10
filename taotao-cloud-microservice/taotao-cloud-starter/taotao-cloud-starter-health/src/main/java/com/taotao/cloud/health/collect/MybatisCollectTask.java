package com.taotao.cloud.health.collect;


import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.model.Collector.Hook;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;

/**
 * @author: chejiangyi
 * @version: 2019-08-02 09:41
 **/
public class MybatisCollectTask extends AbstractCollectTask {

	private CollectTaskProperties properties;
	private Collector collector;

	public MybatisCollectTask(Collector collector,CollectTaskProperties properties) {
		this.collector = collector;
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getMybatisTimeSpan();
	}

	@Override
	public String getDesc() {
		return "mybatis性能采集";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.mybatis.info";
	}

	@Override
	public boolean getEnabled() {
		return properties.isMybatisEnabled();
	}

	@Override
	protected Object getData() {
		SqlMybatisInfo info = new SqlMybatisInfo();

		Hook hook = this.collector.hook("taotao.cloud.health.collect.mybatis.sql.hook");
		if (hook != null) {
			info.hookCurrent = hook.getCurrent();
			info.hookError = hook.getLastErrorPerSecond();
			info.hookSuccess = hook.getLastSuccessPerSecond();
			info.hookList = hook.getMaxTimeSpanList().toText();
			info.hookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
		}
		return info;
	}

	private static class SqlMybatisInfo {

		@FieldReport(name = "taotao.cloud.health.collect.mybatis.sql.hook.error", desc = "mybatis 拦截上一次每秒出错次数")
		private Long hookError;
		@FieldReport(name = "taotao.cloud.health.collect.mybatis.sql.hook.success", desc = "mybatis 拦截上一次每秒成功次数")
		private Long hookSuccess;
		@FieldReport(name = "taotao.cloud.health.collect.mybatis.sql.hook.current", desc = "mybatis 拦截当前执行任务数")
		private Long hookCurrent;
		@FieldReport(name = "taotao.cloud.health.collect.mybatis.sql.hook.list.detail", desc = "mybatis 拦截历史最大耗时任务列表")
		private String hookList;
		@FieldReport(name = "taotao.cloud.health.collect.mybatis.sql.hook.list.minute.detail", desc = "mybatis 拦截历史最大耗时任务列表(每分钟)")
		private String hookListPerMinute;

		public SqlMybatisInfo() {
		}

		public SqlMybatisInfo(Long hookError, Long hookSuccess, Long hookCurrent, String hookList,
			String hookListPerMinute) {
			this.hookError = hookError;
			this.hookSuccess = hookSuccess;
			this.hookCurrent = hookCurrent;
			this.hookList = hookList;
			this.hookListPerMinute = hookListPerMinute;
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
	}
}
