package com.taotao.cloud.health.collect;


import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.health.base.AbstractCollectTask;
import com.taotao.cloud.health.base.FieldReport;
import java.lang.reflect.Field;
import javax.sql.DataSource;

/**
 * @author: chejiangyi
 * @version: 2019-08-02 09:42
 **/
public class DataSourceCollectTask extends AbstractCollectTask {

	@Override
	public int getTimeSpan() {
		return PropertyUtil.getPropertyCache("bsf.health.dataSource.timeSpan", 20);
	}

	@Override
	public String getDesc() {
		return "dataSource性能采集";
	}

	@Override
	public String getName() {
		return "dataSource.info";
	}

	@Override
	public boolean getEnabled() {
		return PropertyUtil.getPropertyCache("bsf.health.dataSource.enabled", true);
	}

	@Override
	protected Object getData() {
		DataSourceInfo info = new DataSourceInfo();
		String[] names = ContextUtil.getApplicationContext()
			.getBeanNamesForType(DataSource.class);
		int duridIndex = 0;
		for (String name : names) {
			DataSource dataSource = ContextUtil.getApplicationContext()
				.getBean(name, DataSource.class);
			Class druidCls = ReflectionUtil.tryClassForName(
				"com.alibaba.druid.pool.DruidDataSource");
			if (druidCls != null && druidCls.isAssignableFrom(dataSource.getClass())) {
				Field field = ReflectionUtil.findField(info.getClass(), "druid" + duridIndex++);
				if (field != null) {
					DruidDataSourceInfo druid = new DruidDataSourceInfo();
					druid.active = (Integer) ReflectionUtil.callMethod(dataSource, "getActiveCount",
						null);
					druid.connect = (Long) ReflectionUtil.callMethod(dataSource, "getConnectCount",
						null);
					druid.poolingCount = (Integer) ReflectionUtil.callMethod(dataSource,
						"getPoolingCount", null);
					druid.lockQueueLength = (Integer) ReflectionUtil.callMethod(dataSource,
						"getLockQueueLength", null);
					druid.waitThreadCount = (Integer) ReflectionUtil.callMethod(dataSource,
						"getWaitThreadCount", null);
					druid.initialSize = (Integer) ReflectionUtil.callMethod(dataSource,
						"getInitialSize", null);
					druid.maxActive = (Integer) ReflectionUtil.callMethod(dataSource,
						"getMaxActive", null);
					druid.minIdle = (Integer) ReflectionUtil.callMethod(dataSource, "getMinIdle",
						null);
					druid.connectErrorCount = (Long) ReflectionUtil.callMethod(dataSource,
						"getConnectErrorCount", null);
					druid.createTimeSpan = (Long) ReflectionUtil.callMethod(dataSource,
						"getCreateTimespanMillis", null);
					druid.closeCount = (Long) ReflectionUtil.callMethod(dataSource, "getCloseCount",
						null);
					druid.createCount = (Long) ReflectionUtil.callMethod(dataSource,
						"getCreateCount", null);
					druid.destroyCount = (Long) ReflectionUtil.callMethod(dataSource,
						"getDestroyCount", null);
					druid.isSharePreparedStatements = ReflectionUtil.callMethod(dataSource,
						"isSharePreparedStatements", null).toString();
					druid.isRemoveAbandoned = ReflectionUtil.callMethod(dataSource,
						"isRemoveAbandoned", null).toString();
					druid.removeAbandonedTimeout = (Integer) ReflectionUtil.callMethod(dataSource,
						"getRemoveAbandonedTimeout", null);
					druid.removeAbandonedCount = (Long) ReflectionUtil.callMethod(dataSource,
						"getRemoveAbandonedCount", null);
					druid.rollbackCount = (Long) ReflectionUtil.callMethod(dataSource,
						"getRollbackCount", null);
					druid.commitCount = (Long) ReflectionUtil.callMethod(dataSource,
						"getCommitCount", null);
					druid.startTransactionCount = (Long) ReflectionUtil.callMethod(dataSource,
						"getStartTransactionCount", null);
					field.setAccessible(true);
					ReflectionUtil.setFieldValue(field, info, druid);
				}
			}
		}

		return info;
	}


