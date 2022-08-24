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
package com.taotao.cloud.idempotent.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.idempotent.aop.IdempotentAspect;
import com.taotao.cloud.idempotent.properties.IdempotentProperties;
import com.taotao.cloud.lock.support.DistributedLock;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * IdempotentConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:13:17
 */
@AutoConfiguration
@EnableConfigurationProperties({IdempotentProperties.class})
@ConditionalOnProperty(prefix = IdempotentProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class IdempotentAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(IdempotentAutoConfiguration.class, StarterName.WEB_STARTER);
	}

	@Bean
	@ConditionalOnBean({DistributedLock.class})
	public IdempotentAspect idempotentAspect(DistributedLock distributedLock) {
		return new IdempotentAspect(distributedLock);
	}
}
