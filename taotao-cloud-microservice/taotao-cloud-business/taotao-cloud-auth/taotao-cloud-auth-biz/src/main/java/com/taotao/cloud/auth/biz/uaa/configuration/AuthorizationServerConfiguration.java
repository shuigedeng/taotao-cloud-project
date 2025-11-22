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

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.utils.io.ResourceUtils;
import com.taotao.boot.common.utils.servlet.ResponseUtils;
import com.taotao.boot.security.spring.constants.DefaultConstants;
import com.taotao.boot.security.spring.enums.Certificate;
import com.taotao.boot.security.spring.oauth2.token1.SecurityTokenStrategyConfigurer;
import com.taotao.boot.security.spring.autoconfigure.OAuth2AuthorizationProperties;
import com.taotao.boot.security.spring.autoconfigure.OAuth2EndpointProperties;
import com.taotao.cloud.auth.biz.authentication.device.DeviceClientAuthenticationConverter;
import com.taotao.cloud.auth.biz.authentication.device.DeviceClientAuthenticationProvider;
import com.taotao.cloud.auth.biz.authentication.event.DefaultOAuth2AuthenticationEventPublisher;
import com.taotao.cloud.auth.biz.authentication.oidc.TtcOidcUserInfoMapper;
import com.taotao.cloud.auth.biz.authentication.processor.HttpCryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.auth.biz.authentication.token.TtcJwtTokenCustomizer;
import com.taotao.cloud.auth.biz.authentication.token.TtcOpaqueTokenCustomizer;
import com.taotao.cloud.auth.biz.authentication.utils.OAuth2ConfigurerUtils;
import com.taotao.cloud.auth.biz.management.processor.ClientDetailsService;
import com.taotao.cloud.auth.biz.management.response.OAuth2AccessTokenResponseHandler;
import com.taotao.cloud.auth.biz.management.response.OAuth2AuthenticationFailureResponseHandler;
import com.taotao.cloud.auth.biz.management.response.OAuth2DeviceVerificationResponseHandler;
import com.taotao.cloud.auth.biz.management.response.OidcClientRegistrationResponseHandler;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.ArrayUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2DeviceAuthorizationRequestAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2DeviceCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

