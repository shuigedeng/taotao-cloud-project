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

package com.taotao.cloud.auth.biz.configuration;

import com.taotao.cloud.auth.biz.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.fingerprint.service.FingerprintUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.gestures.service.GesturesUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.mp.service.MpUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.oauth2.DelegateClientRegistrationRepository;
import com.taotao.cloud.auth.biz.authentication.oauth2.OAuth2ProviderConfigurer;
import com.taotao.cloud.auth.biz.authentication.oneClick.service.OneClickUserDetailsService;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.models.JwtGrantedAuthoritiesConverter;
import com.taotao.cloud.auth.biz.service.MemberUserDetailsService;
import com.taotao.cloud.auth.biz.service.SysUserDetailsService;
import com.taotao.cloud.auth.biz.utils.RedirectLoginAuthenticationSuccessHandler;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * SecurityConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-21 10:20:47
 */
@EnableMethodSecurity(prePostEnabled = true, mode = AdviceMode.PROXY)
@EnableWebSecurity
@Configuration
public class SystemSecurityConfiguration implements EnvironmentAware {

    private static Map<String, String> cache = new HashMap<>();

    public static final String[] permitAllUrls = new String[] {
        "/swagger-ui.html",
        "/v3/**",
        "/favicon.ico",
        "/swagger-resources/**",
        "/webjars/**",
        "/actuator/**",
        "/index",
        "/index.html",
        "/doc.html",
        "/*.js",
        "/*.css",
        "/*.json",
        "/*.min.js",
        "/*.min.css",
        "/**.js",
        "/**.css",
        "/**.json",
        "/**.min.js",
        "/**.min.css",
        "/component/**",
        "/login/**",
        "/actuator/**",
        "/h2-console/**",
        "/pear.config.json",
        "/pear.config.yml",
        "/admin/css/**",
        "/admin/fonts/**",
        "/admin/js/**",
        "/admin/images/**",
        "/health/**"
    };

    private Environment environment;

