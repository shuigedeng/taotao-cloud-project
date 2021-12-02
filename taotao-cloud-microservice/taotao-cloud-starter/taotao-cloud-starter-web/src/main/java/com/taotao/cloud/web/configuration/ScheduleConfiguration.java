/**
 * Copyright 2019 Yanzheng (https://github.com/micyo202). All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.taotao.cloud.web.configuration;

import com.taotao.cloud.common.utils.DateUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
import com.taotao.cloud.web.utils.SpringUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

/**
 * ScheduleConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:28:58
 */
@Configuration
@EnableScheduling
public class ScheduleConfiguration implements SchedulingConfigurer {

	@Value("${spring.application.name}")
	private String applicationName;

	@Autowired(required = false)
	private ScheduleMapper scheduleMapper;

	/**
	 * 定时任务执行数
	 */
	private int scheduleTaskCount = 0;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		if (Objects.nonNull(scheduleMapper)) {
			final List<Schedule> scheduleList = scheduleMapper.getScheduleListByAppName(
				applicationName);

			if (CollectionUtils.isNotEmpty(scheduleList)) {
				LogUtil.info("定时任务即将启动，预计启动任务数量[" + scheduleList.size() + "]，时间："
					+ DateUtil.getCurrentDateTime());

				for (Schedule schedule : scheduleList) {
					// 判断任务是否有效
					if (schedule.getValid()) {
						// 执行定时任务
						taskRegistrar.addTriggerTask(getRunnable(schedule), getTrigger(schedule));
						scheduleTaskCount++;
					}
				}
				LogUtil.info(
					"定时任务实际启动数量[" + scheduleTaskCount + "]，时间：" + DateUtil.getCurrentDateTime());
			}
		}
	}

	/**
	 * getRunnable
	 *
	 * @param schedule schedule
	 * @return {@link java.lang.Runnable }
	 * @author shuigedeng
	 * @since 2021-09-02 21:29:05
	 */
	private Runnable getRunnable(Schedule schedule) {
		return () -> {
			try {
				final Object bean = SpringUtil.getBean(schedule.getClassName());
				final Method method = bean.getClass()
					.getMethod(schedule.getMethod(), (Class<?>[]) null);
				method.invoke(bean);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				LogUtil.error("定时任务调度失败", e);
			}
		};
	}

	/**
	 * getTrigger
	 *
	 * @param schedule schedule
	 * @return {@link org.springframework.scheduling.Trigger }
	 * @author shuigedeng
	 * @since 2021-09-02 21:29:08
	 */
	private Trigger getTrigger(Schedule schedule) {
		return triggerContext -> {
			// 将Cron 0/1 * * * * ? 输入取得下一次执行的时间
			final CronTrigger cronTrigger = new CronTrigger(schedule.getCron());
			return cronTrigger.nextExecutionTime(triggerContext);
		};
	}


	/**
	 * Schedule 定时任务实体类
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 21:29:23
	 */
	public static class Schedule extends JpaSuperEntity<Integer> {

		/**
		 * 任务名称
		 */
		private String name;

		/**
		 * cron表达式
		 */
		private String cron;

		/**
		 * 执行应用名
		 */
		private String appName;

		/**
		 * 执行类
		 */
		private String className;

		/**
		 * 执行方法
		 */
		private String method;

		private Boolean valid;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCron() {
			return cron;
		}

		public void setCron(String cron) {
			this.cron = cron;
		}

		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public Boolean getValid() {
			return valid;
		}

		public void setValid(Boolean valid) {
			this.valid = valid;
		}
	}


	/**
	 * ScheduleMapper 定时任务Mapper方法
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2021/8/25 08:56
	 */
	public interface ScheduleMapper {

		/**
		 * 查询定时任务列表
		 *
		 * @param appName 应用名称
		 */
		@Select("select id, name, cron, app_name appName, class_name className, method, valid from sys_schedule where valid=1 and app_name=#{appName} order by id")
		List<Schedule> getScheduleListByAppName(String appName);
	}

}
