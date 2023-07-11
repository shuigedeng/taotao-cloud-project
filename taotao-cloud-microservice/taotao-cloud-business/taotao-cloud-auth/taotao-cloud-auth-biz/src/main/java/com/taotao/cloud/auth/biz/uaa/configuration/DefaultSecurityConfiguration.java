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

package com.taotao.cloud.auth.biz.uaa.configuration;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.taotao.cloud.auth.biz.authentication.event.DefaultOAuth2AuthenticationEventPublisher;
import com.taotao.cloud.auth.biz.authentication.login.extension.ExtensionLoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.fingerprint.service.FingerprintUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.login.extension.gestures.service.GesturesUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.login.extension.wechatmp.service.WechatMpUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.login.form.captcha.OAuth2FormCaptchaLoginHttpConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.form.sms.Oauth2FormSmsLoginHttpConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.social.SocialDelegateClientRegistrationRepository;
import com.taotao.cloud.auth.biz.authentication.login.social.SocialProviderConfigurer;
import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.auth.biz.authentication.token.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.token.JwtTokenGeneratorImpl;
import com.taotao.cloud.auth.biz.management.processor.ClientDetailsService;
import com.taotao.cloud.auth.biz.management.processor.Oauth2ClientDetailsService;
import com.taotao.cloud.auth.biz.management.processor.SecurityUserDetailsService;
import com.taotao.cloud.auth.biz.management.service.OAuth2ApplicationService;
import com.taotao.cloud.auth.biz.strategy.StrategyUserDetailsService;
import com.taotao.cloud.captcha.support.core.processor.CaptchaRendererFactory;
import com.taotao.cloud.security.springsecurity.core.response.SecurityAccessDeniedHandler;
import com.taotao.cloud.security.springsecurity.core.response.SecurityAuthenticationEntryPoint;
import com.taotao.cloud.security.springsecurity.customizer.SecurityTokenStrategyConfigurer;
import com.taotao.cloud.security.springsecurity.processor.SecurityAuthorizationManager;
import com.taotao.cloud.security.springsecurity.processor.SecurityMatcherConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
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
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * <p>Description: 默认安全配置 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 10:31:08
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DefaultSecurityConfiguration.class);

    /// **
    // * 跨域过滤器配置
    // */
    // @Bean
    // public CorsFilter corsFilter() {
    //	// 初始化cors配置对象
    //	CorsConfiguration configuration = new CorsConfiguration();
    //	// 设置允许跨域的域名,如果允许携带cookie的话,路径就不能写*号, *表示所有的域名都可以跨域访问
    //	configuration.addAllowedOrigin("http://127.0.0.1:5173");
    //	// 设置跨域访问可以携带cookie
    //	configuration.setAllowCredentials(true);
    //	// 允许所有的请求方法 ==> GET POST PUT Delete
    //	configuration.addAllowedMethod("*");
    //	// 允许携带任何头信息
    //	configuration.addAllowedHeader("*");
    //	// 初始化cors配置源对象
    //	UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
    //	// 给配置源对象设置过滤的参数
    //	// 参数一: 过滤的路径 == > 所有的路径都要求校验是否跨域
    //	// 参数二: 配置类
    //	configurationSource.registerCorsConfiguration("/**", configuration);
    //	// 返回配置好的过滤器
    //	return new CorsFilter(configurationSource);
    // }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity httpSecurity,
            UserDetailsService userDetailsService,
            OAuth2AuthenticationProperties authenticationProperties,
            CaptchaRendererFactory captchaRendererFactory,
            SecurityMatcherConfigurer securityMatcherConfigurer,
            SecurityAuthorizationManager securityAuthorizationManager,
            SecurityTokenStrategyConfigurer herodotusTokenStrategyConfigurer,
            SocialDelegateClientRegistrationRepository socialDelegateClientRegistrationRepository)
            throws Exception {

        log.info("Core [Default Security Filter Chain] Auto Configure.");

        // 添加跨域过滤器
        // httpSecurity.addFilter(corsFilter());

        // 使用redis存储、读取登录的认证信息
        // httpSecurity.securityContext(context -> context.securityContextRepository(redisSecurityContextRepository));

        // 禁用CSRF 开启跨域
        httpSecurity
                .anonymous(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable);

        httpSecurity
                .authorizeHttpRequests(authorizeHttpRequestsCustomizer -> {
                    authorizeHttpRequestsCustomizer
                            .requestMatchers(securityMatcherConfigurer.getPermitAllArray())
                            .permitAll()
                            .requestMatchers(securityMatcherConfigurer.getStaticResourceArray())
                            .permitAll()
                            .requestMatchers(EndpointRequest.toAnyEndpoint())
                            .permitAll()
                            .anyRequest()
                            .access(securityAuthorizationManager);
                })
                .exceptionHandling(exceptionHandlingCustomizer -> {
                    exceptionHandlingCustomizer
                            .authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
                            .accessDeniedHandler(new SecurityAccessDeniedHandler());
                })
                .oauth2ResourceServer(herodotusTokenStrategyConfigurer::from)
                .logout(logoutCustomizer -> {
                    logoutCustomizer
                            .addLogoutHandler((request, response, authentication) -> {})
                            .logoutSuccessHandler((request, response, authentication) -> {})
                            .clearAuthentication(true);
                })
                // **************************************自定义登录配置***********************************************
                .apply(new ExtensionLoginFilterSecurityConfigurer<>())
                // 用户+密码登录
                .accountLogin(accountLoginConfigurerCustomizer -> {})

                // 验证码登录
                .captchaLogin(captchaLoginConfigurerCustomizer -> {})

                // 面部识别登录
                .faceLogin(faceLoginConfigurerCustomizer -> {})

                // 指纹登录
                .fingerprintLogin(fingerprintLoginConfigurer -> {
                    fingerprintLoginConfigurer.fingerprintUserDetailsService(new FingerprintUserDetailsService() {
                        @Override
                        public UserDetails loadUserByFingerprint(String username) throws UsernameNotFoundException {
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
                .oneClickLogin(oneClickLoginConfigurer -> {})

                // 手机扫码登录
                .qrcodeLogin(qrcodeLoginConfigurer -> {})

                // 短信登录
                .smsLogin(smsLoginConfigurerCustomizer -> {})

                // email登录
                .emailLogin(emailLoginConfigurerCustomizer -> {})

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
                .wechatMiniAppLogin(miniAppLoginConfigurer -> {})
                .httpSecurity()
                // **************************************oauth2登录配置***********************************************
                .apply(new SocialProviderConfigurer(socialDelegateClientRegistrationRepository))
                // 微信网页授权
                .wechatWebclient("wxcd395c35c45eb823", "75f9a12c82bd24ecac0d37bf1156c749")
                // 企业微信扫码登录
                .workWechatWebLoginclient("wwa70dc5b6e56936e1", "nvzGI4Alp3xxxxxxZUc3TtPtKbnfTEets5W8", "1000005")
                // 微信扫码登录
                .wechatWebLoginclient("wxcd395c35c45eb823", "75f9a12c82bd24ecac0d37bf1156c749")
                .httpSecurity()
                // **************************************oauth2表单登录配置***********************************************
                .apply(new OAuth2FormCaptchaLoginHttpConfigurer<>(
                        userDetailsService, authenticationProperties, captchaRendererFactory))
                .httpSecurity()
                .apply(new Oauth2FormSmsLoginHttpConfigurer<>(authenticationProperties));

        return httpSecurity.build();
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
        log.info("Bean [Authentication Event Publisher] Auto Configure.");
        return new DefaultOAuth2AuthenticationEventPublisher(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsService userDetailsService(StrategyUserDetailsService strategyUserDetailsService) {
        SecurityUserDetailsService securityUserDetailsService =
                new SecurityUserDetailsService(strategyUserDetailsService);
        log.info("Bean [Herodotus User Details Service] Auto Configure.");
        return securityUserDetailsService;
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientDetailsService clientDetailsService(OAuth2ApplicationService applicationService) {
        Oauth2ClientDetailsService oauth2ClientDetailsService = new Oauth2ClientDetailsService(applicationService);
        log.info("Bean [Herodotus Client Details Service] Auto Configure.");
        return oauth2ClientDetailsService;
    }

    @Bean
    public SessionRegistry sessionRegistry(FindByIndexNameSessionRepository<? extends Session> sessionRepository) {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
