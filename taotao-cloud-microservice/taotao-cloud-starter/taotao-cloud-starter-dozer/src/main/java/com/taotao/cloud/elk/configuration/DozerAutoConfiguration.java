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
package com.taotao.cloud.elk.configuration;

import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.spring.DozerBeanMapperFactoryBean;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.elk.helper.DozerHelper;
import com.taotao.cloud.elk.properties.DozerProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * DozerConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @see <a>http://dozer.sourceforge.net/documentation/usage.html</a>
 * @see <a>http://www.jianshu.com/p/bf8f0e8aee23</a>
 * @since 2021-09-02 21:24:03
 */
@AutoConfiguration
@ConditionalOnMissingBean(Mapper.class)
@EnableConfigurationProperties({DozerProperties.class})
@ConditionalOnClass({DozerBeanMapperFactoryBean.class, Mapper.class})
@ConditionalOnProperty(prefix = DozerProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DozerAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(DozerAutoConfiguration.class, StarterName.WEB_STARTER);
	}

	@Bean
	public DozerHelper dozerHelper(Mapper mapper) {
		return new DozerHelper(mapper);
	}

	@Bean
	public DozerBeanMapperFactoryBean dozerMapper(DozerProperties properties) throws IOException {
		DozerBeanMapperFactoryBean factoryBean = new DozerBeanMapperFactoryBean();
		// 官方这样子写，没法用 匹配符！
		// factoryBean.setMappingFiles(properties.getMappingFiles());
		factoryBean.setMappingFiles(properties.resolveMapperLocations());
		return factoryBean;
	}


}
