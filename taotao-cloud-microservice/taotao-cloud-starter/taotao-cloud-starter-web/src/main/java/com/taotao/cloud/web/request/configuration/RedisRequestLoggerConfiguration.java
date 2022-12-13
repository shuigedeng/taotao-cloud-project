package com.taotao.cloud.web.request.configuration;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.logger.enums.RequestLoggerTypeEnum;
import com.taotao.cloud.web.request.properties.RequestLoggerProperties;
import com.taotao.cloud.web.request.service.IRequestLoggerService;
import com.taotao.cloud.web.request.service.impl.RedisRequestLoggerServiceImpl;
import java.util.Arrays;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:21
 */
@AutoConfiguration
@ConditionalOnClass(RedisRepository.class)
@ConditionalOnBean(RedisRepository.class)
public class RedisRequestLoggerConfiguration implements InitializingBean {

	@Autowired
	private RequestLoggerProperties requestLoggerProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(RedisRequestLoggerConfiguration.class, StarterName.CACHE_REDIS_STARTER);
	}

	@Bean
	public IRequestLoggerService redisRequestLoggerService(RedisRepository redisRepository) {
		if (Arrays.stream(requestLoggerProperties.getTypes())
			.anyMatch(e -> e.name().equals(RequestLoggerTypeEnum.REDIS.name()))) {
			return new RedisRequestLoggerServiceImpl(redisRepository);
		}
		return null;
	}

}
