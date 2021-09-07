package com.taotao.cloud.prometheus.config;

import com.taotao.cloud.prometheus.annotation.ConditionalOnExceptionNotice;
import com.taotao.cloud.prometheus.properties.EmailNoticeProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;


@Configuration
@ConditionalOnExceptionNotice
@ConditionalOnProperty(value = "prometheus.email.enabled", havingValue = "true")
public class ExceptionNoticeEmailSendingConfig {

	@Autowired
	private EmailNoticeProperties emailExceptionNoticeProperty;

	private final static Log logger = LogFactory.getLog(ExceptionNoticeEmailSendingConfig.class);

	@Bean("emailSendingComponent")
	@ConditionalOnMissingBean(name = "emailSendingComponent")
	public INoticeSendComponent<ExceptionNotice> emailNoticeSendComponent(MailProperties mailProperties,
			MailSender mailSender) {
		logger.debug("创建邮件异常通知");
		INoticeSendComponent<ExceptionNotice> component = new EmailNoticeSendComponent<ExceptionNotice>(mailSender,
				mailProperties, emailExceptionNoticeProperty, ExceptionNoticeTextResolver());
		return component;
	}

	@Bean
	@ConditionalOnMissingBean
	public NoticeTextResolver<ExceptionNotice> ExceptionNoticeTextResolver() {
		return x -> x.createText();
	}
}
