/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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


import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.reflect.ReflectionUtil;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.model.Collector.Hook;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.exception.HealthException;
import com.taotao.cloud.health.model.CollectInfo;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.Objects;

/**
 * JedisCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-13 22:09:56
 */
public class JedisCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.health.collect.jedis";

	private CollectTaskProperties properties;
	private Collector collector;

	public JedisCollectTask(Collector collector, CollectTaskProperties properties) {
		this.collector = collector;
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getJedisTimeSpan();
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
		return properties.isJedisEnabled();
	}

	@Override
	protected CollectInfo getData() {
		try {
			Object item = ContextUtil.getBean(
				ReflectionUtil.classForName("com.yh.csx.bsf.redis.impl.RedisClusterMonitor"),
				false);
			if (Objects.nonNull(item)) {
				ReflectionUtil.callMethod(item, "collect", null);

				JedisInfo info = new JedisInfo();
				String name = "jedis.cluster";
				info.detail = (String) this.collector.value(name + ".pool.detail").get();
				info.wait = (Integer) this.collector.value(name + ".pool.wait").get();
				info.active = (Integer) this.collector.value(name + ".pool.active").get();
				info.idle = (Integer) this.collector.value(name + ".pool.idle").get();
				info.lockInfo = (String) this.collector.value(name + ".lock.error.detail").get();

				Hook hook = this.collector.hook(name + ".hook");
				if (hook != null) {
					info.hookCurrent = hook.getCurrent();
					info.hookError = hook.getLastErrorPerSecond();
					info.hookSuccess = hook.getLastSuccessPerSecond();
					info.hookList = hook.getMaxTimeSpanList().toText();
					info.hookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
				}
				return info;
			}
		} catch (Exception exp) {
			throw new HealthException(exp);
		}
		return null;
	}

	private static class JedisInfo implements CollectInfo{

		@FieldReport(name = TASK_NAME + ".cluster.pool.wait", desc = "jedis集群排队等待的请求数")
		private Integer wait = 0;
		@FieldReport(name = TASK_NAME + ".cluster.pool.active", desc = "jedis集群活动使用的请求数")
		private Integer active = 0;
		@FieldReport(name = TASK_NAME + ".cluster.pool.idle", desc = "jedis集群空闲的请求数")
		private Integer idle = 0;
		@FieldReport(name = TASK_NAME + ".cluster.pool.detail", desc = "jedis集群连接池详情")
		private String detail =" = 0L";
		@FieldReport(name = TASK_NAME + ".cluster.hook.error", desc = "jedis集群拦截上一次每秒出错次数")
		private Long hookError  = 0L;
		@FieldReport(name = TASK_NAME + ".cluster.hook.success", desc = "jedis集群拦截上一次每秒成功次数")
		private Long hookSuccess = 0L;
		@FieldReport(name = TASK_NAME + ".cluster.hook.current", desc = "jedis集群拦截当前执行任务数")
		private Long hookCurrent = 0L;
		@FieldReport(name = TASK_NAME + ".cluster.hook.list.detail", desc = "jedis集群拦截历史最大耗时任务列表")
		private String hookList = "";
		@FieldReport(name = TASK_NAME + ".cluster.hook.list.minute.detail", desc = "jedis集群拦截历史最大耗时任务列表(每分钟)")
		private String hookListPerMinute = "";
		@FieldReport(name = TASK_NAME + ".cluster.lock.error.detail", desc = "jedis集群分布式锁异常信息")
		private String lockInfo = "";
	}
}
