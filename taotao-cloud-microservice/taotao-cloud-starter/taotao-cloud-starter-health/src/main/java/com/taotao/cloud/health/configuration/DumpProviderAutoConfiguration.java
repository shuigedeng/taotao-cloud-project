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
package com.taotao.cloud.health.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.health.dump.DumpFilter;
import com.taotao.cloud.health.dump.DumpProvider;
import com.taotao.cloud.health.properties.DumpProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * HealthConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:22:15
 */
@AutoConfiguration
@EnableConfigurationProperties(DumpProperties.class)
@ConditionalOnProperty(prefix = DumpProperties.PREFIX, name = "enabled", havingValue = "true")
public class DumpProviderAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(DumpProviderAutoConfiguration.class, StarterName.HEALTH_STARTER);
	}

	@Bean
	public DumpProvider dumpProvider() {
		return new DumpProvider();
	}

	@Bean
	@ConditionalOnWebApplication(type = Type.SERVLET)
	public FilterRegistrationBean<DumpFilter> dumpFilter() {
		FilterRegistrationBean<DumpFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		filterRegistrationBean.setFilter(new DumpFilter());
		filterRegistrationBean.setName(DumpFilter.class.getName());
		filterRegistrationBean.addUrlPatterns("/health/dump/*");
		return filterRegistrationBean;
	}

}
