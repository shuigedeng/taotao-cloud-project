///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.oauth2.api.oauth2_server.component;
//
//import cn.hutool.core.util.StrUtil;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import com.fasterxml.jackson.databind.ser.std.StdSerializer;
//import com.nimbusds.oauth2.sdk.util.StringUtils;
//import com.taotao.cloud.common.constant.CommonConstant;
//import com.taotao.cloud.common.enums.ResultEnum;
//import com.taotao.cloud.common.utils.IdGeneratorUtil;
//import com.taotao.cloud.common.utils.LogUtil;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.Set;
//import org.slf4j.MDC;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2RefreshToken;
//import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.CompositeTokenGranter;
//import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
//import org.springframework.security.oauth2.provider.TokenGranter;
//import org.springframework.security.oauth2.provider.TokenRequest;
//import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
//import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
//import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
//import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
//import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
//import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
//import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
//import org.springframework.util.CollectionUtils;
//
///**
// * 自定义的 {@link TokenGranter}<br>
// * 为了自定义令牌的返回结构 (把令牌信息包装到通用结构的 data 属性内).
// *
// * <pre>
// * {
// *     "status": 200,
// *     "timestamp": "2020-06-23 17:42:12",
// *     "message": "OK",
// *     "data": "{\"additionalInformation\":{},\"expiration\":1592905452867,\"expired\":false,\"expiresIn\":119,\"scope\":[\"ACCESS_RESOURCE\"],\"tokenType\":\"bearer\",\"value\":\"81b0d28f-f517-4521-b549-20a10aab0392\"}"
// * }
// * </pre>
// *
// * @author shuigedeng
// * @since 2020-06-23 14:52
// * @see CompositeTokenGranter
// * @version 1.0.0
// */
//public class TokenGranterComponent implements TokenGranter {
//
//    /**
//     * 委托 {@link CompositeTokenGranter}
//     */
//    private final CompositeTokenGranter delegate;
//
//    /**
//     * Description: 构建委托对象 {@link CompositeTokenGranter}
//     *
//     * @param configurer            {@link AuthorizationServerEndpointsConfigurer}
//     * @param authenticationManager {@link AuthenticationManager}, grantType 为 password 时需要
//     * @author shuigedeng
//     * @since 2020-06-23 15:28:24
//     */
//    public TokenGranterComponent(AuthorizationServerEndpointsConfigurer configurer, AuthenticationManager authenticationManager) {
//        final ClientDetailsService clientDetailsService = configurer.getClientDetailsService();
//        final AuthorizationServerTokenServices tokenServices = configurer.getTokenServices();
//        final AuthorizationCodeServices authorizationCodeServices = configurer.getAuthorizationCodeServices();
//        final OAuth2RequestFactory requestFactory = configurer.getOAuth2RequestFactory();
//
//        this.delegate = new CompositeTokenGranter(Arrays.asList(
//                new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory),
//                new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory),
//                new ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory),
//                new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory),
//                new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory)
//        ));
//    }
//
//    @Override
//    public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
//        LogUtil.debug("Custom TokenGranter :: grant token with type {0}", grantType);
//
//        // 如果发生异常, 会触发 WebResponseExceptionTranslator
//        final OAuth2AccessToken oAuth2AccessToken =
//                Optional.ofNullable(delegate.grant(grantType, tokenRequest)).orElseThrow(() -> new UnsupportedGrantTypeException("不支持的授权类型!"));
//        return new CustomOAuth2AccessToken(oAuth2AccessToken);
//    }
//
//    /**
//     * 自定义 {@link CustomOAuth2AccessToken}
//     */
//    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = CustomOAuth2AccessTokenJackson2Serializer.class)
//    public static final class CustomOAuth2AccessToken extends DefaultOAuth2AccessToken {
//
//        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//
//        public CustomOAuth2AccessToken(OAuth2AccessToken accessToken) {
//            super(accessToken);
//        }
//
//        /**
//         * Description: 序列化 {@link OAuth2AccessToken}
//         *
//         * @return 形如 { "access_token": "aa5a459e-4da6-41a6-bf67-6b8e50c7663b", "token_type": "bearer", "expires_in": 119, "scope": "read_scope" } 的字符串
//         */
//        @SneakyThrows
//        public Map<Object, Object> tokenSerialize() {
//            final LinkedHashMap<Object, Object> map = new LinkedHashMap<>(5);
//            map.put(OAuth2AccessToken.ACCESS_TOKEN, this.getValue());
//            map.put(OAuth2AccessToken.TOKEN_TYPE, this.getTokenType());
//
//            final OAuth2RefreshToken refreshToken = this.getRefreshToken();
//            if (Objects.nonNull(refreshToken)) {
//                map.put(OAuth2AccessToken.REFRESH_TOKEN, refreshToken.getValue());
//            }
//
//            final Date expiration = this.getExpiration();
//            if (Objects.nonNull(expiration)) {
//                map.put(OAuth2AccessToken.EXPIRES_IN, (expiration.getTime() - System.currentTimeMillis()) / 1000);
//            }
//
//            final Set<String> scopes = this.getScope();
//            if (!CollectionUtils.isEmpty(scopes)) {
//                final StringBuffer buffer = new StringBuffer();
//                scopes.stream().filter(StringUtils::isNotBlank).forEach(scope -> buffer.append(scope).append(" "));
//                map.put(OAuth2AccessToken.SCOPE, buffer.substring(0, buffer.length() - 1));
//            }
//
//            final Map<String, Object> additionalInformation = this.getAdditionalInformation();
//            if (!CollectionUtils.isEmpty(additionalInformation)) {
//                additionalInformation.forEach((key, value) -> map.put(key, additionalInformation.get(key)));
//            }
//
//            return map;
//        }
//    }
//
//    /**
//     * 自定义 {@link CustomOAuth2AccessToken} 的序列化器
//     */
//    private static final class CustomOAuth2AccessTokenJackson2Serializer extends StdSerializer<CustomOAuth2AccessToken> {
//
//        protected CustomOAuth2AccessTokenJackson2Serializer() {
//            super(CustomOAuth2AccessToken.class);
//        }
//
//        @Override
//        public void serialize(CustomOAuth2AccessToken oAuth2AccessToken, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//            jsonGenerator.writeStartObject();
//            jsonGenerator.writeObjectField("code", ResultEnum.SUCCESS.getCode());
//            jsonGenerator.writeObjectField("message", ResultEnum.SUCCESS.getMessage());
//            jsonGenerator.writeObjectField("timestamp", CommonConstant.DATETIME_FORMATTER.format(LocalDateTime.now()));
//			jsonGenerator.writeObjectField("type", CommonConstant.ERROR);
//			jsonGenerator.writeObjectField("requestId", StrUtil.isNotBlank(MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID)) ? MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID) : IdGeneratorUtil.getIdStr());
//            jsonGenerator.writeObjectField("data", oAuth2AccessToken.tokenSerialize());
//            jsonGenerator.writeEndObject();
//        }
//    }
//}
