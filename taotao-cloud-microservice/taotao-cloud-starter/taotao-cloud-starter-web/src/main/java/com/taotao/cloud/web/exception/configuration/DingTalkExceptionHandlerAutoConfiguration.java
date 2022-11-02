package com.taotao.cloud.web.exception.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.dingtalk.model.DingerSender;
import com.taotao.cloud.web.exception.enums.ExceptionHandleTypeEnum;
import com.taotao.cloud.web.exception.handler.DingTalkExceptionHandler;
import com.taotao.cloud.web.exception.handler.ExceptionHandler;
import com.taotao.cloud.web.exception.properties.ExceptionHandleProperties;
import java.util.Arrays;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:21
 */
@AutoConfiguration
@ConditionalOnClass(DingerSender.class)
@ConditionalOnBean(DingerSender.class)
@ConditionalOnProperty(prefix = ExceptionHandleProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DingTalkExceptionHandlerAutoConfiguration implements InitializingBean {

	@Autowired
	private ExceptionHandleProperties exceptionHandleProperties;

	@Value("${spring.application.name: unknown-application}")
	private String applicationName;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(DingTalkExceptionHandlerAutoConfiguration.class, StarterName.LOG_STARTER);
	}

	@Bean
	public ExceptionHandler dingTalkGlobalExceptionHandler(DingerSender dingerSender) {
		if (Arrays.stream(exceptionHandleProperties.getTypes())
			.anyMatch(e -> e.name().equals(ExceptionHandleTypeEnum.DING_TALK.name()))) {
			return new DingTalkExceptionHandler(exceptionHandleProperties, dingerSender,
				applicationName);
		}
		return null;
	}
}
