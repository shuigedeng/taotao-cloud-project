package com.taotao.cloud.web.exception.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.web.exception.enums.ExceptionHandleTypeEnum;
import com.taotao.cloud.web.exception.handler.ExceptionHandler;
import com.taotao.cloud.web.exception.handler.LoggerExceptionHandler;
import com.taotao.cloud.web.exception.properties.ExceptionHandleProperties;
import java.util.Arrays;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:21
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = ExceptionHandleProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggerExceptionHandlerAutoConfiguration implements InitializingBean {

	@Autowired
	private ExceptionHandleProperties exceptionHandleProperties;

	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	private RequestMappingHandlerMapping mapping;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(LoggerExceptionHandlerAutoConfiguration.class, StarterName.WEB_STARTER);
	}

	@Bean
	public ExceptionHandler dingTalkGlobalExceptionHandler() {
		if (Arrays.stream(exceptionHandleProperties.getTypes())
			.anyMatch(e -> e.name().equals(ExceptionHandleTypeEnum.LOGGER.name()))) {
			return new LoggerExceptionHandler(mapping);
		}
		return null;
	}
}
