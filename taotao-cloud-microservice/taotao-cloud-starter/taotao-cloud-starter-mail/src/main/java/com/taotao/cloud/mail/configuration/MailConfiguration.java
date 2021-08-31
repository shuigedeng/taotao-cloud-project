package com.taotao.cloud.mail.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.mail.core.JavaMailTemplate;
import com.taotao.cloud.mail.core.MailTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮件配置
 */
@Configuration
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(MailConfiguration.class, StarterName.MAIL_STARTER);
	}

	@Bean
	@ConditionalOnBean({MailProperties.class, JavaMailSender.class})
	public MailTemplate mailTemplate(JavaMailSender mailSender, MailProperties mailProperties) {
		LogUtil.started(MailTemplate.class, StarterName.MAIL_STARTER);

		return new JavaMailTemplate(mailSender,mailProperties);
	}
}
