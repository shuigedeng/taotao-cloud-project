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
package com.taotao.cloud.job.xxl.configuration;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.common.utils.RequestUtil;
import com.taotao.cloud.job.xxl.properties.XxlExecutorProperties;
import com.taotao.cloud.job.xxl.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * XxlJobConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/30 20:29
 */
@Configuration
@EnableConfigurationProperties({XxlJobProperties.class})
@ConditionalOnProperty(prefix = XxlJobProperties.PREFIX, name = "enabled", havingValue = "true")
public class XxlJobConfiguration implements InitializingBean {

	/**
	 * 服务名称 包含 XXL_JOB_ADMIN 则说明是 Admin
	 */
	private static final String TAO_TAO_CLOUD_XXL_JOB_ADMIN = "taotao-cloud-xxljob";

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(XxlJobConfiguration.class, StarterName.JOB_XXL_STARTER);
	}

	@Bean
	@ConditionalOnProperty(prefix = XxlJobProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
	public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties xxlJobProperties,
		Environment environment,
		DiscoveryClient discoveryClient) {

		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
		XxlExecutorProperties executor = xxlJobProperties.getExecutor();

		// 应用名默认为服务名
		String appName = executor.getAppname();
		if (!StringUtils.hasText(appName)) {
			appName = PropertyUtil.getProperty(CoreProperties.SpringApplicationName);
		}

		xxlJobSpringExecutor.setAppname(appName);
		xxlJobSpringExecutor.setAddress(executor.getAddress());
		xxlJobSpringExecutor.setIp(executor.getIp());

		if (StrUtil.isEmpty(executor.getIp())) {
			executor.setIp(RequestUtil.getLocalAddr());
		}

		xxlJobSpringExecutor.setPort(executor.getPort());
		xxlJobSpringExecutor.setAccessToken(executor.getAccessToken());
		xxlJobSpringExecutor.setLogPath(executor.getLogPath());
		xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());

		// 如果配置为空则获取注册中心的服务列表 "http://127.0.0.1:9080/taotao-cloud-xxljob"
		if (!StringUtils.hasText(xxlJobProperties.getAdmin().getAddresses())) {
			String serverList = discoveryClient.getServices().stream()
				.filter(s -> s.contains(TAO_TAO_CLOUD_XXL_JOB_ADMIN))
				.flatMap(s -> discoveryClient.getInstances(s).stream())
				.map(instance -> String.format("http://%s:%s", instance.getHost(),
					instance.getPort()))
				.collect(Collectors.joining(","));
			xxlJobSpringExecutor.setAdminAddresses(serverList);
		} else {
			xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
		}

		return xxlJobSpringExecutor;
	}

}
