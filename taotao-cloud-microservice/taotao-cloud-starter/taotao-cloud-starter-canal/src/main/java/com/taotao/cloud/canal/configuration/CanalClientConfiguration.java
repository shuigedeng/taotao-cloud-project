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
package com.taotao.cloud.canal.configuration;

import com.taotao.cloud.canal.model.SimpleCanalClient;
import com.taotao.cloud.canal.interfaces.CanalClient;
import com.taotao.cloud.canal.properties.CanalProperties;
import com.taotao.cloud.canal.runner.CanalApplicationRunner;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CanalClientConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/30 21:26
 */
@Configuration
public class CanalClientConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(CanalClientConfiguration.class, StarterName.CANAL_STARTER);
	}

	@Bean
	@ConditionalOnProperty(prefix = CanalProperties.PREFIX, name = "enabled", havingValue = "true")
	public CanalClient canalClient(CanalProperties properties) {
		LogUtil.started(CanalClient.class, StarterName.CANAL_STARTER);

		// 连接 canal 客户端
		// CanalClient canalClient = new SimpleCanalClient(canalConfig, MessageTransponders.defaultMessageTransponder());
		return new SimpleCanalClient(properties);
	}

	@Bean
	@ConditionalOnBean(CanalClient.class)
	public CanalApplicationRunner canalApplicationRunner(CanalClient canalClient) {
		return new CanalApplicationRunner(canalClient);
	}
}
