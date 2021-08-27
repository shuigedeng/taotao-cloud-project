package com.taotao.cloud.health.collect;


import com.taotao.cloud.common.base.Collector;
import com.taotao.cloud.common.base.Collector.Hook;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.health.base.AbstractCollectTask;
import com.taotao.cloud.health.base.FieldReport;

/**
 * @author: chejiangyi
 * @version: 2019-08-02 09:41
 **/
public class MybatisCollectTask extends AbstractCollectTask {

	public MybatisCollectTask() {
	}

	@Override
	public int getTimeSpan() {
		return PropertyUtil.getPropertyCache("bsf.health.mybatis.timeSpan", 20);
	}

	@Override
	public String getDesc() {
		return "bsf mybatis性能采集";
	}

	@Override
	public String getName() {
		return "bsf.health.mybatis.info";
	}

	@Override
	public boolean getEnabled() {
		return PropertyUtil.getPropertyCache("bsf.health.mybatis.enabled", true);
	}

	@Override
	protected Object getData() {
		SqlMybatisInfo info = new SqlMybatisInfo();
		Hook hook = Collector.Default.hook("bsf.mybatis.sql.hook");
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

		@FieldReport(name = "mybatis.sql.hook.error", desc = "mybatis 拦截上一次每秒出错次数")
		private Long hookError;
		@FieldReport(name = "mybatis.sql.hook.success", desc = "mybatis 拦截上一次每秒成功次数")
		private Long hookSuccess;
		@FieldReport(name = "mybatis.sql.hook.current", desc = "mybatis 拦截当前执行任务数")
		private Long hookCurrent;
		@FieldReport(name = "mybatis.sql.hook.list.detail", desc = "mybatis 拦截历史最大耗时任务列表")
		private String hookList;
		@FieldReport(name = "mybatis.sql.hook.list.minute.detail", desc = "mybatis 拦截历史最大耗时任务列表(每分钟)")
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
