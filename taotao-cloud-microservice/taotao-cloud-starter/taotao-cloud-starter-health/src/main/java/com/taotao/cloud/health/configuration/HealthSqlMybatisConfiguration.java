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
package com.taotao.cloud.health.configuration;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.health.interceptor.SqlMybatisInterceptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HealthSqlMybatisConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:22:35
 */
@Configuration
public class HealthSqlMybatisConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(HealthSqlMybatisConfiguration.class, StarterNameConstant.HEALTH_STARTER);
	}

	@Bean
	@ConditionalOnClass(name = "org.apache.ibatis.plugin.Interceptor")
	public SqlMybatisInterceptor sqlMybatisInterceptor(Collector collector) {
		LogUtil.started(SqlMybatisInterceptor.class, StarterNameConstant.HEALTH_STARTER);
		return new SqlMybatisInterceptor(collector);
	}

	//@Bean
	//@ConditionalOnClass(name = "org.hibernate.Interceptor")
	//public HibernateInterceptor hibernateInterceptor() {
	//	LogUtil.started(HibernateInterceptor.class, StarterNameConstant.HEALTH_STARTER);
	//	return new HibernateInterceptor();
	//}


}
