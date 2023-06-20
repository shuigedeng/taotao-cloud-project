/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social.justauth.configuration;

import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.enums.AccountType;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.justauth.processor.JustAuthAccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.justauth.processor.JustAuthProcessor;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.justauth.properties.JustAuthProperties;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.justauth.stamp.JustAuthStateStampManager;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: JustAuth配置 </p>
 * <p>
 * 仅在存在herodotus.platform.social.justauth.configs配置的情况下才注入
 *
 * 
 * @date : 2021/5/22 11:25
 */
@Configuration(proxyBeanMethods = false)
//@ConditionalOnJustAuthEnabled
@EnableConfigurationProperties(JustAuthProperties.class)
public class JustAuthConfiguration {

	private static final Logger log = LoggerFactory.getLogger(JustAuthConfiguration.class);

	@PostConstruct
	public void init() {
		log.debug("[Herodotus] |- SDK [Access Just Auth] Auto Configure.");
	}

	@Bean
	@ConditionalOnMissingBean
	public JustAuthStateStampManager justAuthStateStampManager(RedisRepository redisRepository, JustAuthProperties justAuthProperties) {
		JustAuthStateStampManager justAuthStateStampManager = new JustAuthStateStampManager(redisRepository, justAuthProperties);
		log.trace("[Herodotus] |- Bean [Just Auth State Redis Cache] Auto Configure.");
		return justAuthStateStampManager;
	}

	@Bean
	@ConditionalOnBean(JustAuthStateStampManager.class)
	@ConditionalOnMissingBean
	public JustAuthProcessor justAuthProcessor(JustAuthStateStampManager justAuthStateStampManager, JustAuthProperties justAuthProperties) {
		JustAuthProcessor justAuthProcessor = new JustAuthProcessor();
		justAuthProcessor.setJustAuthStateRedisCache(justAuthStateStampManager);
		justAuthProcessor.setJustAuthProperties(justAuthProperties);
		log.trace("[Herodotus] |- Bean [Just Auth Request Generator] Auto Configure.");
		return justAuthProcessor;
	}

	@Bean(AccountType.JUST_AUTH_HANDLER)
	@ConditionalOnBean(JustAuthProcessor.class)
	@ConditionalOnMissingBean
	public JustAuthAccessHandler justAuthAccessHandler(JustAuthProcessor justAuthProcessor) {
		JustAuthAccessHandler justAuthAccessHandler = new JustAuthAccessHandler(justAuthProcessor);
		log.debug("[Herodotus] |- Bean [Just Auth Access Handler] Auto Configure.");
		return justAuthAccessHandler;
	}
}
