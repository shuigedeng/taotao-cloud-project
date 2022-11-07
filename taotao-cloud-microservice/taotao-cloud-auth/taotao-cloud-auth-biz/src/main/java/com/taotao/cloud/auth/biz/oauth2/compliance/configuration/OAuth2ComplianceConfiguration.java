/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
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

package com.taotao.cloud.auth.biz.oauth2.compliance.configuration;

import com.taotao.cloud.auth.biz.oauth2.compliance.definition.AccountStatusChangeService;
import com.taotao.cloud.auth.biz.oauth2.compliance.listener.AccountStatusListener;
import com.taotao.cloud.auth.biz.oauth2.compliance.listener.AuthenticationFailureListener;
import com.taotao.cloud.auth.biz.oauth2.compliance.listener.AuthenticationSuccessListener;
import com.taotao.cloud.auth.biz.oauth2.compliance.service.OAuth2AccountStatusService;
import com.taotao.cloud.auth.biz.oauth2.compliance.service.OAuth2ComplianceService;
import com.taotao.cloud.auth.biz.oauth2.compliance.stamp.SignInFailureLimitedStampManager;
import com.taotao.cloud.auth.biz.oauth2.core.annotation.ConditionalOnAutoUnlockUserAccount;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * <p>Description: OAuth2 应用安全合规配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/11 10:20
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AccountStatusChangeService.class)
@EntityScan(basePackages = {
	"cn.herodotus.engine.oauth2.compliance.entity"
})
@EnableJpaRepositories(basePackages = {
	"cn.herodotus.engine.oauth2.compliance.repository",
})
@ComponentScan(basePackages = {
	"cn.herodotus.engine.oauth2.compliance.stamp",
	"cn.herodotus.engine.oauth2.compliance.service",
	"cn.herodotus.engine.oauth2.compliance.controller",
})
public class OAuth2ComplianceConfiguration {

	private static final Logger log = LoggerFactory.getLogger(OAuth2ComplianceConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.debug("[Herodotus] |- SDK [Engine OAuth2 Compliance] Auto Configure.");
	}

	@Bean
	@ConditionalOnAutoUnlockUserAccount
	public AccountStatusListener accountLockStatusListener(
		RedisMessageListenerContainer redisMessageListenerContainer,
		OAuth2AccountStatusService accountLockService) {
		AccountStatusListener lockStatusListener = new AccountStatusListener(
			redisMessageListenerContainer, accountLockService);
		log.trace("[Herodotus] |- Bean [OAuth2 Account Lock Status Listener] Auto Configure.");
		return lockStatusListener;
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationFailureListener authenticationFailureListener(
		SignInFailureLimitedStampManager stampManager,
		OAuth2AccountStatusService accountLockService) {
		AuthenticationFailureListener authenticationFailureListener = new AuthenticationFailureListener(
			stampManager, accountLockService);
		log.trace("[Herodotus] |- Bean [OAuth2 Authentication Failure Listener] Auto Configure.");
		return authenticationFailureListener;
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationSuccessListener authenticationSuccessListener(
		SignInFailureLimitedStampManager stampManager, OAuth2ComplianceService complianceService) {
		AuthenticationSuccessListener authenticationSuccessListener = new AuthenticationSuccessListener(
			stampManager, complianceService);
		log.trace("[Herodotus] |- Bean [OAuth2 Authentication Success Listener] Auto Configure.");
		return authenticationSuccessListener;
	}

}
