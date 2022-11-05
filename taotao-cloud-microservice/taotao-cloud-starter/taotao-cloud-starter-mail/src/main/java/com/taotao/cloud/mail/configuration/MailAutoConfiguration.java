/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.mail.configuration;

import cn.hutool.extra.mail.MailAccount;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.mail.template.JavaMailTemplate;
import com.taotao.cloud.mail.template.MailTemplate;
import com.taotao.cloud.mail.util.MailUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * MailAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-09 11:38:47
 */
@AutoConfiguration(after = MailSenderAutoConfiguration.class)
@EnableConfigurationProperties({com.taotao.cloud.mail.properties.MailProperties.class})
@ConditionalOnProperty(prefix = com.taotao.cloud.mail.properties.MailProperties.PREFIX, name = "enabled", havingValue = "true")
public class MailAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(MailAutoConfiguration.class, StarterName.MAIL_STARTER);
	}

	@Bean
	@ConditionalOnBean({MailProperties.class, JavaMailSender.class})
	public MailTemplate mailTemplate(JavaMailSender mailSender, MailProperties mailProperties) {
		LogUtils.started(MailTemplate.class, StarterName.MAIL_STARTER);

		return new JavaMailTemplate(mailSender, mailProperties);
	}

	@Bean
	public MailAccount mailAccount(MailProperties mailProperties) {
		MailAccount account = new MailAccount();
		account.setHost(mailProperties.getHost());
		account.setPort(mailProperties.getPort());
		account.setAuth(mailProperties.getPassword() != null);
		//account.setFrom(mailProperties.getFrom());
		//account.setUser(mailProperties.getUser());
		account.setPass(mailProperties.getPassword());
		account.setSocketFactoryPort(mailProperties.getPort());

		account.setStarttlsEnable(true);
		account.setSslEnable(true);
		//account.setTimeout(mailProperties.getTimeout());
		//account.setConnectionTimeout(mailProperties.getConnectionTimeout());
		return account;
	}

	@Bean
	public MailUtils mailUtils(MailAccount mailAccount) {
		return new MailUtils(mailAccount);
	}
}
