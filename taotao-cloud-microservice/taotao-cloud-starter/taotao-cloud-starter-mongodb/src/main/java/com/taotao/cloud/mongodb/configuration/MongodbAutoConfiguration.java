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
package com.taotao.cloud.mongodb.configuration;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.mongodb.converter.DBObjectToJsonNodeConverter;
import com.taotao.cloud.mongodb.converter.JsonNodeToDocumentConverter;
import com.taotao.cloud.mongodb.properties.MongodbProperties;
import com.taotao.cloud.mongodb.service.BaseMongoDAO;
import com.taotao.cloud.mongodb.service.MongoDaoSupport;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

/**
 * es配置类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/3 06:47
 */
@Configuration
@EnableConfigurationProperties({MongodbProperties.class})
@ConditionalOnProperty(prefix = MongodbProperties.PREFIX, name = "enabled", havingValue = "true")
public class MongodbAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(MongodbAutoConfiguration.class, StarterNameConstant.MONGODB_STARTER);
	}

	@Primary
	@Bean
	public MongoCustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<>(2);
		converters.add(DBObjectToJsonNodeConverter.INSTANCE);
		converters.add(JsonNodeToDocumentConverter.INSTANCE);
		return new MongoCustomConversions(converters);
	}

	@Bean
	public BaseMongoDAO baseMongoDAO() {
		return new MongoDaoSupport();
	}

}
