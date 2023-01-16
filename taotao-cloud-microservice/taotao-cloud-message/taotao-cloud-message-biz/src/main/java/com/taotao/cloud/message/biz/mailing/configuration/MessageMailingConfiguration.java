
package com.taotao.cloud.message.biz.mailing.configuration;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *  消息互动
 */
@Configuration(proxyBeanMethods = false)
@EntityScan(basePackages = {
	"com.taotao.cloud.message.mailing.entity"
})
@EnableJpaRepositories(basePackages = {
	"com.taotao.cloud.message.biz.mailing.repository",
})
@ComponentScan(basePackages = {
	"com.taotao.cloud.message.biz.mailing.service",
	"com.taotao.cloud.message.biz.mailing.controller",
})
public class MessageMailingConfiguration {

	private static final Logger log = LoggerFactory.getLogger(MessageMailingConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.debug("[Websocket] |- SDK [Message Mailing] Auto Configure.");
	}
}
