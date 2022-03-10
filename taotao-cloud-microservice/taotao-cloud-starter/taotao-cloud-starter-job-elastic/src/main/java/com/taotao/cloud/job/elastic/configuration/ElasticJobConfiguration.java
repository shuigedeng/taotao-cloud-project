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
package com.taotao.cloud.job.elastic.configuration;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.job.elastic.model.JobController;
import com.taotao.cloud.job.elastic.model.JobService;
import com.taotao.cloud.job.elastic.parser.JobConfParser;
import com.taotao.cloud.job.elastic.properties.ElasticJobProperties;
import com.taotao.cloud.job.elastic.properties.ZookeeperProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JobParserConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/30 20:41
 */
@Configuration
@EnableConfigurationProperties({ZookeeperProperties.class, ElasticJobProperties.class})
@ConditionalOnProperty(prefix = ElasticJobProperties.PREFIX, name = "enabled", havingValue = "true")
public class ElasticJobConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(ElasticJobConfiguration.class, StarterName.JOB_ELASTIC_STARTER);
	}

	@Bean(initMethod = "init")
	@ConditionalOnProperty(prefix = ZookeeperProperties.PREFIX, name = "serverList")
	public ZookeeperRegistryCenter registryCenter(ZookeeperProperties properties) {

		ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(
			properties.getServerList(),
			properties.getNamespace()
		);

		zkConfig.setBaseSleepTimeMilliseconds(properties.getBaseSleepTimeMilliseconds());
		zkConfig.setConnectionTimeoutMilliseconds(
			properties.getConnectionTimeoutMilliseconds());
		zkConfig.setDigest(properties.getDigest());
		zkConfig.setMaxRetries(properties.getMaxRetries());
		zkConfig.setMaxSleepTimeMilliseconds(properties.getMaxSleepTimeMilliseconds());
		zkConfig.setSessionTimeoutMilliseconds(properties.getSessionTimeoutMilliseconds());
		return new ZookeeperRegistryCenter(zkConfig);
	}

	@Bean
	public JobService jobService(ZookeeperRegistryCenter zookeeperRegistryCenter) {
		return new JobService(zookeeperRegistryCenter);
	}

	@Bean
	public JobController jobController(JobService jobService) {
		return new JobController(jobService);
	}

	@Bean
	@ConditionalOnBean(JobService.class)
	public JobConfParser jobConfParser(ZookeeperRegistryCenter zookeeperRegistryCenter,
		JobService jobService) {
		return new JobConfParser(zookeeperRegistryCenter, jobService);
	}

}
