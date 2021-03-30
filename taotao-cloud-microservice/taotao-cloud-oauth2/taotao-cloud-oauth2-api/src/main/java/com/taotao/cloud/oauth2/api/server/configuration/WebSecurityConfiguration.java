/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.oauth2.api.server.configuration;

import com.taotao.cloud.oauth2.api.server.authentication.TaotaoCloudAuthenticationSecurityConfig;
import com.taotao.cloud.oauth2.api.server.component.Oauth2UserServiceComponent;
import com.taotao.cloud.oauth2.api.server.handler.OauthLogoutSuccessHandler;
import com.taotao.cloud.security.service.IUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/**
 * web安全配置类
 *
 * @author dengtao
 * @since 2020/4/29 20:14
 * @version 1.0.0
 */
@Order(2)
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final PasswordEncoder passwordEncoder;
    private final LogoutHandler oauthLogoutHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final TaotaoCloudAuthenticationSecurityConfig taotaoCloudAuthenticationSecurityConfig;
    private final IUserDetailsService userDetailsService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .antMatchers("/oauth/**", "/auth/**", "/logout/**", "/api/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**", "/api/**").authenticated()
                .and()
                // 通过httpSession保存认证信息
//                .addFilter(new SecurityContextPersistenceFilter())
                .formLogin()
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/authorize")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                .logoutSuccessHandler(new OauthLogoutSuccessHandler())
                .addLogoutHandler(oauthLogoutHandler)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .clearAuthentication(true)
                .and()
                .apply(taotaoCloudAuthenticationSecurityConfig).and()
                .addFilterBefore(new OAuth2AuthenticationProcessingFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .csrf().disable()
                .headers().frameOptions().disable().cacheControl();

        Oauth2UserServiceComponent oauth2UserServiceComponent = new Oauth2UserServiceComponent();
        oauth2UserServiceComponent.setRequestEntityConverter(new CustomOAuth2UserRequestEntityConverter());
        http.oauth2Login(oauth2LoginConfigurer -> oauth2LoginConfigurer
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .loginProcessingUrl("/api/login/oauth2/code/*")
                // 配置授权服务器端点信息
                .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                        // 授权端点的前缀基础url
                        .baseUri("/api/oauth2/authorization"))
                // 配置获取access_token的端点信息
                .tokenEndpoint(tokenEndpointConfig -> tokenEndpointConfig.accessTokenResponseClient(oAuth2AccessTokenResponseClient()))
                // 配置获取userInfo的端点信息
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oauth2UserServiceComponent))
        );

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

    }

    /**
     * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 这个方法是去数据库查询用户的密码，做权限验证
     *
     * @author dengtao
     * @since 2020/4/29 20:15
     */
    @Autowired
    public void config(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }


    /**
     * qq获取access_token返回的结果是类似get请求参数的字符串，无法通过指定Accept请求头来使qq返回特定的响应类型，并且qq返回的access_token
     * 也缺少了必须的token_type字段（不符合oauth2标准的授权码认证流程），spring-security默认远程获取
     * access_token的客户端是{@link DefaultAuthorizationCodeTokenResponseClient}，所以我们需要
     * 自定义{@link QqoAuth2AccessTokenResponseHttpMessageConverter}注入到这个client中来解析qq的access_token响应信息
     *
     * @return {@link DefaultAuthorizationCodeTokenResponseClient} 用来获取access_token的客户端
     * @see <a href="https://www.oauth.com/oauth2-servers/access-tokens/authorization-code-request">authorization-code-request规范</a>
     * @see <a href="https://www.oauth.com/oauth2-servers/access-tokens/access-token-response">access-token-response规范</a>
     * @see <a href="https://wiki.connect.qq.com/%E5%BC%80%E5%8F%91%E6%94%BB%E7%95%A5_server-side">qq开发文档</a>
     */
    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> oAuth2AccessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
        client.setRequestEntityConverter(new CustomOAuth2AuthorizationCodeGrantRequestEntityConverter());
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(),

                // 解析标准的AccessToken响应信息转换器
                new OAuth2AccessTokenResponseHttpMessageConverter(),

                // 解析qq的AccessToken响应信息转换器
                new QqoAuth2AccessTokenResponseHttpMessageConverter(MediaType.TEXT_HTML)));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        client.setRestOperations(restTemplate);

        return client;
    }

    /**
     * 自定义消息转换器来解析qq的access_token响应信息
     *
     * @see OAuth2AccessTokenResponseHttpMessageConverter#readInternal(Class, org.springframework.http.HttpInputMessage)
     * @see OAuth2LoginAuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
     */
    private static class QqoAuth2AccessTokenResponseHttpMessageConverter extends OAuth2AccessTokenResponseHttpMessageConverter {

        public QqoAuth2AccessTokenResponseHttpMessageConverter(MediaType... mediaType) {
            setSupportedMediaTypes(Arrays.asList(mediaType));
        }

        @SneakyThrows
        @Override
        protected OAuth2AccessTokenResponse readInternal(Class<? extends OAuth2AccessTokenResponse> clazz, HttpInputMessage inputMessage) {
            String response = StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
            // log.info("qq的AccessToken响应信息：{}", response);

            // 解析响应信息类似access_token=YOUR_ACCESS_TOKEN&expires_in=3600这样的字符串
            Map<String, String> tokenResponseParameters = Arrays.stream(response.split("&"))
                    .collect(Collectors.toMap(s -> s.split("=")[0], s -> s.split("=")[1]));

            // 手动给qq的access_token响应信息添加token_type字段，spring-security会按照oauth2规范校验返回参数
            tokenResponseParameters.put(OAuth2ParameterNames.TOKEN_TYPE, "bearer");
            return this.tokenResponseConverter.convert(tokenResponseParameters);
        }

        @Override
        protected void writeInternal(OAuth2AccessTokenResponse tokenResponse, HttpOutputMessage outputMessage) {
            throw new UnsupportedOperationException();
        }
    }

    private static class CustomOAuth2AuthorizationCodeGrantRequestEntityConverter extends OAuth2AuthorizationCodeGrantRequestEntityConverter {
        @Override
        public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
            ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
            HttpHeaders headers = OAuth2AuthorizationGrantRequestEntityUtils.getTokenRequestHeaders(clientRegistration);
            MultiValueMap<String, String> formParameters = buildFormParameters(authorizationCodeGrantRequest);
            URI uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getTokenUri())
                    .build()
                    .toUri();

            return new RequestEntity<>(formParameters, headers, HttpMethod.POST, uri);
        }

        private MultiValueMap<String, String> buildFormParameters(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
            ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
            OAuth2AuthorizationExchange authorizationExchange = authorizationCodeGrantRequest.getAuthorizationExchange();

            MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
            formParameters.add(OAuth2ParameterNames.GRANT_TYPE, authorizationCodeGrantRequest.getGrantType().getValue());
            formParameters.add(OAuth2ParameterNames.CODE, authorizationExchange.getAuthorizationResponse().getCode());
            String redirectUri = authorizationExchange.getAuthorizationRequest().getRedirectUri();
            String codeVerifier = authorizationExchange.getAuthorizationRequest().getAttribute(PkceParameterNames.CODE_VERIFIER);
            if (redirectUri != null) {
                formParameters.add(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
            }
            if (!ClientAuthenticationMethod.BASIC.equals(clientRegistration.getClientAuthenticationMethod())) {
                formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
            }
            if (ClientAuthenticationMethod.POST.equals(clientRegistration.getClientAuthenticationMethod())) {
                formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientRegistration.getClientSecret());
            }
            if (codeVerifier != null) {
                formParameters.add(PkceParameterNames.CODE_VERIFIER, codeVerifier);
            }

            return formParameters;
        }
    }

    public static class OAuth2AuthorizationGrantRequestEntityUtils {
        private static final HttpHeaders DEFAULT_TOKEN_REQUEST_HEADERS = getDefaultTokenRequestHeaders();

        static HttpHeaders getTokenRequestHeaders(ClientRegistration clientRegistration) {
            HttpHeaders headers = new HttpHeaders();
            headers.addAll(DEFAULT_TOKEN_REQUEST_HEADERS);
            if (ClientAuthenticationMethod.BASIC.equals(clientRegistration.getClientAuthenticationMethod())) {
                headers.setBasicAuth(clientRegistration.getClientId(), clientRegistration.getClientSecret());
            }
            return headers;
        }

        private static HttpHeaders getDefaultTokenRequestHeaders() {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            final MediaType contentType = MediaType.valueOf(APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
            headers.setContentType(contentType);
            // 为了解决gitee登录 报403错误
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
            return headers;
        }
    }


    public static class CustomOAuth2UserRequestEntityConverter extends OAuth2UserRequestEntityConverter {
        private static final MediaType DEFAULT_CONTENT_TYPE = MediaType.valueOf(APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

        @Override
        public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
            ClientRegistration clientRegistration = userRequest.getClientRegistration();

            HttpMethod httpMethod = HttpMethod.GET;
            if (AuthenticationMethod.FORM.equals(clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod())) {
                httpMethod = HttpMethod.POST;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            // 解决gitee登录 报403错误
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
            URI uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                    .build()
                    .toUri();

            RequestEntity<?> request;
            if (HttpMethod.POST.equals(httpMethod)) {
                headers.setContentType(DEFAULT_CONTENT_TYPE);
                MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
                formParameters.add(OAuth2ParameterNames.ACCESS_TOKEN, userRequest.getAccessToken().getTokenValue());
                request = new RequestEntity<>(formParameters, headers, httpMethod, uri);
            } else {
                headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());
                request = new RequestEntity<>(headers, httpMethod, uri);
            }

            return request;
        }
    }
}

