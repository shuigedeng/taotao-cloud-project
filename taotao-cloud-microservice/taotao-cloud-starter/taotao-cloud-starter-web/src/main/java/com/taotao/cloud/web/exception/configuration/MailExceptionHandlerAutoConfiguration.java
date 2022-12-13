package com.taotao.cloud.web.exception.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.web.exception.enums.ExceptionHandleTypeEnum;
import com.taotao.cloud.web.exception.handler.ExceptionHandler;
import com.taotao.cloud.web.exception.handler.MailExceptionHandler;
import com.taotao.cloud.web.exception.properties.ExceptionHandleProperties;
import java.util.Arrays;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:21
 */
@AutoConfiguration
@ConditionalOnClass(JavaMailSender.class)
@ConditionalOnBean({JavaMailSender.class, MailProperties.class})
@ConditionalOnProperty(prefix = ExceptionHandleProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class MailExceptionHandlerAutoConfiguration implements InitializingBean {

	@Autowired
	private ExceptionHandleProperties exceptionHandleProperties;
	@Autowired
	private MailProperties mailProperties;

	@Value("${spring.application.name: unknown-application}")
	private String applicationName;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(MailExceptionHandlerAutoConfiguration.class, StarterName.MAIL_STARTER);
	}

	@Bean
	public ExceptionHandler mailGlobalExceptionHandler(JavaMailSender mailSender) {
		if (Arrays.stream(exceptionHandleProperties.getTypes())
			.anyMatch(e -> e.name().equals(ExceptionHandleTypeEnum.MAIL.name()))) {
			return new MailExceptionHandler(mailProperties, exceptionHandleProperties, mailSender,
				applicationName);
		}
		return null;
	}
}