	private static class DataSourceInfo {

		@FieldReport(name = "bsf.druid0.info", desc = "druid0信息")
		private DruidDataSourceInfo druid0;
		@FieldReport(name = "bsf.druid1.info", desc = "druid1信息")
		private DruidDataSourceInfo druid1;
		@FieldReport(name = "bsf.druid2.info", desc = "druid2信息")
		private DruidDataSourceInfo druid2;
		@FieldReport(name = "bsf.druid3.info", desc = "druid3信息")
		private DruidDataSourceInfo druid3;
		@FieldReport(name = "bsf.druid4.info", desc = "druid4信息")
		private DruidDataSourceInfo druid4;
		@FieldReport(name = "bsf.druid5.info", desc = "druid5信息")
		private DruidDataSourceInfo druid5;
		@FieldReport(name = "bsf.druid6.info", desc = "druid6信息")
		private DruidDataSourceInfo druid6;
		@FieldReport(name = "bsf.druid7.info", desc = "druid7信息")
		private DruidDataSourceInfo druid7;
		@FieldReport(name = "bsf.druid8.info", desc = "druid8信息")
		private DruidDataSourceInfo druid8;
		@FieldReport(name = "bsf.druid9.info", desc = "druid9信息")
		private DruidDataSourceInfo druid9;

		public DataSourceInfo() {
		}

		public DataSourceInfo(
			DruidDataSourceInfo druid0,
			DruidDataSourceInfo druid1,
			DruidDataSourceInfo druid2,
			DruidDataSourceInfo druid3,
			DruidDataSourceInfo druid4,
			DruidDataSourceInfo druid5,
			DruidDataSourceInfo druid6,
			DruidDataSourceInfo druid7,
			DruidDataSourceInfo druid8,
			DruidDataSourceInfo druid9) {
			this.druid0 = druid0;
			this.druid1 = druid1;
			this.druid2 = druid2;
			this.druid3 = druid3;
			this.druid4 = druid4;
			this.druid5 = druid5;
			this.druid6 = druid6;
			this.druid7 = druid7;
			this.druid8 = druid8;
			this.druid9 = druid9;
		}

		public DruidDataSourceInfo getDruid0() {
			return druid0;
		}

		public void setDruid0(DruidDataSourceInfo druid0) {
			this.druid0 = druid0;
		}

		public DruidDataSourceInfo getDruid1() {
			return druid1;
		}

		public void setDruid1(DruidDataSourceInfo druid1) {
			this.druid1 = druid1;
		}

		public DruidDataSourceInfo getDruid2() {
			return druid2;
		}

		public void setDruid2(DruidDataSourceInfo druid2) {
			this.druid2 = druid2;
		}

		public DruidDataSourceInfo getDruid3() {
			return druid3;
		}

		public void setDruid3(DruidDataSourceInfo druid3) {
			this.druid3 = druid3;
		}

		public DruidDataSourceInfo getDruid4() {
			return druid4;
		}

		public void setDruid4(DruidDataSourceInfo druid4) {
			this.druid4 = druid4;
		}

		public DruidDataSourceInfo getDruid5() {
			return druid5;
		}

		public void setDruid5(DruidDataSourceInfo druid5) {
			this.druid5 = druid5;
		}

		public DruidDataSourceInfo getDruid6() {
			return druid6;
		}

		public void setDruid6(DruidDataSourceInfo druid6) {
			this.druid6 = druid6;
		}

		public DruidDataSourceInfo getDruid7() {
			return druid7;
		}

		public void setDruid7(DruidDataSourceInfo druid7) {
			this.druid7 = druid7;
		}

		public DruidDataSourceInfo getDruid8() {
			return druid8;
		}

		public void setDruid8(DruidDataSourceInfo druid8) {
			this.druid8 = druid8;
		}

		public DruidDataSourceInfo getDruid9() {
			return druid9;
		}

