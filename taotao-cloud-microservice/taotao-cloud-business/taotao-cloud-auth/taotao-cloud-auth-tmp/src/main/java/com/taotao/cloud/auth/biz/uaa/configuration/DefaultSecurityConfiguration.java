/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Cloud licensed under the Apache License, Version 2.0 (the "License");
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
 * Dante Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/dromara/dante-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/dromara/dante-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.uaa.configuration;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.taotao.cloud.auth.biz.authentication.authentication.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.authentication.JwtTokenGeneratorImpl;
import com.taotao.cloud.auth.biz.authentication.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.fingerprint.service.FingerprintUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.authentication.gestures.service.GesturesUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.authentication.wechatmp.service.WechatMpUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.authentication.oauth2.DelegateClientRegistrationRepository;
import com.taotao.cloud.auth.biz.authentication.authentication.oauth2.OAuth2ProviderConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.oauth2.Oauth2LoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.biz.authentication.authentication.oneClick.service.OneClickUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.form.OAuth2FormLoginSecureConfigurer;
import com.taotao.cloud.auth.biz.authentication.form.Oauth2FormPhoneLoginSecureConfigurer;
import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.auth.biz.authentication.response.DefaultOAuth2AuthenticationEventPublisher;
import com.taotao.cloud.auth.biz.management.processor.HerodotusClientDetailsService;
import com.taotao.cloud.auth.biz.management.processor.HerodotusUserDetailsService;
import com.taotao.cloud.auth.biz.management.service.OAuth2ApplicationService;
import com.taotao.cloud.captcha.support.core.processor.CaptchaRendererFactory;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import com.taotao.cloud.security.springsecurity.authorization.customizer.HerodotusTokenStrategyConfigurer;
import com.taotao.cloud.security.springsecurity.authorization.processor.SecurityAuthorizationManager;
import com.taotao.cloud.security.springsecurity.authorization.processor.SecurityMatcherConfigurer;
import com.taotao.cloud.security.springsecurity.core.definition.service.ClientDetailsService;
import com.taotao.cloud.security.springsecurity.core.definition.strategy.StrategyUserDetailsService;
import com.taotao.cloud.security.springsecurity.core.response.HerodotusAccessDeniedHandler;
import com.taotao.cloud.security.springsecurity.core.response.HerodotusAuthenticationEntryPoint;
import org.redisson.api.RedissonClient;
import org.redisson.spring.session.RedissonSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.util.Assert;

