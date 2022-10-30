package com.taotao.cloud.logger.configuration.request;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.logger.aspect.RequestLoggerAspect;
import com.taotao.cloud.logger.listener.RequestLoggerListener;
import com.taotao.cloud.logger.properties.RequestLoggerProperties;
import com.taotao.cloud.logger.service.IRequestLoggerService;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:21
 */
@AutoConfiguration(after = {
	RedisRequestLoggerConfiguration.class,
	KafkaRequestLoggerConfiguration.class,
	KafkaRequestLoggerConfiguration.class})
@EnableConfigurationProperties({RequestLoggerProperties.class})
@ConditionalOnProperty(prefix = RequestLoggerProperties.PREFIX, name = "enabled", havingValue = "true")
public class RequestLoggerConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(RequestLoggerConfiguration.class, StarterName.LOG_STARTER);
	}

	@Bean
	public RequestLoggerListener requestLoggerListener(
		List<IRequestLoggerService> requestLoggerServices) {
		return new RequestLoggerListener(requestLoggerServices);
	}

	@Bean
	public RequestLoggerAspect requestLoggerAspect() {
		return new RequestLoggerAspect();
	}
}
