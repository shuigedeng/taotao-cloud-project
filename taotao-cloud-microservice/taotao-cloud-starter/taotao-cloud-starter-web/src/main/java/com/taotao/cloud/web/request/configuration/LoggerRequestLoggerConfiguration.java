package com.taotao.cloud.web.request.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.logger.enums.RequestLoggerTypeEnum;
import com.taotao.cloud.web.request.properties.RequestLoggerProperties;
import com.taotao.cloud.web.request.service.IRequestLoggerService;
import com.taotao.cloud.web.request.service.impl.LoggerRequestLoggerServiceImpl;
import java.util.Arrays;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:21
 */
@AutoConfiguration
public class LoggerRequestLoggerConfiguration implements InitializingBean {

	@Autowired
	private RequestLoggerProperties requestLoggerProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(LoggerRequestLoggerConfiguration.class, StarterName.LOG_STARTER);
	}

	@Bean
	public IRequestLoggerService loggerRequestLoggerService() {
		if (Arrays.stream(requestLoggerProperties.getTypes())
			.anyMatch(e -> e.name().equals(RequestLoggerTypeEnum.LOGGER.name()))) {
			return new LoggerRequestLoggerServiceImpl();
		}
		return null;
	}

}
