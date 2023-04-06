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

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.auth.biz.service.CloudJdbcOAuth2AuthorizationConsentService;
import com.taotao.cloud.auth.biz.service.CloudOAuth2AuthorizationService;
import com.taotao.cloud.auth.biz.service.CloudRegisteredClientService;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * AuthorizationServerConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-21 10:24:33
 */
@Configuration
public class AuthorizationServerConfiguration {

    @Value("${oauth2.token.issuer}")
    private String tokenIssuer;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("memberUserDetailsService")
    private UserDetailsService memberUserDetailsService;

    @Autowired
    @Qualifier("sysUserDetailsService")
    private UserDetailsService sysUserDetailsService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http.apply(authorizationServerConfigurer
                .tokenEndpoint(tokenEndpointCustomizer -> tokenEndpointCustomizer
                        .accessTokenRequestConverter(new DelegatingAuthenticationConverter(Arrays.asList(
                                new OAuth2AuthorizationCodeAuthenticationConverter(),
                                new OAuth2RefreshTokenAuthenticationConverter(),
                                new OAuth2ClientCredentialsAuthenticationConverter())))
                        .errorResponseHandler((request, response, authException) -> {
                            LogUtils.error("用户认证失败", authException);
                            ResponseUtils.fail(response, authException.getMessage());
                        }))
                .authorizationEndpoint(authorizationEndpointCustomizer ->
                        authorizationEndpointCustomizer.consentPage("/oauth2/consent"))
                .oidc(oidcCustomizer -> oidcCustomizer.userInfoEndpoint(
                        userInfoEndpointCustomizer -> userInfoEndpointCustomizer.userInfoMapper(userInfoMapper -> {
                            OidcUserInfoAuthenticationToken authentication = userInfoMapper.getAuthentication();
                            JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
                            return new OidcUserInfo(principal.getToken().getClaims());
                        }))));

        RequestMatcher authorizationServerConfigurerEndpointsMatcher =
                authorizationServerConfigurer.getEndpointsMatcher();

        http.securityMatcher(authorizationServerConfigurerEndpointsMatcher)
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerConfigurerEndpointsMatcher))
                .formLogin()
                .and()
                .apply(authorizationServerConfigurer);

        SecurityFilterChain securityFilterChain =
                http.formLogin(Customizer.withDefaults()).build();

        // addCustomOAuth2ResourceOwnerPasswordAuthenticationProvider(http);
        //
        // addCustomOAuth2ResourceOwnerMobileAuthenticationProvider(http);

        return securityFilterChain;
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new CloudRegisteredClientService(jdbcTemplate);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository,
            RedisRepository redisRepository,
            JwtDecoder jwtDecoder) {

        JdbcOAuth2AuthorizationService service = new CloudOAuth2AuthorizationService(
                jdbcTemplate, registeredClientRepository, redisRepository, jwtDecoder);

        JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper =
                new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);

        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

        //// You will need to write the Mixin for your class so Jackson can marshall it.
        // objectMapper.addMixIn(UserAuthority.class, UserAuthorityMixin.class);
        // objectMapper.addMixIn(CloudUserDetails.class, CloudUserDetailsMixin.class);
        // objectMapper.addMixIn(AuditDeletedDate.class, AuditDeletedDateMixin.class);
        // objectMapper.addMixIn(Long.class, LongMixin.class);

        rowMapper.setObjectMapper(objectMapper);
        service.setAuthorizationRowMapper(rowMapper);

        return service;
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(
            JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new CloudJdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder().issuer(tokenIssuer).build();
    }
}
