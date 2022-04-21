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
package com.taotao.cloud.mongodb.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.mongodb.converter.DBObjectToJsonNodeConverter;
import com.taotao.cloud.mongodb.converter.JsonNodeToDocumentConverter;
import com.taotao.cloud.mongodb.converter.LocalDateTimeToString;
import com.taotao.cloud.mongodb.converter.LocalDateToString;
import com.taotao.cloud.mongodb.converter.StringToLocalDate;
import com.taotao.cloud.mongodb.converter.StringToLocalDateTime;
import com.taotao.cloud.mongodb.helper.config.MongoStartedEventListener;
import com.taotao.cloud.mongodb.helper.utils.ImportExportUtil;
import com.taotao.cloud.mongodb.helper.utils.MongoHelper;
import com.taotao.cloud.mongodb.properties.MongodbProperties;
import com.taotao.cloud.mongodb.service.BaseMongoDAO;
import com.taotao.cloud.mongodb.service.MongoDaoSupport;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * es配置类
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/3 06:47
 */
@Configuration
@EnableConfigurationProperties({MongodbProperties.class})
@ConditionalOnProperty(prefix = MongodbProperties.PREFIX, name = "enabled", havingValue = "true")
public class MongodbAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(MongodbAutoConfiguration.class, StarterName.MONGODB_STARTER);
	}

	@Primary
	@Bean
	public MongoCustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<>(2);
		converters.add(DBObjectToJsonNodeConverter.INSTANCE);
		converters.add(JsonNodeToDocumentConverter.INSTANCE);
		converters.add(new LocalDateTimeToString());
		converters.add(new LocalDateToString());
		converters.add(new StringToLocalDateTime());
		converters.add(new StringToLocalDate());
		return new MongoCustomConversions(converters);
	}

	@Bean
	@ConditionalOnBean(MongoTemplate.class)
	public BaseMongoDAO baseMongoDAO(MongoTemplate mongoTemplate) {
		return new MongoDaoSupport(mongoTemplate);
	}

	@Configuration
	public static class MongodbHelperAutoConfiguration {

		// 开启事务(如使用单机mongodb,可不配置此@Bean)
		//@Bean
		//public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
		//	return new MongoTransactionManager(dbFactory);
		//}

		@Autowired
		private MongoDatabaseFactory mongoDatabaseFactory;

		@Autowired
		private MongoMappingContext mongoMappingContext;

		@Bean
		public MappingMongoConverter mappingMongoConverter() {
			DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
			MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver,
				mongoMappingContext);
			// 此处是去除插入数据库的 _class 字段
			converter.setTypeMapper(new DefaultMongoTypeMapper(null));
			return converter;
		}

		@Bean
		public MongoHelper mongoHelper() {
			return new MongoHelper();
		}

		@Bean
		public ImportExportUtil importExportUtil() {
			return new ImportExportUtil();
		}

		@Bean
		public MongoStartedEventListener mongoStartedEventListener() {
			return new MongoStartedEventListener();
		}

	}

}
