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
package com.taotao.cloud.job.elastic.model;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties.JobPropertiesEnum;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.List;
import java.util.Objects;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

public class JobService implements ApplicationContextAware {

	private ZookeeperRegistryCenter zookeeperRegistryCenter;

	private ApplicationContext ctx;

	public JobService(ZookeeperRegistryCenter zookeeperRegistryCenter) {
		this.zookeeperRegistryCenter = zookeeperRegistryCenter;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}

	public void addJob(Job job) {
		// 核心配置
		JobCoreConfiguration coreConfig =
			JobCoreConfiguration.newBuilder(job.getJobName(), job.getCron(),
					job.getShardingTotalCount())
				.shardingItemParameters(job.getShardingItemParameters())
				.description(job.getDescription())
				.failover(job.isFailover())
				.jobParameter(job.getJobParameter())
				.misfire(job.isMisfire())
				.jobProperties(JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(),
					job.getJobProperties().getJobExceptionHandler())
				.jobProperties(JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(),
					job.getJobProperties().getExecutorServiceHandler())
				.build();

		// 不同类型的任务配置处理
		LiteJobConfiguration jobConfig = null;
		JobTypeConfiguration typeConfig = null;
		String jobType = job.getJobType();
		if (jobType.equals("SIMPLE")) {
			typeConfig = new SimpleJobConfiguration(coreConfig, job.getJobClass());
		}

		if (jobType.equals("DATAFLOW")) {
			typeConfig = new DataflowJobConfiguration(coreConfig, job.getJobClass(),
				job.isStreamingProcess());
		}

		if (jobType.equals("SCRIPT")) {
			typeConfig = new ScriptJobConfiguration(coreConfig, job.getScriptCommandLine());
		}

		jobConfig = LiteJobConfiguration.newBuilder(typeConfig)
			.overwrite(job.isOverwrite())
			.disabled(job.isDisabled())
			.monitorPort(job.getMonitorPort())
			.monitorExecution(job.isMonitorExecution())
			.maxTimeDiffSeconds(job.getMaxTimeDiffSeconds())
			.jobShardingStrategyClass(job.getJobShardingStrategyClass())
			.reconcileIntervalMinutes(job.getReconcileIntervalMinutes())
			.build();

		List<BeanDefinition> elasticJobListeners = getTargetElasticJobListeners(job);

		// 构建SpringJobScheduler对象来初始化任务
		BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(
			SpringJobScheduler.class);
		factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		if ("SCRIPT".equals(jobType)) {
			factory.addConstructorArgValue(null);
		} else {
			BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(
				job.getJobClass());
			factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
		}
		factory.addConstructorArgValue(zookeeperRegistryCenter);
		factory.addConstructorArgValue(jobConfig);

		// 任务执行日志数据源，以名称获取
		if (StringUtils.hasText(job.getEventTraceRdbDataSource())) {
			BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(
				JobEventRdbConfiguration.class);
			rdbFactory.addConstructorArgReference(job.getEventTraceRdbDataSource());
			factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
		}

		factory.addConstructorArgValue(elasticJobListeners);
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
		defaultListableBeanFactory.registerBeanDefinition("SpringJobScheduler" + job.getJobName(),
			factory.getBeanDefinition());
		SpringJobScheduler springJobScheduler = (SpringJobScheduler) ctx.getBean(
			"SpringJobScheduler" + job.getJobName());
		springJobScheduler.init();
		LogUtil.info("[" + job.getJobName() + "]\t" + job.getJobClass() + "\tinit success");
	}

	private List<BeanDefinition> getTargetElasticJobListeners(Job job) {
		List<BeanDefinition> result = new ManagedList<BeanDefinition>(2);
		String listeners = job.getListener();
		if (StringUtils.hasText(listeners)) {
			BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listeners);
			factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			result.add(factory.getBeanDefinition());
		}

		String distributedListeners = job.getDistributedListener();
		long startedTimeoutMilliseconds = job.getStartedTimeoutMilliseconds();
		long completedTimeoutMilliseconds = job.getCompletedTimeoutMilliseconds();

		if (StringUtils.hasText(distributedListeners)) {
			BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(
				distributedListeners);
			factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			factory.addConstructorArgValue(startedTimeoutMilliseconds);
			factory.addConstructorArgValue(completedTimeoutMilliseconds);
			result.add(factory.getBeanDefinition());
		}
		return result;
	}


	public void removeJob(String jobName) throws Exception {
		CuratorFramework client = zookeeperRegistryCenter.getClient();
		client.delete().deletingChildrenIfNeeded().forPath("/" + jobName);
	}

	/**
	 * 开启任务监听,当有任务添加时，监听zk中的数据增加，自动在其他节点也初始化该任务
	 */
	public void monitorJobRegister() {
		CuratorFramework client = zookeeperRegistryCenter.getClient();
		PathChildrenCache childrenCache = new PathChildrenCache(client, "/", true);
		PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				ChildData data = event.getData();
				switch (event.getType()) {
					case CHILD_ADDED:
						String config = new String(client.getData().forPath(data.getPath() + "/config"));
						Job job = JsonUtil.toObject(config, Job.class);
						Object bean = null;
						// 获取bean失败则添加任务
						try {
							bean = ctx.getBean("SpringJobScheduler" + job.getJobName());
						} catch (BeansException e) {
							LogUtil.error(
								"ERROR NO BEAN,CREATE BEAN SpringJobScheduler" + job.getJobName());
						}
						if (Objects.isNull(bean)) {
							addJob(job);
						}
						break;
					default:
						break;
				}
			}
		};

		childrenCache.getListenable().addListener(childrenCacheListener);
		try {
			childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
			//childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