/**
 * https://java.cunzaima.cn//spring-authorization-server-1.2.2-zh/core-model-components.html
 *
 * <p>认证服务器配置 </p>
 * <p>
 * 1. 权限核心处理 {@link org.springframework.security.web.access.intercept.FilterSecurityInterceptor} 2.
 * 默认的权限判断 {@link org.springframework.security.access.vote.AffirmativeBased} 3. 模式决策
 * {@link org.springframework.security.authentication.ProviderManager}
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 10:30:08
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {

    private static final Logger log =
            LoggerFactory.getLogger(AuthorizationServerConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("SDK [OAuth2 Authorization Server] Auto Configure.");
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity httpSecurity,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService,
            ClientDetailsService clientDetailsService,
            HttpCryptoProcessor httpCryptoProcessor,
            SecurityTokenStrategyConfigurer ttcTokenStrategyConfigurer,
            OAuth2FormLoginUrlConfigurer formLoginUrlConfigurer,
            OAuth2AuthenticationProperties authenticationProperties,
            OAuth2DeviceVerificationResponseHandler deviceVerificationResponseHandler,
            OidcClientRegistrationResponseHandler clientRegistrationResponseHandler,
            OAuth2EndpointProperties auth2EndpointProperties,
            RegisteredClientRepository registeredClientRepository)
            throws Exception {

        log.info("Core [Authorization Server Security Filter Chain] Auto Configure.");

        // 实现授权码模式使用前后端分离的登录页面   使用redis存储、读取登录的认证信息
        // httpSecurity.securityContext(securityContextCustomizer -> {
        //	securityContextCustomizer.securityContextRepository(redisSecurityContextRepository)
        // });

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        httpSecurity.with(authorizationServerConfigurer, Customizer.withDefaults());

        OAuth2AuthenticationFailureResponseHandler errorResponseHandler =
                new OAuth2AuthenticationFailureResponseHandler();

        // 用于管理新建和现有客户端的RegisteredClientRepository（必需）
        // authorizationServerConfigurer.registeredClientRepository(registeredClientRepository);

        // 用于管理新建和现有授权的OAuth2AuthorizationService。
        // authorizationServerConfigurer.authorizationService()

        // 用于管理新建和现有授权同意的OAuth2AuthorizationConsentService。
        // authorizationServerConfigurer.authorizationConsentService()

        // 用于自定义配置OAuth2授权服务器的AuthorizationServerSettings（必需）
        // authorizationServerConfigurer.authorizationServerSettings()

        // 用于生成OAuth2授权服务器支持的令牌的OAuth2TokenGenerator。
        // authorizationServerConfigurer.tokenGenerator()

        // 用于OAuth2授权服务器元数据端点的配置器。
        // authorizationServerConfigurer.authorizationServerMetadataEndpoint()

        // 用于OpenID Connect 1.0提供者配置端点的配置器。。
        // authorizationServerConfigurer.providerConfigurationEndpoint()

        // 用于OpenID Connect 1.0注销端点的配置器。
        // authorizationServerConfigurer.logoutEndpoint()

        // 用于OpenID Connect 1.0用户信息端点的配置器。
        // authorizationServerConfigurer.userInfoEndpoint()

        // 用于OpenID Connect 1.0客户端注册端点的配置器。
        // authorizationServerConfigurer.clientRegistrationEndpoint()

        // 用于OAuth2令牌端点的配置器。
        authorizationServerConfigurer.tokenEndpoint(
                tokenEndpointCustomizer -> {
                    AuthenticationConverter authenticationConverter =
                            new DelegatingAuthenticationConverter(
                                    Arrays.asList(
                                            new OAuth2AuthorizationCodeAuthenticationConverter(),
                                            new OAuth2RefreshTokenAuthenticationConverter(),
                                            new OAuth2ClientCredentialsAuthenticationConverter(),
                                            new OAuth2DeviceCodeAuthenticationConverter(),
                                            new OAuth2DeviceAuthorizationRequestAuthenticationConverter(),
                                            new OAuth2ResourceOwnerPasswordAuthenticationConverter(
                                                    httpCryptoProcessor),
                                            new OAuth2SocialCredentialsAuthenticationConverter(
                                                    httpCryptoProcessor)));

                    tokenEndpointCustomizer
                            .accessTokenRequestConverter(authenticationConverter)
                            .errorResponseHandler(errorResponseHandler)
                            .accessTokenResponseHandler(
                                    new OAuth2AccessTokenResponseHandler(httpCryptoProcessor));
                });

        // 用于OAuth2令牌内省端点的配置器
        authorizationServerConfigurer.tokenIntrospectionEndpoint(
                tokenIntrospectionEndpointCustomizer -> {
                    tokenIntrospectionEndpointCustomizer.errorResponseHandler(errorResponseHandler);
                });

        // 用于OAuth2令牌撤销端点的配置器。
        authorizationServerConfigurer.tokenRevocationEndpoint(
                tokenRevocationEndpointCustomizer -> {
                    tokenRevocationEndpointCustomizer.errorResponseHandler(errorResponseHandler);
                });

        // 用于OAuth2授权端点的配置器。
        authorizationServerConfigurer.authorizationEndpoint(
                authorizationEndpointCustomizer -> {
                    authorizationEndpointCustomizer
                            .authorizationResponseHandler(this::sendAuthorizationResponse)
                            .errorResponseHandler(errorResponseHandler)
                            .consentPage(DefaultConstants.AUTHORIZATION_CONSENT_URI);
                });

        // 新建设备码converter和provider
        DeviceClientAuthenticationConverter deviceClientAuthenticationConverter =
                new DeviceClientAuthenticationConverter(
                        auth2EndpointProperties.getDeviceAuthorizationEndpoint());
        DeviceClientAuthenticationProvider deviceClientAuthenticationProvider =
                new DeviceClientAuthenticationProvider(registeredClientRepository);

        // 用于配置OAuth2客户端认证的配置器。
        authorizationServerConfigurer.clientAuthentication(
                clientAuthenticationCustomizer -> {
                    // 客户端认证添加设备码的converter和provider
                    clientAuthenticationCustomizer
                            .errorResponseHandler(errorResponseHandler)
                            .authenticationConverter(deviceClientAuthenticationConverter)
                            .authenticationProvider(deviceClientAuthenticationProvider)
                            .errorResponseHandler(errorResponseHandler);
                });

        // 用于OAuth2设备授权端点的配置器。 开启设备请求相关端点
        authorizationServerConfigurer.deviceAuthorizationEndpoint(
                deviceAuthorizationEndpointCustomizer -> {
                    deviceAuthorizationEndpointCustomizer
                            .errorResponseHandler(errorResponseHandler)
                            .verificationUri(DefaultConstants.DEVICE_ACTIVATION_URI);
                });

        // 用于OAuth2设备验证端点的配置器。
        authorizationServerConfigurer.deviceVerificationEndpoint(
                deviceVerificationEndpointCustomizer -> {
                    deviceVerificationEndpointCustomizer
                            .errorResponseHandler(errorResponseHandler)
                            .consentPage(DefaultConstants.AUTHORIZATION_CONSENT_URI)
                            .deviceVerificationResponseHandler(deviceVerificationResponseHandler);
                });

        // 开启OpenId Connect 1.0相关端点
        authorizationServerConfigurer.oidc(
                oidcCustomizer -> {
                    oidcCustomizer
                            .clientRegistrationEndpoint(
                                    clientRegistrationEndpointCustomizer -> {
                                        clientRegistrationEndpointCustomizer
                                                .errorResponseHandler(errorResponseHandler)
                                                .clientRegistrationResponseHandler(
                                                        clientRegistrationResponseHandler);
                                    })
                            .logoutEndpoint(
                                    logoutEndpointCustomizer -> {
                                        // logoutEndpointCustomizer.logoutResponseHandler()
                                    })
                            .userInfoEndpoint(
                                    userInfoEndpointCustomizer -> {
                                        userInfoEndpointCustomizer.userInfoMapper(
                                                new TtcOidcUserInfoMapper());
                                    });
                });

        SessionRegistry sessionRegistry =
                OAuth2ConfigurerUtils.getOptionalBean(httpSecurity, SessionRegistry.class);

        // 使用自定义的 AuthenticationProvider 替换已有 AuthenticationProvider
        authorizationServerConfigurer.withObjectPostProcessor(
                new ObjectPostProcessor<AuthenticationProvider>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <O extends AuthenticationProvider> O postProcess(O object) {
                        OAuth2AuthorizationService authorizationService =
                                OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity);

                        // 自定义(重写)OAuth2AuthorizationCodeAuthenticationProvider
                        if (org.springframework.security.oauth2.server.authorization.authentication
                                .OAuth2AuthorizationCodeAuthenticationProvider.class
                                .isAssignableFrom(object.getClass())) {
                            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator =
                                    OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
                            OAuth2AuthorizationCodeAuthenticationProvider provider =
                                    new OAuth2AuthorizationCodeAuthenticationProvider(
                                            authorizationService, tokenGenerator);
                            provider.setSessionRegistry(sessionRegistry);
                            log.info(
                                    "Custom OAuth2AuthorizationCodeAuthenticationProvider is in effect!");
                            return (O) provider;
                        }

                        // 自定义(重写)OAuth2ClientCredentialsAuthenticationProvider
                        if (org.springframework.security.oauth2.server.authorization.authentication
                                .OAuth2ClientCredentialsAuthenticationProvider.class
                                .isAssignableFrom(object.getClass())) {
                            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator =
                                    OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
                            OAuth2ClientCredentialsAuthenticationProvider provider =
                                    new OAuth2ClientCredentialsAuthenticationProvider(
                                            authorizationService,
                                            tokenGenerator,
                                            clientDetailsService);
                            log.info(
                                    "Custom OAuth2ClientCredentialsAuthenticationProvider is in effect!");
                            return (O) provider;
                        }
                        return object;
                    }
                });

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        // 仅拦截 OAuth2 Authorization Server 的相关 endpoint
        httpSecurity
                .securityMatcher(endpointsMatcher)
                // 开启请求认证
                .authorizeHttpRequests(
                        authorizeHttpRequestsCustomizer -> {
                            authorizeHttpRequestsCustomizer.anyRequest().authenticated();
                        })
                // 禁用对 OAuth2 Authorization Server 相关 endpoint 的 CSRF 防御
                .csrf(
                        csrfCustomizer -> {
                            csrfCustomizer.ignoringRequestMatchers(endpointsMatcher);
                        })
                .oauth2ResourceServer(ttcTokenStrategyConfigurer::from);

        // 这里增加 DefaultAuthenticationEventPublisher 配置，是为了解决 ProviderManager
        // 在初次使用时，外部定义DefaultAuthenticationEventPublisher 不会注入问题
        // 外部注入DefaultAuthenticationEventPublisher是标准配置方法，两处都保留是为了保险，还需要深入研究才能决定去掉哪个。
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        ApplicationContext applicationContext =
                httpSecurity.getSharedObject(ApplicationContext.class);
        authenticationManagerBuilder.authenticationEventPublisher(
                new DefaultOAuth2AuthenticationEventPublisher(applicationContext));

        // 增加新的、自定义 OAuth2 Granter
        OAuth2AuthorizationService authorizationService =
                OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity);
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator =
                OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);

        OAuth2ResourceOwnerPasswordAuthenticationProvider
                resourceOwnerPasswordAuthenticationProvider =
                        new OAuth2ResourceOwnerPasswordAuthenticationProvider(
                                authorizationService,
                                tokenGenerator,
                                userDetailsService,
                                authenticationProperties);
        resourceOwnerPasswordAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        resourceOwnerPasswordAuthenticationProvider.setSessionRegistry(sessionRegistry);
        httpSecurity.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);

        OAuth2SocialCredentialsAuthenticationProvider socialCredentialsAuthenticationProvider =
                new OAuth2SocialCredentialsAuthenticationProvider(
                        authorizationService,
                        tokenGenerator,
                        userDetailsService,
                        authenticationProperties);
        socialCredentialsAuthenticationProvider.setSessionRegistry(sessionRegistry);
        httpSecurity.authenticationProvider(socialCredentialsAuthenticationProvider);

        httpSecurity.exceptionHandling(
                exceptionHandlingCustomizer -> {
                    // 这里使用自定义的未登录处理，并设置登录地址为前端的登录地址
                    exceptionHandlingCustomizer.defaultAuthenticationEntryPointFor(
                            new SecurityLoginUrlAuthenticationEntryPoint("/login"),
                            createRequestMatcher());
                });

        return httpSecurity
                .formLogin(formLoginUrlConfigurer::from)
                .sessionManagement(Customizer.withDefaults())
                // .addFilterBefore(new MultiTenantFilter(), AuthorizationFilter.class)
                .build();
    }

    private static RequestMatcher createRequestMatcher() {
        MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
        requestMatcher.setIgnoredMediaTypes(Set.of(MediaType.ALL));
        return requestMatcher;
    }

    /**
     * jwk set缓存前缀
     */
    public static final String AUTHORIZATION_JWS_PREFIX_KEY = "authorization_jws";

    @Bean
    public JWKSource<SecurityContext> jwkSource(
            OAuth2AuthorizationProperties authorizationProperties, RedisRepository redisRepository)
            throws NoSuchAlgorithmException, ParseException {
        OAuth2AuthorizationProperties.Jwk jwk = authorizationProperties.getJwk();
        KeyPair keyPair = null;

        // 持久化JWKSource，解决重启后无法解析AccessToken问题
        Object jwkSetCache = redisRepository.get(AUTHORIZATION_JWS_PREFIX_KEY);
        if (ObjUtil.isEmpty(jwkSetCache)) {
            if (jwk.getCertificate() == Certificate.CUSTOM) {
                try {
                    Resource[] resource = ResourceUtils.getResources(jwk.getJksKeyStore());
                    if (ArrayUtils.isNotEmpty(resource)) {
                        KeyStoreKeyFactory keyStoreKeyFactory =
                                new KeyStoreKeyFactory(
                                        resource[0], jwk.getJksStorePassword().toCharArray());
                        keyPair =
                                keyStoreKeyFactory.getKeyPair(
                                        jwk.getJksKeyAlias(),
                                        jwk.getJksKeyPassword().toCharArray());
                    }
                } catch (IOException e) {
                    log.error("Read custom certificate under resource folder error!", e);
                }
            } else {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                keyPair = keyPairGenerator.generateKeyPair();
            }

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAKey rsaKey =
                    new RSAKey.Builder(publicKey)
                            .privateKey(privateKey)
                            .keyID(UUID.randomUUID().toString())
                            .build();
            JWKSet jwkSet = new JWKSet(rsaKey);
            // 转为json字符串
            String jwkSetString = jwkSet.toString(Boolean.FALSE);
            // 存入redis
            redisRepository.set(AUTHORIZATION_JWS_PREFIX_KEY, jwkSetString);
            return new ImmutableJWKSet<>(jwkSet);
            // return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
        }
        // 解析存储的jws
        JWKSet jwkSet = JWKSet.parse((String) jwkSetCache);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * jwt 解码
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    // jwt Token定制器
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        TtcJwtTokenCustomizer ttcJwtTokenCustomizer = new TtcJwtTokenCustomizer();
        log.info("Bean [OAuth2 Jwt Token Customizer] Auto Configure.");
        return ttcJwtTokenCustomizer;
    }

    // @Bean
    // public OAuth2TokenCustomizer<JwtEncodingContext> federatedIdentityIdTokenCustomizer() {
    //	FederatedIdentityIdTokenCustomizer federatedIdentityIdTokenCustomizer = new
    // FederatedIdentityIdTokenCustomizer();
    //	log.info("Bean [OAuth2 federatedIdentityIdTokenCustomizer Token Customizer] Auto
    // Configure.");
    //	return federatedIdentityIdTokenCustomizer;
    // }

    // opaqueToken定制器
    @Bean
    public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> opaqueTokenCustomizer() {
        TtcOpaqueTokenCustomizer ttcOpaqueTokenCustomizer = new TtcOpaqueTokenCustomizer();
        log.info("Bean [OAuth2 Opaque Token Customizer] Auto Configure.");
        return ttcOpaqueTokenCustomizer;
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(
            OAuth2EndpointProperties auth2EndpointProperties) {
        return AuthorizationServerSettings.builder()
                .issuer(auth2EndpointProperties.getIssuerUri())
                .authorizationEndpoint(auth2EndpointProperties.getAuthorizationEndpoint())
                .deviceAuthorizationEndpoint(
                        auth2EndpointProperties.getDeviceAuthorizationEndpoint())
                .deviceVerificationEndpoint(auth2EndpointProperties.getDeviceVerificationEndpoint())
                .tokenEndpoint(auth2EndpointProperties.getAccessTokenEndpoint())
                .tokenIntrospectionEndpoint(auth2EndpointProperties.getTokenIntrospectionEndpoint())
                .tokenRevocationEndpoint(auth2EndpointProperties.getTokenRevocationEndpoint())
                .jwkSetEndpoint(auth2EndpointProperties.getJwkSetEndpoint())
                .oidcLogoutEndpoint(auth2EndpointProperties.getOidcLogoutEndpoint())
                .oidcUserInfoEndpoint(auth2EndpointProperties.getOidcUserInfoEndpoint())
                .oidcClientRegistrationEndpoint(
                        auth2EndpointProperties.getOidcClientRegistrationEndpoint())
                .build();
    }

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private void sendAuthorizationResponse(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication =
                (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
        UriComponentsBuilder uriBuilder =
                UriComponentsBuilder.fromUriString(
                                authorizationCodeRequestAuthentication.getRedirectUri())
                        .queryParam(
                                OAuth2ParameterNames.CODE,
                                authorizationCodeRequestAuthentication
                                        .getAuthorizationCode()
                                        .getTokenValue());
        if (StringUtils.hasText(authorizationCodeRequestAuthentication.getState())) {
            uriBuilder.queryParam(
                    OAuth2ParameterNames.STATE,
                    UriUtils.encode(
                            authorizationCodeRequestAuthentication.getState(),
                            StandardCharsets.UTF_8));
        }
        String redirectUri = uriBuilder.build(true).toUriString();

        if (!isAjaxRequest(request)) {
            this.redirectStrategy.sendRedirect(request, response, redirectUri);
        }
        ResponseUtils.success(response, redirectUri);
    }

    public boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        return "XMLHttpRequest".equalsIgnoreCase(requestedWith);
    }
}
