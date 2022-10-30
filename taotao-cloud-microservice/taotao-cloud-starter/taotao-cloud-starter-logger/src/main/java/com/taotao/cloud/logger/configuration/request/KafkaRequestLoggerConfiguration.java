package com.taotao.cloud.logger.configuration.request;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.logger.annotation.ConditionalOnRequestLoggerType;
import com.taotao.cloud.logger.enums.RequestLoggerTypeEnum;
import com.taotao.cloud.logger.properties.RequestLoggerProperties;
import com.taotao.cloud.logger.service.IRequestLoggerService;
import com.taotao.cloud.logger.service.impl.KafkaRequestLoggerServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:21
 */
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnBean(KafkaTemplate.class)
@ConditionalOnProperty(prefix = RequestLoggerProperties.PREFIX, name = "enabled", havingValue = "true")
@AutoConfiguration
public class KafkaRequestLoggerConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(KafkaRequestLoggerConfiguration.class, StarterName.LOG_STARTER);
	}

	@Bean
	@ConditionalOnRequestLoggerType(logType = RequestLoggerTypeEnum.KAFKA)
	public IRequestLoggerService kafkaRequestLoggerServiceImpl(
		KafkaTemplate<String, String> kafkaTemplate) {
		return new KafkaRequestLoggerServiceImpl(kafkaTemplate);
	}
}