		public void setDruid9(DruidDataSourceInfo druid9) {
			this.druid9 = druid9;
		}
	}

	private static class DruidDataSourceInfo {

		@FieldReport(name = "dataSource.druid.pool.startTransaction.count", desc = "druid sql 开启事务次数")
		private Long startTransactionCount;
		@FieldReport(name = "dataSource.druid.pool.commit.count", desc = "druid sql commit次数")
		private Long commitCount;
		@FieldReport(name = "dataSource.druid.pool.rollback.count", desc = "druid sql回滚次数")
		private Long rollbackCount;
		@FieldReport(name = "dataSource.druid.pool.removeAbandoned.count", desc = "druid 连接超时回收次数")
		private Long removeAbandonedCount;
		@FieldReport(name = "dataSource.druid.pool.removeAbandoned.timeout", desc = "druid 连接超时回收周期（秒）")
		private Integer removeAbandonedTimeout;
		@FieldReport(name = "dataSource.druid.pool.isRemoveAbandoned", desc = "druid 是否开启连接超时回收")
		private String isRemoveAbandoned;
		@FieldReport(name = "dataSource.druid.pool.isSharePreparedStatements", desc = "druid preparedStatement是否缓存")
		private String isSharePreparedStatements;
		@FieldReport(name = "dataSource.druid.pool.destroy.count", desc = "druid销毁连接次数")
		private Long destroyCount;
		@FieldReport(name = "dataSource.druid.pool.create.count", desc = "druid创建连接次数")
		private Long createCount;
		@FieldReport(name = "dataSource.druid.pool.close.count", desc = "druid关闭连接次数")
		private Long closeCount;
		@FieldReport(name = "dataSource.druid.pool.create.timeSpan", desc = "druid物理连接创建耗时(毫秒)")
		private Long createTimeSpan;
		@FieldReport(name = "dataSource.druid.pool.connect.errorCount", desc = "druid物理连接错误数")
		private Long connectErrorCount;
		@FieldReport(name = "dataSource.druid.pool.idle.min", desc = "druid连接池最小值")
		private Integer minIdle;
		@FieldReport(name = "dataSource.druid.pool.active.max", desc = "druid连接池最大值")
		private Integer maxActive;
		@FieldReport(name = "dataSource.druid.pool.initial.size", desc = "druid连接池初始化长度")
		private Integer initialSize;
		@FieldReport(name = "dataSource.druid.pool.waitThread.count", desc = "druid获取连接时等待线程数")
		private Integer waitThreadCount;
		@FieldReport(name = "dataSource.druid.pool.lockQueue.length", desc = "druid获取连接等待队列长度")
		private Integer lockQueueLength;
		@FieldReport(name = "dataSource.druid.pool.active", desc = "druid正在打开的连接数")
		private Integer active;
		@FieldReport(name = "dataSource.druid.pool.connect", desc = "druid申请连接的次数")
		private Long connect;
		@FieldReport(name = "dataSource.druid.pool.poolingCount", desc = "druid连接池空闲连接数")
		private Integer poolingCount;

		public DruidDataSourceInfo() {
		}

		public DruidDataSourceInfo(Long startTransactionCount, Long commitCount,
			Long rollbackCount, Long removeAbandonedCount, Integer removeAbandonedTimeout,
			String isRemoveAbandoned, String isSharePreparedStatements, Long destroyCount,
			Long createCount, Long closeCount, Long createTimeSpan, Long connectErrorCount,
			Integer minIdle, Integer maxActive, Integer initialSize, Integer waitThreadCount,
			Integer lockQueueLength, Integer active, Long connect, Integer poolingCount) {
			this.startTransactionCount = startTransactionCount;
			this.commitCount = commitCount;
			this.rollbackCount = rollbackCount;
			this.removeAbandonedCount = removeAbandonedCount;
			this.removeAbandonedTimeout = removeAbandonedTimeout;
			this.isRemoveAbandoned = isRemoveAbandoned;
			this.isSharePreparedStatements = isSharePreparedStatements;
			this.destroyCount = destroyCount;
			this.createCount = createCount;
			this.closeCount = closeCount;
			this.createTimeSpan = createTimeSpan;
			this.connectErrorCount = connectErrorCount;
			this.minIdle = minIdle;
			this.maxActive = maxActive;
			this.initialSize = initialSize;
			this.waitThreadCount = waitThreadCount;
			this.lockQueueLength = lockQueueLength;
			this.active = active;
			this.connect = connect;
			this.poolingCount = poolingCount;
		}

