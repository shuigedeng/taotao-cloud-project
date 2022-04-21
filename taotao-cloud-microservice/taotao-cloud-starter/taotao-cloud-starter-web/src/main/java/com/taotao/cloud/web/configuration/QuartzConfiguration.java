/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.web.configuration;

import com.taotao.cloud.core.configuration.MonitorAutoConfiguration.MonitorThreadPoolExecutor;
import com.taotao.cloud.core.configuration.MonitorAutoConfiguration.MonitorThreadPoolFactory;
import com.taotao.cloud.web.quartz.QuartzJobListener;
import com.taotao.cloud.web.quartz.QuartzManager;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * 定时任务配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 15:01:01
 */
@Configuration
public class QuartzConfiguration {

	/**
	 * 该处仅供参考
	 */
	public final static ThreadPoolExecutor EXECUTOR;

	static {
		MonitorThreadPoolExecutor executor = new MonitorThreadPoolExecutor(
			10,
			50,
			60,
			TimeUnit.SECONDS,
			new SynchronousQueue<>(),
			new MonitorThreadPoolFactory("taotao-cloud-quartz-executor"));

		executor.setNamePrefix("taotao-cloud-quartz-executor");
		EXECUTOR = executor;
	}

	/**
	 * 解决Job中注入Spring Bean为null的问题
	 */
	@Component("quartzJobFactory")
	public static class QuartzJobFactory extends AdaptableJobFactory {

		private final AutowireCapableBeanFactory capableBeanFactory;

		public QuartzJobFactory(AutowireCapableBeanFactory capableBeanFactory) {
			this.capableBeanFactory = capableBeanFactory;
		}

		@Override
		protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
			//调用父类的方法
			Object jobInstance = super.createJobInstance(bundle);
			capableBeanFactory.autowireBean(jobInstance);
			return jobInstance;
		}
	}

	/**
	 * 注入scheduler到spring
	 */
	@Bean(name = "scheduler")
	public Scheduler scheduler(QuartzJobFactory quartzJobFactory) throws Exception {
		SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
		factoryBean.setJobFactory(quartzJobFactory);
		factoryBean.afterPropertiesSet();

		Scheduler scheduler = factoryBean.getScheduler();
		scheduler.getListenerManager().addJobListener(new QuartzJobListener());

		// 添加JobListener, 精确匹配JobKey
		// KeyMatcher<JobKey> keyMatcher = KeyMatcher.keyEquals(JobKey.jobKey("helloJob", "group1"));
		// scheduler.getListenerManager().addJobListener(new HelloJobListener(), keyMatcher);

		scheduler.start();
		return scheduler;
	}

	@Bean
	public QuartzManager quartzManage() {
		return new QuartzManager();
	}
}
