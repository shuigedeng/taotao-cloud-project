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
package com.taotao.cloud.core.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.endpoint.RequestMappingEndPoint;
import com.taotao.cloud.core.endpoint.StandardEndPoint;
import com.taotao.cloud.core.endpoint.StandardHealthEndPoint;
import com.taotao.cloud.core.endpoint.StandardMbeanRegistrar;
import com.taotao.cloud.core.endpoint.indicator.StandardHealthIndicator;
import com.taotao.cloud.core.endpoint.mbean.StandardMBean;
import com.taotao.cloud.core.properties.EndpointProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.management.MalformedObjectNameException;

/**
 * EndPointConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/04/02 10:25
 */
@AutoConfiguration
@EnableConfigurationProperties({EndpointProperties.class})
@ConditionalOnProperty(prefix = EndpointProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class EndPointAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(EndPointAutoConfiguration.class, StarterName.CORE_STARTER);
	}

	@Bean
	public StandardHealthIndicator standardHealthIndicator() {
		return new StandardHealthIndicator();
	}

	@Bean
	public StandardHealthEndPoint standardHealthEndPoint() {
		return new StandardHealthEndPoint();
	}

	@Bean
	public StandardEndPoint standardEndPoint() {
		return new StandardEndPoint();
	}

	@Bean
	public RequestMappingEndPoint requestMappingEndPoint() {
		return new RequestMappingEndPoint();
	}

	@Bean
	public StandardMBean standardMBean() {
		return new StandardMBean();
	}

	@Bean
	public StandardMbeanRegistrar standardMbeanRegistrar() throws MalformedObjectNameException {
		return new StandardMbeanRegistrar();
	}
}
