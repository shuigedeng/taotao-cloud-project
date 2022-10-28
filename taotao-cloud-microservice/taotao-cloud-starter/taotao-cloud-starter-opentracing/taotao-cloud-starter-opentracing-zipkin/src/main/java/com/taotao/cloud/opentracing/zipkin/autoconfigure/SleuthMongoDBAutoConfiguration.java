package com.taotao.cloud.opentracing.zipkin.autoconfigure;

import brave.Tracing;
import brave.mongodb.MongoDBTracing;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.event.CommandListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Sleuth MonogoDB 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(MongoAutoConfiguration.class)
@ConditionalOnClass(MongoClient.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SleuthMongoDBAutoConfiguration implements InitializingBean {

	public static final String INITIALIZING_MONGO_CLIENT_SETTINGS = "Initializing MongoClientSettings";
	private final MongoClientSettings settings;

	@Override
	public void afterPropertiesSet() {
		log.debug(INITIALIZING_MONGO_CLIENT_SETTINGS);
		CommandListener listener = MongoDBTracing.create(Tracing.current()).commandListener();
		settings.getCommandListeners().add(listener);
	}
}