/**
 * <p>Description: 默认安全配置 </p>
 *
 * @date : 2022/2/12 20:53
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfiguration {

	private static final Logger log = LoggerFactory.getLogger(DefaultSecurityConfiguration.class);

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(
		HttpSecurity httpSecurity,
		UserDetailsService userDetailsService,
		OAuth2AuthenticationProperties authenticationProperties,
		CaptchaRendererFactory captchaRendererFactory,
		SecurityMatcherConfigurer securityMatcherConfigurer,
		SecurityAuthorizationManager securityAuthorizationManager,
		HerodotusTokenStrategyConfigurer herodotusTokenStrategyConfigurer,
		DelegateClientRegistrationRepository delegateClientRegistrationRepository
	) throws Exception {

		log.debug("[Herodotus] |- Core [Default Security Filter Chain] Auto Configure.");

		// 禁用CSRF 开启跨域
		httpSecurity
			.anonymous(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.sessionManagement(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable);

		// @formatter:off
        httpSecurity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(securityMatcherConfigurer.getPermitAllArray()).permitAll()
                        .requestMatchers(securityMatcherConfigurer.getStaticResourceArray()).permitAll()
                        .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                        .anyRequest().access(securityAuthorizationManager))
                .exceptionHandling(exceptions -> {
                    exceptions.authenticationEntryPoint(new HerodotusAuthenticationEntryPoint());
                    exceptions.accessDeniedHandler(new HerodotusAccessDeniedHandler());
                })
                .oauth2ResourceServer(herodotusTokenStrategyConfigurer::from)
			// **************************************自定义登录配置***********************************************
			.apply(new LoginFilterSecurityConfigurer<>())
			// 用户+密码登录
			.accountLogin(accountLoginFilterConfigurerCustomizer -> {

			})
			// 用户+密码+验证码登录
			.accountVerificationLogin(accountVerificationLoginFilterConfigurerCustomizer -> {
			})
			// 面部识别登录
			.faceLogin(faceLoginFilterConfigurerCustomizer -> {
			})
			// 指纹登录
			.fingerprintLogin(fingerprintLoginConfigurer -> {
				fingerprintLoginConfigurer.fingerprintUserDetailsService(new FingerprintUserDetailsService() {
					@Override
					public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
						return null;
					}
				});
			})
			// 手势登录
			.gesturesLogin(fingerprintLoginConfigurer -> {
				fingerprintLoginConfigurer.gesturesUserDetailsService(new GesturesUserDetailsService() {
					@Override
					public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
						return null;
					}
				});
			})
			// 本机号码一键登录
			.oneClickLogin(oneClickLoginConfigurer -> {

			})
			// 手机扫码登录
			.qrcodeLogin(qrcodeLoginConfigurer -> {
			})
			// 手机号码+短信登录
			.phoneLogin(phoneLoginConfigurer -> {
			})
			// 微信公众号登录
			.wechatMpLogin(mpLoginConfigurer -> {
				mpLoginConfigurer.mpUserDetailsService(new WechatMpUserDetailsService() {
					@Override
					public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
						return null;
					}
				});
			})
			// 小程序登录 同时支持多个小程序
			.wechatMiniAppLogin(miniAppLoginConfigurer -> {
			})
			.and()
			// **************************************oauth2登录配置***********************************************
			.apply(new OAuth2ProviderConfigurer(delegateClientRegistrationRepository))
			// 微信网页授权
			.wechatWebclient("wxcd395c35c45eb823", "75f9a12c82bd24ecac0d37bf1156c749")
			// 企业微信登录
			.workWechatWebLoginclient("wwa70dc5b6e56936e1", "nvzGI4Alp3xxxxxxZUc3TtPtKbnfTEets5W8", "1000005")
			// 微信扫码登录
			.wechatWebLoginclient("wxcd395c35c45eb823", "75f9a12c82bd24ecac0d37bf1156c749")
			.oAuth2LoginConfigurerConsumer(oauth2LoginConfigurer -> {
				oauth2LoginConfigurer
					.successHandler(authenticationSuccessHandler(httpSecurity))
					// 认证失败后的处理器
					.failureHandler((request, response, authException) -> {
						LogUtils.error("用户认证失败", authException);
						ResponseUtils.fail(response, authException.getMessage());
					});
			})
			.and()
			// **************************************oauth2表单登录配置***********************************************
			.apply(new OAuth2FormLoginSecureConfigurer<>(userDetailsService, authenticationProperties, captchaRendererFactory))
			.httpSecurity()
			.apply(new Oauth2FormPhoneLoginSecureConfigurer<>( authenticationProperties));

        // @formatter:on
		return httpSecurity.build();
	}

	private AuthenticationSuccessHandler authenticationSuccessHandler(HttpSecurity httpSecurity) {
		ApplicationContext applicationContext = httpSecurity.getSharedObject(ApplicationContext.class);
		JwtTokenGenerator jwtTokenGenerator = applicationContext.getBean(JwtTokenGenerator.class);
		Assert.notNull(jwtTokenGenerator, "jwtTokenGenerator is required");

		return new Oauth2LoginAuthenticationSuccessHandler(jwtTokenGenerator);
	}

	@Bean
	public JwtTokenGenerator jwtTokenGenerator(JWKSource<SecurityContext> jwkSource) {
		return new JwtTokenGeneratorImpl(jwkSource);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationEventPublisher authenticationEventPublisher(ApplicationContext applicationContext) {
		log.debug("[Herodotus] |- Bean [Authentication Event Publisher] Auto Configure.");
		return new DefaultOAuth2AuthenticationEventPublisher(applicationContext);
	}

	@Bean
	@ConditionalOnMissingBean
	public UserDetailsService userDetailsService(StrategyUserDetailsService strategyUserDetailsService) {
		HerodotusUserDetailsService herodotusUserDetailsService = new HerodotusUserDetailsService(strategyUserDetailsService);
		log.debug("[Herodotus] |- Bean [Herodotus User Details Service] Auto Configure.");
		return herodotusUserDetailsService;
	}

	@Bean
	@ConditionalOnMissingBean
	public ClientDetailsService clientDetailsService(OAuth2ApplicationService applicationService) {
		HerodotusClientDetailsService herodotusClientDetailsService = new HerodotusClientDetailsService(applicationService);
		log.debug("[Herodotus] |- Bean [Herodotus Client Details Service] Auto Configure.");
		return herodotusClientDetailsService;
	}

	@Bean
	public FindByIndexNameSessionRepository redissonSessionRepository(RedissonClient redissonClient, ApplicationEventPublisher eventPublisher) {
		return new RedissonSessionRepository(redissonClient, eventPublisher);
	}

	@Bean
	public SessionRegistry sessionRegistry(FindByIndexNameSessionRepository sessionRepository) {
		return new SpringSessionBackedSessionRegistry<>(sessionRepository);
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}
}
