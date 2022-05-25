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
package com.taotao.cloud.mongodb.helper.config;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.mongodb.client.result.UpdateResult;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.mongodb.helper.bean.IgnoreDocument;
import com.taotao.cloud.mongodb.helper.bean.InitValue;
import com.taotao.cloud.mongodb.helper.utils.SystemTool;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * 项目启动 表初始化
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@AutoConfiguration
public class MongoStartedEventListener {

	// 写链接(写到主库,可使用事务)
	@Autowired(required = false)
	private MongoTemplate mongoTemplate;

	@Autowired(required = false)
	private MongoMappingContext mongoMappingContext;

	@Async
	@Order(Ordered.LOWEST_PRECEDENCE - 1)
	@EventListener(WebServerInitializedEvent.class)
	public void afterStart(WebServerInitializedEvent event) {
		WebServerApplicationContext context = event.getApplicationContext();
		Environment environment = context.getEnvironment();

		Map<String, Object> beansWithAnnotation = ContextUtil.getApplicationContext()
			.getBeansWithAnnotation(SpringBootApplication.class);
		LogUtil.info(beansWithAnnotation.toString());

		if(Objects.nonNull(mongoMappingContext) && Objects.nonNull(mongoTemplate)){
			Object cla = beansWithAnnotation.values().stream().findFirst().get();
			// 找到主程序包
			Set<Class<?>> set = ClassUtil.scanPackage(ClassUtils.getPackageName(cla.getClass()));
			for (Class<?> clazz : set) {
				IgnoreDocument ignoreDocument = clazz.getAnnotation(IgnoreDocument.class);
				if (ignoreDocument != null) {
					continue;
				}

				Document document = clazz.getAnnotation(Document.class);
				if (document == null) {
					continue;
				}

				// 创建表
				if (!mongoTemplate.collectionExists(clazz)) {
					mongoTemplate.createCollection(clazz);
					LogUtil.info("创建了" + clazz.getSimpleName() + "表");
				}

				// 创建索引
				IndexOperations indexOps = mongoTemplate.indexOps(clazz);
				IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
				resolver.resolveIndexFor(clazz).forEach(indexOps::ensureIndex);

				Field[] fields = ReflectUtil.getFields(clazz);
				for (Field field : fields) {
					// 获取注解
					if (field.isAnnotationPresent(InitValue.class)) {
						InitValue initValue = field.getAnnotation(InitValue.class);
						if (initValue.value() != null) {

							// 更新表默认值
							Query query = new Query();
							query.addCriteria(Criteria.where(field.getName()).is(null));

							long count = mongoTemplate.count(query, clazz);
							if (count > 0) {
								Object value = null;
								Class<?> type = field.getType();

								if (type.equals(String.class)) {
									value = initValue.value();
								}
								if (type.equals(Short.class)) {
									value = Short.parseShort(initValue.value());
								}
								if (type.equals(Integer.class)) {
									value = Integer.parseInt(initValue.value());
								}
								if (type.equals(Long.class)) {
									value =  Long.parseLong(initValue.value());
								}
								if (type.equals(Float.class)) {
									value =  Float.parseFloat(initValue.value());
								}
								if (type.equals(Double.class)) {
									value = Double.parseDouble(initValue.value());
								}
								if (type.equals(Boolean.class)) {
									value =  Boolean.parseBoolean(initValue.value());
								}

								Update update = new Update().set(field.getName(), value);
								UpdateResult updateResult = mongoTemplate.updateMulti(query, update, clazz);

								LogUtil.info(clazz.getSimpleName() + "表更新了" + updateResult.getModifiedCount() + "条默认值");
							}
						}
					}
				}
			}
		}
	}
}
