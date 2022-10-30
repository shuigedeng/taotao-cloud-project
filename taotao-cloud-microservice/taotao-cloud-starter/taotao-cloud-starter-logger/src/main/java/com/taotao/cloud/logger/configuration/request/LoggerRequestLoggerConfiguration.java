package com.taotao.cloud.logger.configuration.request;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.logger.annotation.ConditionalOnRequestLoggerType;
import com.taotao.cloud.logger.enums.RequestLoggerTypeEnum;
import com.taotao.cloud.logger.properties.RequestLoggerProperties;
import com.taotao.cloud.logger.service.IRequestLoggerService;
import com.taotao.cloud.logger.service.impl.LoggerRequestLoggerServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:21
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = RequestLoggerProperties.PREFIX, name = "enabled", havingValue = "true")
public class LoggerRequestLoggerConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(LoggerRequestLoggerConfiguration.class, StarterName.LOG_STARTER);
	}

	@Bean
	@ConditionalOnRequestLoggerType(logType = RequestLoggerTypeEnum.LOGGER)
	public IRequestLoggerService loggerRequestLoggerService() {
		return new LoggerRequestLoggerServiceImpl();
	}

}
