package com.taotao.cloud.monitor.kuding.config.servicemonitor;

import com.taotao.cloud.monitor.kuding.message.EmailNoticeSendComponent;
import com.taotao.cloud.monitor.kuding.message.INoticeSendComponent;
import com.taotao.cloud.monitor.kuding.properties.notice.EmailNoticeProperty;
import com.taotao.cloud.monitor.kuding.config.annos.ConditionalOnServiceMonitor;
import com.taotao.cloud.monitor.kuding.pojos.notice.ServiceCheckNotice;
import com.taotao.cloud.monitor.kuding.text.ServiceMonitorResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;


@Configuration
@AutoConfigureAfter({ MailSenderAutoConfiguration.class })
@ConditionalOnBean({ MailSender.class, MailProperties.class })
@ConditionalOnServiceMonitor
@ConditionalOnProperty(value = "prometheus.email.enabled", havingValue = "true")
public class ExceptionNoticeEmailSendingConfig {

	@Autowired
	private MailSender mailSender;
	@Autowired
	private MailProperties mailProperties;
	@Autowired
	private EmailNoticeProperty emailExceptionNoticeProperty;

	private final static Log logger = LogFactory.getLog(ExceptionNoticeEmailSendingConfig.class);

	@Bean
	@ConditionalOnMissingBean(parameterizedContainer = INoticeSendComponent.class)
	public INoticeSendComponent<ServiceCheckNotice> emailNoticeSendComponent(
			ServiceMonitorResolver exceptionNoticeResolver) {
		logger.debug("创建邮件异常通知");
		INoticeSendComponent<ServiceCheckNotice> component = new EmailNoticeSendComponent<ServiceCheckNotice>(
				mailSender, mailProperties, emailExceptionNoticeProperty, exceptionNoticeResolver);
		return component;
	}

	@Bean
	@ConditionalOnMissingBean
	public ServiceMonitorResolver serviceMonitorResolver() {
		return x -> x.generateText();
	}
}