		public Long getStartTransactionCount() {
			return startTransactionCount;
		}

		public void setStartTransactionCount(Long startTransactionCount) {
			this.startTransactionCount = startTransactionCount;
		}

		public Long getCommitCount() {
			return commitCount;
		}

		public void setCommitCount(Long commitCount) {
			this.commitCount = commitCount;
		}

		public Long getRollbackCount() {
			return rollbackCount;
		}

		public void setRollbackCount(Long rollbackCount) {
			this.rollbackCount = rollbackCount;
		}

		public Long getRemoveAbandonedCount() {
			return removeAbandonedCount;
		}

		public void setRemoveAbandonedCount(Long removeAbandonedCount) {
			this.removeAbandonedCount = removeAbandonedCount;
		}

		public Integer getRemoveAbandonedTimeout() {
			return removeAbandonedTimeout;
		}

		public void setRemoveAbandonedTimeout(Integer removeAbandonedTimeout) {
			this.removeAbandonedTimeout = removeAbandonedTimeout;
		}

		public String getIsRemoveAbandoned() {
			return isRemoveAbandoned;
		}

		public void setIsRemoveAbandoned(String isRemoveAbandoned) {
			this.isRemoveAbandoned = isRemoveAbandoned;
		}

		public String getIsSharePreparedStatements() {
			return isSharePreparedStatements;
		}

		public void setIsSharePreparedStatements(String isSharePreparedStatements) {
			this.isSharePreparedStatements = isSharePreparedStatements;
		}

		public Long getDestroyCount() {
			return destroyCount;
		}

		public void setDestroyCount(Long destroyCount) {
			this.destroyCount = destroyCount;
		}

		public Long getCreateCount() {
			return createCount;
		}

		public void setCreateCount(Long createCount) {
			this.createCount = createCount;
		}

		public Long getCloseCount() {
			return closeCount;
		}

		public void setCloseCount(Long closeCount) {
			this.closeCount = closeCount;
		}

		public Long getCreateTimeSpan() {
			return createTimeSpan;
		}

		public void setCreateTimeSpan(Long createTimeSpan) {
			this.createTimeSpan = createTimeSpan;
		}

		public Long getConnectErrorCount() {
			return connectErrorCount;
		}

		public void setConnectErrorCount(Long connectErrorCount) {
			this.connectErrorCount = connectErrorCount;
		}

		public Integer getMinIdle() {
			return minIdle;
		}

		public void setMinIdle(Integer minIdle) {
			this.minIdle = minIdle;
		}

		public Integer getMaxActive() {
			return maxActive;
		}

		public void setMaxActive(Integer maxActive) {
			this.maxActive = maxActive;
		}

		public Integer getInitialSize() {
			return initialSize;
		}

		public void setInitialSize(Integer initialSize) {
			this.initialSize = initialSize;
		}

		public Integer getWaitThreadCount() {
			return waitThreadCount;
		}

		public void setWaitThreadCount(Integer waitThreadCount) {
			this.waitThreadCount = waitThreadCount;
		}

		public Integer getLockQueueLength() {
			return lockQueueLength;
		}

		public void setLockQueueLength(Integer lockQueueLength) {
			this.lockQueueLength = lockQueueLength;
		}

		public Integer getActive() {
			return active;
		}

		public void setActive(Integer active) {
			this.active = active;
		}

		public Long getConnect() {
			return connect;
		}

		public void setConnect(Long connect) {
			this.connect = connect;
		}

		public Integer getPoolingCount() {
			return poolingCount;
		}

		public void setPoolingCount(Integer poolingCount) {
			this.poolingCount = poolingCount;
		}
	}
}