    // @Primary
    // @Bean(name = "memberUserDetailsService")
    // public UserDetailsService memberUserDetailsService() {
    //     return new MemberUserDetailsService();
    // }
	//
    // @Bean(name = "sysUserDetailsService")
    // public UserDetailsService sysUserDetailsService() {
    //     return new SysUserDetailsService();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        // builder.userDetailsService(memberUserDetailsService())
        //         .passwordEncoder(passwordEncoder())
        //         .and()
        //         .eraseCredentials(true);
    }

    @Value("${jwk.set.uri}")
    private String jwkSetUri;

    private String exceptionMessage(AuthenticationException exception) {
        String msg = "用户未认证";
        if (exception instanceof AccountExpiredException) {
            msg = "账户过期";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            msg = "用户身份凭证未找到";
        } else if (exception instanceof AuthenticationServiceException) {
            msg = "用户身份认证服务异常";
        } else if (exception instanceof BadCredentialsException) {
            msg = exception.getMessage();
        }

        return msg;
    }

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    AuthenticationEntryPoint authenticationEntryPoint = (request, response, authException) -> {
        LogUtils.error("用户未认证", authException);
        authException.printStackTrace();
        ResponseUtils.fail(response, exceptionMessage(authException));
    };
    AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
        LogUtils.error("用户权限不足", accessDeniedException);
        ResponseUtils.fail(response, ResultEnum.FORBIDDEN);
    };
    AuthenticationFailureHandler authenticationFailureHandler = (request, response, accessDeniedException) -> {
        LogUtils.error("账号或者密码错误", accessDeniedException);
        ResponseUtils.fail(response, "账号或者密码错误");
    };
    AuthenticationSuccessHandler authenticationSuccessHandler = (request, response, authentication) -> {
        LogUtils.error("用户认证成功", authentication);
        ResponseUtils.success(response, jwtTokenGenerator.tokenResponse((UserDetails) authentication.getPrincipal()));
    };

    @Bean
    DelegateClientRegistrationRepository delegateClientRegistrationRepository(
            @Autowired(required = false) OAuth2ClientProperties properties) {
        DelegateClientRegistrationRepository clientRegistrationRepository = new DelegateClientRegistrationRepository();
        if (properties != null) {
            List<ClientRegistration> registrations =
                    new ArrayList<>(OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(properties)
                            .values());
            registrations.forEach(clientRegistrationRepository::addClientRegistration);
        }
        return clientRegistrationRepository;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http, DelegateClientRegistrationRepository delegateClientRegistrationRepository)
            throws Exception {
        http.formLogin(formLoginConfigurer -> {
                    formLoginConfigurer
                            .loginPage("/login")
                            .loginProcessingUrl("/login")
                            .successHandler(new RedirectLoginAuthenticationSuccessHandler())
                            .failureHandler(authenticationFailureHandler)
                            .permitAll()
                            .usernameParameter("username")
                            .passwordParameter("password");
                })
                .userDetailsService(new MemberUserDetailsService())
                .anonymous()
                .and()
                .csrf()
                .disable()
                .logout()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(EndpointRequest.toAnyEndpoint())
                        .permitAll()
                        .requestMatchers(permitAllUrls)
                        .permitAll()
                        .requestMatchers("/webjars/**", "/user/login", "/login-error", "/index")
                        .permitAll()
                        .requestMatchers("/messages/**")
                        .access((authentication, object) -> {
                            // .access("hasAuthority('ADMIN')")
                            return null;
                        })
                        .anyRequest()
                        .authenticated())
                // **************************************资源服务器配置***********************************************
                .oauth2ResourceServer(oauth2ResourceServerCustomizer -> oauth2ResourceServerCustomizer
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .bearerTokenResolver(bearerTokenResolver -> {
                            DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
                            defaultBearerTokenResolver.setAllowFormEncodedBodyParameter(true);
                            defaultBearerTokenResolver.setAllowUriQueryParameter(true);
                            return defaultBearerTokenResolver.resolve(bearerTokenResolver);
                        })
                        .jwt(jwtCustomizer -> jwtCustomizer
                                .decoder(NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
                                        .build())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                // **************************************自定义登录配置***********************************************
                .apply(new LoginFilterSecurityConfigurer<>())
                // 用户+密码登录
                .accountLogin(accountLoginFilterConfigurerCustomizer -> {
                    accountLoginFilterConfigurerCustomizer
                            .successHandler(authenticationSuccessHandler)
                            // 两个登录保持一致
                            .failureHandler(authenticationFailureHandler);
                })
                // 用户+密码+验证码登录
                .accountVerificationLogin(accountVerificationLoginFilterConfigurerCustomizer -> {
                    accountVerificationLoginFilterConfigurerCustomizer
                            .successHandler(authenticationSuccessHandler)
                            // 两个登录保持一致
                            .failureHandler(authenticationFailureHandler);
                })
                // 面部识别登录
                .faceLogin(faceLoginFilterConfigurerCustomizer -> {
                    faceLoginFilterConfigurerCustomizer
                            .successHandler(authenticationSuccessHandler)
                            // 两个登录保持一致
                            .failureHandler(authenticationFailureHandler);
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
                    oneClickLoginConfigurer.oneClickUserDetailsService(new OneClickUserDetailsService() {
                        @Override
                        public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
                            return null;
                        }
                    });
                })
                // 手机扫码登录
                .qrcodeLogin(qrcodeLoginConfigurer -> {
                    qrcodeLoginConfigurer
                            .successHandler(authenticationSuccessHandler)
                            // 两个登录保持一致
                            .failureHandler(authenticationFailureHandler);
                })
                // 手机号码+短信登录
                .phoneLogin(phoneLoginConfigurer ->
                        // 验证码校验 1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
                        phoneLoginConfigurer
                                // 生成JWT 返回  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
                                // 两个登录保持一致
                                .successHandler(authenticationSuccessHandler)
                                // 两个登录保持一致
                                .failureHandler(authenticationFailureHandler))
                // 微信公众号登录
                .mpLogin(mpLoginConfigurer -> {
                    mpLoginConfigurer.mpUserDetailsService(new MpUserDetailsService() {
                        @Override
                        public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
                            return null;
                        }
                    });
                })
                // 小程序登录 同时支持多个小程序
                .miniAppLogin(miniAppLoginConfigurer -> miniAppLoginConfigurer
                        // 生成JWT 返回  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
                        // 两个登录保持一致
                        .successHandler(authenticationSuccessHandler)
                        // 两个登录保持一致
                        .failureHandler(authenticationFailureHandler))
                .and()
                // **************************************oauth2登录配置***********************************************
                .apply(new OAuth2ProviderConfigurer(delegateClientRegistrationRepository))
                // 微信网页授权
                .wechatWebclient("wxcd395c35c45eb823", "75f9a12c82bd24ecac0d37bf1156c749")
                // 企业微信登录
                .workWechatWebLoginclient("wwa70dc5b6e56936e1", "nvzGI4Alp3xxxxxxZUc3TtPtKbnfTEets5W8", "1000005")
                // 微信扫码登录
                .wechatWebLoginclient("wxcd395c35c45eb823", "75f9a12c82bd24ecac0d37bf1156c749")
                .oAuth2LoginConfigurerConsumer(
                        oauth2LoginConfigurer -> oauth2LoginConfigurer
                                // .loginPage("/user/login").failureUrl("/login-error").permitAll()
                                // .loginPage("/login.html").permitAll()
                                // .loginProcessingUrl("/login").permitAll()
                                // .loginProcessingUrl("/form/login/process").permitAll()
                                // 认证成功后的处理器
                                // 登录请求url
                                // .loginProcessingUrl(DEFAULT_FILTER_PROCESSES_URI)
                                // .loginPage("/login")
                                .successHandler(authenticationSuccessHandler)
                                // 认证失败后的处理器
                                .failureHandler(authenticationFailureHandler)
                        // // 配置授权服务器端点信息
                        // .authorizationEndpoint(authorizationEndpointCustomizer ->
                        // 	authorizationEndpointCustomizer
                        // 		// 授权端点的前缀基础url
                        // 		.baseUri(DEFAULT_AUTHORIZATION_REQUEST_BASE_URI)
                        // )
                        // // 配置获取access_token的端点信息
                        // .tokenEndpoint(tokenEndpointCustomizer ->
                        // 	tokenEndpointCustomizer
                        // 		.accessTokenResponseClient(oAuth2AccessTokenResponseClient())
                        // )
                        // //配置获取userInfo的端点信息
                        // .userInfoEndpoint(userInfoEndpointCustomizer ->
                        // 	userInfoEndpointCustomizer
                        // 		.userService(new QQOauth2UserService())
                        // ));

                        );
        return http.build();
    }

    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
