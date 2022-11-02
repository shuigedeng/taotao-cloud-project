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
package com.taotao.cloud.job.quartz.configuration;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.job.quartz.event.DefaultQuartzEventListener;
import com.taotao.cloud.job.quartz.event.RedisQuartzEventListener;
import com.taotao.cloud.job.quartz.listener.QuartzListenerRegister;
import com.taotao.cloud.job.quartz.properties.QuartzProperties;
import com.taotao.cloud.job.quartz.utils.QuartzManager;
import org.jetbrains.annotations.NotNull;
import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 15:01:01
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = QuartzProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(value = {QuartzProperties.class})
public class QuartzJobAutoConfiguration implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ContextUtils.setApplicationContext((ConfigurableApplicationContext) applicationContext);
	}

	/**
	 * 解决Job中注入Spring Bean为null的问题
	 */
	@Component("quartzJobFactory")
	public static class QuartzJobFactory extends SpringBeanJobFactory {

		private final AutowireCapableBeanFactory capableBeanFactory;

		public QuartzJobFactory(AutowireCapableBeanFactory capableBeanFactory) {
			this.capableBeanFactory = capableBeanFactory;
		}

		@NotNull
		@Override
		protected Object createJobInstance(@NotNull TriggerFiredBundle bundle) throws Exception {
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
	public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		// scheduler.getListenerManager().addJobListener(new QuartzJobListener());

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

	@Bean
	public QuartzListenerRegister quartzListenerRegister() {
		return new QuartzListenerRegister();
	}

	@Bean
	@ConditionalOnBean(RedisRepository.class)
	public RedisQuartzEventListener redisQuartzLogEventListener() {
		return new RedisQuartzEventListener();
	}

	@Bean
	@ConditionalOnMissingBean
	public DefaultQuartzEventListener defaultQuartzLogEventListener() {
		return new DefaultQuartzEventListener();
	}

	///**
	// * 主要解决 @ConditionalOnSingleCandidate(DataSource.class)
	// * <p>
	// * 此处从 QuartzAutoConfiguration copy  如elastic改进之后可以删除此段代码
	// * <p>
	// * 集成了job-elastic模块之后 会产生2个datasource * dataSource * tracingDataSource
	// * <p>
	// * 主要解决 这个冲突的问题
	// */
	//@Configuration(proxyBeanMethods = false)
	//@ConditionalOnProperty(prefix = "spring.quartz", name = "job-store-type", havingValue = "jdbc")
	//@Import(DatabaseInitializationDependencyConfigurer.class)
	//protected static class JdbcStoreTypeConfiguration {
	//
	//	@Bean
	//	@Order(0)
	//	public SchedulerFactoryBeanCustomizer dataSourceCustomizer(
	//		org.springframework.boot.autoconfigure.quartz.QuartzProperties properties,
	//		@Qualifier("dataSource") DataSource dataSource,
	//		@QuartzDataSource ObjectProvider<DataSource> quartzDataSource,
	//		ObjectProvider<PlatformTransactionManager> transactionManager,
	//		@QuartzTransactionManager ObjectProvider<PlatformTransactionManager> quartzTransactionManager) {
	//		return (schedulerFactoryBean) -> {
	//			DataSource dataSourceToUse = getDataSource(dataSource, quartzDataSource);
	//			schedulerFactoryBean.setDataSource(dataSourceToUse);
	//			PlatformTransactionManager txManager = getTransactionManager(transactionManager,
	//				quartzTransactionManager);
	//			if (txManager != null) {
	//				schedulerFactoryBean.setTransactionManager(txManager);
	//			}
	//		};
	//	}
	//
	//	private DataSource getDataSource(DataSource dataSource,
	//		ObjectProvider<DataSource> quartzDataSource) {
	//		DataSource dataSourceIfAvailable = quartzDataSource.getIfAvailable();
	//		return (dataSourceIfAvailable != null) ? dataSourceIfAvailable : dataSource;
	//	}
	//
	//	private PlatformTransactionManager getTransactionManager(
	//		ObjectProvider<PlatformTransactionManager> transactionManager,
	//		ObjectProvider<PlatformTransactionManager> quartzTransactionManager) {
	//		PlatformTransactionManager transactionManagerIfAvailable = quartzTransactionManager.getIfAvailable();
	//		return (transactionManagerIfAvailable != null) ? transactionManagerIfAvailable
	//			: transactionManager.getIfUnique();
	//	}
	//
	//	@Bean
	//	@SuppressWarnings("deprecation")
	//	@ConditionalOnMissingBean({QuartzDataSourceScriptDatabaseInitializer.class,
	//		QuartzDataSourceInitializer.class})
	//	@Conditional(OnQuartzDatasourceInitializationCondition.class)
	//	public QuartzDataSourceScriptDatabaseInitializer quartzDataSourceScriptDatabaseInitializer(
	//		DataSource dataSource, @QuartzDataSource ObjectProvider<DataSource> quartzDataSource,
	//		org.springframework.boot.autoconfigure.quartz.QuartzProperties properties) {
	//		DataSource dataSourceToUse = getDataSource(dataSource, quartzDataSource);
	//		return new QuartzDataSourceScriptDatabaseInitializer(dataSourceToUse, properties);
	//	}
	//
	//	static class OnQuartzDatasourceInitializationCondition extends
	//		OnDatabaseInitializationCondition {
	//
	//		OnQuartzDatasourceInitializationCondition() {
	//			super("Quartz", "spring.quartz.jdbc.initialize-schema");
	//		}
	//
	//	}
	//
	//}
}
