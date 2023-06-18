

package com.taotao.cloud.auth.biz.jpa.configuration;

import com.taotao.cloud.auth.biz.jpa.service.HerodotusAuthorizationConsentService;
import com.taotao.cloud.auth.biz.jpa.service.HerodotusAuthorizationService;
import com.taotao.cloud.auth.biz.jpa.service.HerodotusRegisteredClientService;
import com.taotao.cloud.auth.biz.jpa.storage.JpaOAuth2AuthorizationConsentService;
import com.taotao.cloud.auth.biz.jpa.storage.JpaOAuth2AuthorizationService;
import com.taotao.cloud.auth.biz.jpa.storage.JpaRegisteredClientRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * <p>Description: OAuth2 Manager 模块配置 </p>
 *
 *
 * @date : 2022/3/1 18:25
 */
@Configuration(proxyBeanMethods = false)
//@EntityScan(basePackages = {
//        "cn.herodotus.engine.oauth2.data.jpa.entity"
//})
//@EnableJpaRepositories(basePackages = {
//        "cn.herodotus.engine.oauth2.data.jpa.repository",
//})
//@ComponentScan(basePackages = {
//        "cn.herodotus.engine.oauth2.data.jpa.service",
//})
public class OAuth2DataJpaConfiguration {

	private static final Logger log = LoggerFactory.getLogger(OAuth2DataJpaConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.info("[Herodotus] |- SDK [OAuth2 Data JPA] Auto Configure.");
	}

	@Bean
	@ConditionalOnMissingBean
	public RegisteredClientRepository registeredClientRepository(HerodotusRegisteredClientService herodotusRegisteredClientService, PasswordEncoder passwordEncoder) {
		JpaRegisteredClientRepository jpaRegisteredClientRepository = new JpaRegisteredClientRepository(herodotusRegisteredClientService, passwordEncoder);
		log.info("[Herodotus] |- Bean [Jpa Registered Client Repository] Auto Configure.");
		return jpaRegisteredClientRepository;
	}

	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationService authorizationService(HerodotusAuthorizationService herodotusAuthorizationService, RegisteredClientRepository registeredClientRepository) {
		JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService = new JpaOAuth2AuthorizationService(herodotusAuthorizationService, registeredClientRepository);
		log.info("[Herodotus] |- Bean [Jpa OAuth2 Authorization Service] Auto Configure.");
		return jpaOAuth2AuthorizationService;
	}

	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationConsentService authorizationConsentService(HerodotusAuthorizationConsentService herodotusAuthorizationConsentService, RegisteredClientRepository registeredClientRepository) {
		JpaOAuth2AuthorizationConsentService jpaOAuth2AuthorizationConsentService = new JpaOAuth2AuthorizationConsentService(herodotusAuthorizationConsentService, registeredClientRepository);
		log.info("[Herodotus] |- Bean [Jpa OAuth2 Authorization Consent Service] Auto Configure.");
		return jpaOAuth2AuthorizationConsentService;
	}
}
