package com.taotao.cloud.auth.api.compliance.configuration;//
// package com.taotao.cloud.auth.api.compliance.configuration;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
// import org.springframework.boot.autoconfigure.domain.EntityScan;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.ComponentScan;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//
// import jakarta.annotation.PostConstruct;
//
// /**
//  * <p>Description: OAuth2 应用安全合规配置 </p>
//  *
//  * @author : gengwei.zheng
//  * @date : 2022/7/11 10:20
//  */
// @Configuration(proxyBeanMethods = false)
// @ConditionalOnClass(AccountStatusChangeService.class)
// @EntityScan(basePackages = {
//         "cn.herodotus.engine.oauth2.compliance.entity"
// })
// @EnableJpaRepositories(basePackages = {
//         "cn.herodotus.engine.oauth2.compliance.repository",
// })
// @ComponentScan(basePackages = {
//         "cn.herodotus.engine.oauth2.compliance.stamp",
//         "cn.herodotus.engine.oauth2.compliance.service",
//         "cn.herodotus.engine.oauth2.compliance.controller",
// })
// public class OAuth2ComplianceConfiguration {
//
//     private static final Logger log = LoggerFactory.getLogger(OAuth2ComplianceConfiguration.class);
//
//     @PostConstruct
//     public void postConstruct() {
//         log.debug("[Herodotus] |- SDK [Engine OAuth2 Compliance] Auto Configure.");
//     }
//
//     @Bean
//     @ConditionalOnAutoUnlockUserAccount
//     public AccountStatusListener accountLockStatusListener(RedisMessageListenerContainer redisMessageListenerContainer, OAuth2AccountStatusService accountLockService) {
//         AccountStatusListener lockStatusListener = new AccountStatusListener(redisMessageListenerContainer, accountLockService);
//         log.trace("[Herodotus] |- Bean [OAuth2 Account Lock Status Listener] Auto Configure.");
//         return lockStatusListener;
//     }
//
//     @Bean
//     @ConditionalOnMissingBean
//     public AuthenticationFailureListener authenticationFailureListener(SignInFailureLimitedStampManager stampManager, OAuth2AccountStatusService accountLockService) {
//         AuthenticationFailureListener authenticationFailureListener = new AuthenticationFailureListener(stampManager, accountLockService);
//         log.trace("[Herodotus] |- Bean [OAuth2 Authentication Failure Listener] Auto Configure.");
//         return authenticationFailureListener;
//     }
//
//     @Bean
//     @ConditionalOnMissingBean
//     public AuthenticationSuccessListener authenticationSuccessListener(SignInFailureLimitedStampManager stampManager, OAuth2ComplianceService complianceService) {
//         AuthenticationSuccessListener authenticationSuccessListener = new AuthenticationSuccessListener(stampManager, complianceService);
//         log.trace("[Herodotus] |- Bean [OAuth2 Authentication Success Listener] Auto Configure.");
//         return authenticationSuccessListener;
//     }
//
// }
