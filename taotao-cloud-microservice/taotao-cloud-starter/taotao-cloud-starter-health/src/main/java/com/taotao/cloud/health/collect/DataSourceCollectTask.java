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
import java.lang.reflect.Field;
import java.util.Arrays;
import javax.sql.DataSource;

/**
 * DataSourceCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:31:27
 */
public class DataSourceCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.health.collect.datasource";

	private CollectTaskProperties properties;

	public DataSourceCollectTask(CollectTaskProperties properties) {
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getDatasourcTimeSpan();
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
		return properties.isDatasourceEnabled();
	}

	@Override
	protected CollectInfo getData() {
		try {
			DataSourceInfo info = new DataSourceInfo();
			String[] names = ContextUtil.getApplicationContext()
				.getBeanNamesForType(DataSource.class);

			LogUtil.info("DataSourceCollectTask names : {}", Arrays.toString(names));

			int index = 0;
			for (String name : names) {
				DataSource dataSource = ContextUtil.getApplicationContext()
					.getBean(name, DataSource.class);

				Class druidCls = ReflectionUtil.tryClassForName(
					"com.alibaba.druid.pool.DruidDataSource");
				if (druidCls != null && druidCls.isAssignableFrom(dataSource.getClass())) {
					Field field = ReflectionUtil.findField(info.getClass(), "druid" + index++);
					if (field != null) {
						DruidDataSourceInfo druid = new DruidDataSourceInfo();
						druid.active = (Integer) ReflectionUtil.callMethod(dataSource,
							"getActiveCount",
							null);
						druid.connect = (Long) ReflectionUtil.callMethod(dataSource,
							"getConnectCount",
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
						druid.minIdle = (Integer) ReflectionUtil.callMethod(dataSource,
							"getMinIdle",
							null);
						druid.connectErrorCount = (Long) ReflectionUtil.callMethod(dataSource,
							"getConnectErrorCount", null);
						druid.createTimeSpan = (Long) ReflectionUtil.callMethod(dataSource,
							"getCreateTimespanMillis", null);
						druid.closeCount = (Long) ReflectionUtil.callMethod(dataSource,
							"getCloseCount",
							null);
						druid.createCount = (Long) ReflectionUtil.callMethod(dataSource,
							"getCreateCount", null);
						druid.destroyCount = (Long) ReflectionUtil.callMethod(dataSource,
							"getDestroyCount", null);
						druid.isSharePreparedStatements = ReflectionUtil.callMethod(dataSource,
							"isSharePreparedStatements", null).toString();
						druid.isRemoveAbandoned = ReflectionUtil.callMethod(dataSource,
							"isRemoveAbandoned", null).toString();
						druid.removeAbandonedTimeout = (Integer) ReflectionUtil.callMethod(
							dataSource,
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
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}


	private static class DataSourceInfo implements CollectInfo{

		@FieldReport(name = TASK_NAME + ".druid0.info", desc = "druid0信息")
		private DruidDataSourceInfo druid0;
		@FieldReport(name = TASK_NAME + ".druid1.info", desc = "druid1信息")
		private DruidDataSourceInfo druid1;
		@FieldReport(name = TASK_NAME + ".druid2.info", desc = "druid2信息")
		private DruidDataSourceInfo druid2;
		@FieldReport(name = TASK_NAME + ".druid3.info", desc = "druid3信息")
		private DruidDataSourceInfo druid3;
		@FieldReport(name = TASK_NAME + ".druid4.info", desc = "druid4信息")
		private DruidDataSourceInfo druid4;
		@FieldReport(name = TASK_NAME + ".druid5.info", desc = "druid5信息")
		private DruidDataSourceInfo druid5;
		@FieldReport(name = TASK_NAME + ".druid6.info", desc = "druid6信息")
		private DruidDataSourceInfo druid6;
		@FieldReport(name = TASK_NAME + ".druid7.info", desc = "druid7信息")
		private DruidDataSourceInfo druid7;
		@FieldReport(name = TASK_NAME + ".druid8.info", desc = "druid8信息")
		private DruidDataSourceInfo druid8;
		@FieldReport(name = TASK_NAME + ".druid9.info", desc = "druid9信息")
		private DruidDataSourceInfo druid9;
	}

	private static class DruidDataSourceInfo  implements CollectInfo{

		@FieldReport(name = TASK_NAME
			+ ".druid.pool.startTransaction.count", desc = "druid sql 开启事务次数")
		private Long startTransactionCount;
		@FieldReport(name = TASK_NAME + ".druid.pool.commit.count", desc = "druid sql commit次数")
		private Long commitCount;
		@FieldReport(name = TASK_NAME + ".druid.pool.rollback.count", desc = "druid sql回滚次数")
		private Long rollbackCount;
		@FieldReport(name = TASK_NAME
			+ ".druid.pool.removeAbandoned.count", desc = "druid 连接超时回收次数")
		private Long removeAbandonedCount;
		@FieldReport(name = TASK_NAME
			+ ".druid.pool.removeAbandoned.timeout", desc = "druid 连接超时回收周期（秒）")
		private Integer removeAbandonedTimeout;
		@FieldReport(name = TASK_NAME + ".druid.pool.isRemoveAbandoned", desc = "druid 是否开启连接超时回收")
		private String isRemoveAbandoned;
		@FieldReport(name = TASK_NAME
			+ ".druid.pool.isSharePreparedStatements", desc = "druid preparedStatement是否缓存")
		private String isSharePreparedStatements;
		@FieldReport(name = TASK_NAME + ".druid.pool.destroy.count", desc = "druid销毁连接次数")
		private Long destroyCount;
		@FieldReport(name = TASK_NAME + ".druid.pool.create.count", desc = "druid创建连接次数")
		private Long createCount;
		@FieldReport(name = TASK_NAME + ".druid.pool.close.count", desc = "druid关闭连接次数")
		private Long closeCount;
		@FieldReport(name = TASK_NAME + ".druid.pool.create.timeSpan", desc = "druid物理连接创建耗时(毫秒)")
		private Long createTimeSpan;
		@FieldReport(name = TASK_NAME + ".druid.pool.connect.errorCount", desc = "druid物理连接错误数")
		private Long connectErrorCount;
		@FieldReport(name = TASK_NAME + ".druid.pool.idle.min", desc = "druid连接池最小值")
		private Integer minIdle;
		@FieldReport(name = TASK_NAME + ".druid.pool.active.max", desc = "druid连接池最大值")
		private Integer maxActive;
		@FieldReport(name = TASK_NAME + ".druid.pool.initial.size", desc = "druid连接池初始化长度")
		private Integer initialSize;
		@FieldReport(name = TASK_NAME + ".druid.pool.waitThread.count", desc = "druid获取连接时等待线程数")
		private Integer waitThreadCount;
		@FieldReport(name = TASK_NAME + ".druid.pool.lockQueue.length", desc = "druid获取连接等待队列长度")
		private Integer lockQueueLength;
		@FieldReport(name = TASK_NAME + ".druid.pool.active", desc = "druid正在打开的连接数")
		private Integer active;
		@FieldReport(name = TASK_NAME + ".druid.pool.connect", desc = "druid申请连接的次数")
		private Long connect;
		@FieldReport(name = TASK_NAME + ".druid.pool.poolingCount", desc = "druid连接池空闲连接数")
		private Integer poolingCount;
	}
}
