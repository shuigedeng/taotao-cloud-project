/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
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
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.dante.management.configuration;

import com.taotao.cloud.auth.biz.dante.authentication.configuration.OAuth2AuthenticationConfiguration;
import com.taotao.cloud.auth.biz.dante.authentication.stamp.SignInFailureLimitedStampManager;
import com.taotao.cloud.auth.biz.dante.jpa.configuration.OAuth2DataJpaConfiguration;
import com.taotao.cloud.auth.biz.dante.management.compliance.listener.AuthenticationSuccessListener;
import com.taotao.cloud.auth.biz.dante.management.service.OAuth2ComplianceService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <p>Description: OAuth2 Manager 模块配置 </p>
 * <p>
 * {@link org.springframework.security.oauth2.jwt.JwtTimestampValidator}
 *
 * @author : gengwei.zheng
 * @date : 2022/2/26 12:35
 */
@AutoConfiguration
@Import({OAuth2DataJpaConfiguration.class, OAuth2AuthenticationConfiguration.class, OAuth2ComplianceConfiguration.class})
@EntityScan(basePackages = {
	"cn.herodotus.engine.oauth2.management.entity"
})
@EnableJpaRepositories(basePackages = {
	"cn.herodotus.engine.oauth2.management.repository",
})
@ComponentScan(basePackages = {
	"cn.herodotus.engine.oauth2.management.service",
	"cn.herodotus.engine.oauth2.management.controller",
})
public class OAuth2ManagementConfiguration {

	private static final Logger log = LoggerFactory.getLogger(OAuth2ManagementConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.debug("[Herodotus] |- SDK [OAuth2 Authorization Server] Auto Configure.");
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationSuccessListener authenticationSuccessListener(SignInFailureLimitedStampManager stampManager, OAuth2ComplianceService complianceService) {
		AuthenticationSuccessListener authenticationSuccessListener = new AuthenticationSuccessListener(stampManager, complianceService);
		log.trace("[Herodotus] |- Bean [OAuth2 Authentication Success Listener] Auto Configure.");
		return authenticationSuccessListener;
	}

}
