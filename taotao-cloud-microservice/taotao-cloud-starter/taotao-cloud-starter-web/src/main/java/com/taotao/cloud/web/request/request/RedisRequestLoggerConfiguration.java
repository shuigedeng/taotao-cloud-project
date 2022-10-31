package com.taotao.cloud.web.request.request;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.logger.enums.RequestLoggerTypeEnum;
import com.taotao.cloud.web.request.annotation.ConditionalOnRequestLogger;
import com.taotao.cloud.web.request.properties.RequestLoggerProperties;
import com.taotao.cloud.web.request.service.IRequestLoggerService;
import com.taotao.cloud.web.request.service.impl.RedisRequestLoggerServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:21
 */
@AutoConfiguration
@ConditionalOnClass(RedisRepository.class)
@ConditionalOnBean(RedisRepository.class)
@ConditionalOnProperty(prefix = RequestLoggerProperties.PREFIX, name = "enabled", havingValue = "true")
public class RedisRequestLoggerConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(RedisRequestLoggerConfiguration.class, StarterName.LOG_STARTER);
	}

	@Bean
	@ConditionalOnRequestLogger(logType = RequestLoggerTypeEnum.REDIS)
	public IRequestLoggerService redisRequestLoggerService(RedisRepository redisRepository) {
		return new RedisRequestLoggerServiceImpl(redisRepository);
	}

}
