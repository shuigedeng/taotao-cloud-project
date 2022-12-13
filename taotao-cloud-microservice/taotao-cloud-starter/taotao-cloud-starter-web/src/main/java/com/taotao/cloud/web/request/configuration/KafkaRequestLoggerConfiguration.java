package com.taotao.cloud.web.request.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.logger.enums.RequestLoggerTypeEnum;
import com.taotao.cloud.web.request.properties.RequestLoggerProperties;
import com.taotao.cloud.web.request.service.IRequestLoggerService;
import com.taotao.cloud.web.request.service.impl.KafkaRequestLoggerServiceImpl;
import java.util.Arrays;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:21
 */
@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnBean(KafkaTemplate.class)
public class KafkaRequestLoggerConfiguration implements InitializingBean {

	@Autowired
	private RequestLoggerProperties requestLoggerProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(KafkaRequestLoggerConfiguration.class, StarterName.LOGGER_STARTER);
	}

	@Bean
	public IRequestLoggerService kafkaRequestLoggerServiceImpl(
		KafkaTemplate<String, String> kafkaTemplate) {
		if (Arrays.stream(requestLoggerProperties.getTypes())
			.anyMatch(e -> e.name().equals(RequestLoggerTypeEnum.KAFKA.name()))) {
			return new KafkaRequestLoggerServiceImpl(kafkaTemplate);
		}
		return null;
	}
}
