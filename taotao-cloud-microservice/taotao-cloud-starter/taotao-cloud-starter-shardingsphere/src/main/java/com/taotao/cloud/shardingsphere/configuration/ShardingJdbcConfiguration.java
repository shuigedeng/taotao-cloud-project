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
package com.taotao.cloud.shardingsphere.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.shardingsphere.algorithm.DataSourceShardingAlgorithm;
import com.taotao.cloud.shardingsphere.properties.ShardingJdbcProperties;
import java.beans.ConstructorProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.apache.shardingsphere.shardingjdbc.spring.boot.common.SpringBootPropertiesConfigurationProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.encrypt.SpringBootEncryptRuleConfigurationProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.masterslave.SpringBootMasterSlaveRuleConfigurationProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.shadow.SpringBootShadowRuleConfigurationProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.sharding.SpringBootShardingRuleConfigurationProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ShardingJdbcConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/22 17:30
 */
@Configuration
@ConditionalOnProperty(prefix = ShardingJdbcProperties.PREIX, name = "enabled", havingValue = "true")
public class ShardingJdbcConfiguration extends SpringBootConfiguration implements
	ApplicationContextAware, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(ShardingJdbcConfiguration.class, StarterName.SHARDINGSPHERE_STARTER);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		super.setEnvironment(context.getEnvironment());
	}

	@ConstructorProperties({
		"shardingRule",
		"masterSlaveRule",
		"encryptRule",
		"shadowRule",
		"props"
	})
	public ShardingJdbcConfiguration(
		SpringBootShardingRuleConfigurationProperties shardingRule,
		SpringBootMasterSlaveRuleConfigurationProperties masterSlaveRule,
		SpringBootEncryptRuleConfigurationProperties encryptRule,
		SpringBootShadowRuleConfigurationProperties shadowRule,
		SpringBootPropertiesConfigurationProperties props) {
		super(shardingRule, masterSlaveRule, encryptRule, shadowRule, props);
	}

	@Bean
	public DataSourceShardingAlgorithm dataSourceShardingAlgorithm() {
		LogUtil.started(DataSourceShardingAlgorithm.class, StarterName.SHARDINGSPHERE_STARTER);

		return new DataSourceShardingAlgorithm();
	}

}
