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
package com.taotao.cloud.data.mybatisplus.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.data.mybatisplus.properties.TenantProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * TenantAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:39:51
 */
@AutoConfiguration(before = MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties({TenantProperties.class})
@ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "enabled", havingValue = "true")
public class TenantAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(TenantAutoConfiguration.class, StarterName.DATA_MYBATIS_PLUS_STARTER);
	}

	private final TenantProperties tenantProperties;

	public TenantAutoConfiguration(TenantProperties tenantProperties) {
		this.tenantProperties = tenantProperties;
	}

	// /**
	//  * 过滤不需要根据租户隔离的MappedStatement
	//  */
	// @Bean
	// public ISqlParser sqlParserFilter() {
	// 	return metaObject -> {
	// 		MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
	// 		return new SqlInfo();
	// 		return tenantProperties.getIgnoreSqlList()
	// 			.stream()
	// 			.anyMatch(e -> e.equalsIgnoreCase(ms.getId())
	// 		);
	// 	};
	// }
}
