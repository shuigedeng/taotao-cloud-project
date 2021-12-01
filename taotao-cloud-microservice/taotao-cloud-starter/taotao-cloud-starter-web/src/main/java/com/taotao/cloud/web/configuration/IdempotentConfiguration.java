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
package com.taotao.cloud.web.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.lock.DistributedLock;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.web.idempotent.IdempotentAspect;
import com.taotao.cloud.web.properties.IdempotentProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IdempotentConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:13:17
 */
@Configuration
@EnableConfigurationProperties({IdempotentProperties.class})
@ConditionalOnProperty(prefix = IdempotentProperties.PREFIX, name = "enabled", havingValue = "true")
public class IdempotentConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(IdempotentConfiguration.class, StarterName.WEB_STARTER);
	}

	@Bean
	@ConditionalOnBean({DistributedLock.class})
	public IdempotentAspect idempotentAspect(DistributedLock distributedLock) {
		return new IdempotentAspect(distributedLock);
	}
}
