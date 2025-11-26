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

package com.taotao.cloud.gateway.authentication;

import com.taotao.boot.security.spring.constants.BaseConstants;
import com.taotao.boot.security.spring.core.authority.TtcGrantedAuthority;
import com.taotao.boot.security.spring.autoconfigure.OAuth2EndpointProperties;
import java.net.URI;
import java.time.Instant;
import java.util.*;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

/**
 * <p>Description: 自定义 OpaqueTokenIntrospector </p>
 * <p>
 * 解决默认的 OpaqueTokenIntrospector 使用 Scope 作为权限的问题。
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 10:00:05
 */
public class ReactiveSecurityOpaqueTokenIntrospector implements ReactiveOpaqueTokenIntrospector {

    /**
     * 字符串对象映射
     */
    private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP =
            new ParameterizedTypeReference<>() {};

    /**
     * 日志记录器
     */
	private static final Logger logger = LoggerFactory.getLogger(ReactiveSecurityOpaqueTokenIntrospector.class);
    /**
     * 其他操作
     */
    private final RestOperations restOperations;

    /**
     * 请求实体转换器
     */
    private Converter<String, RequestEntity<?>> requestEntityConverter;

    /**
     * 希罗多德内省不透明令牌
     *
     * @param endpointProperties       端点属性
     * @param resourceServerProperties 资源服务器属性
     * @since 2023-07-04 10:00:05
     */
    public ReactiveSecurityOpaqueTokenIntrospector(
            OAuth2EndpointProperties endpointProperties,
            OAuth2ResourceServerProperties resourceServerProperties) {
        this(
                getIntrospectionUri(endpointProperties, resourceServerProperties),
                resourceServerProperties.getOpaquetoken().getClientId(),
                resourceServerProperties.getOpaquetoken().getClientSecret());
    }

    /**
     * Creates a {@code OpaqueTokenAuthenticationProvider} with the provided parameters
     *
     * @param introspectionUri The introspection endpoint uri
     * @param clientId         The client id authorized to introspect
     * @param clientSecret     The client's secret
     * @since 2023-07-04 10:00:05
     */
    public ReactiveSecurityOpaqueTokenIntrospector(
            String introspectionUri, String clientId, String clientSecret) {
        Assert.notNull(introspectionUri, "introspectionUri cannot be null");
        Assert.notNull(clientId, "clientId cannot be null");
        Assert.notNull(clientSecret, "clientSecret cannot be null");
        this.requestEntityConverter =
                this.defaultRequestEntityConverter(URI.create(introspectionUri));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate
                .getInterceptors()
                .add(new BasicAuthenticationInterceptor(clientId, clientSecret));
        this.restOperations = restTemplate;
    }

    /**
     * Creates a {@code OpaqueTokenAuthenticationProvider} with the provided parameters
     * <p>
     * The given {@link RestOperations} should perform its own client authentication
     * against the introspection endpoint.
     *
     * @param introspectionUri The introspection endpoint uri
     * @param restOperations   The client for performing the introspection request
     * @since 2023-07-04 10:00:05
     */
    public ReactiveSecurityOpaqueTokenIntrospector(
            String introspectionUri, RestOperations restOperations) {
        Assert.notNull(introspectionUri, "introspectionUri cannot be null");
        Assert.notNull(restOperations, "restOperations cannot be null");
        this.requestEntityConverter =
                this.defaultRequestEntityConverter(URI.create(introspectionUri));
        this.restOperations = restOperations;
    }

    /**
     * 得到反省uri
     *
     * @param endpointProperties       端点属性
     * @param resourceServerProperties 资源服务器属性
     * @return {@link String }
     * @since 2023-07-04 10:00:05
     */
    private static String getIntrospectionUri(
            OAuth2EndpointProperties endpointProperties,
            OAuth2ResourceServerProperties resourceServerProperties) {
        String introspectionUri = endpointProperties.getTokenIntrospectionUri();
        String configIntrospectionUri =
                resourceServerProperties.getOpaquetoken().getIntrospectionUri();
        if (StringUtils.isNotBlank(configIntrospectionUri)) {
            introspectionUri = configIntrospectionUri;
        }
        return introspectionUri;
    }

    /**
     * 默认请求实体转换器
     *
     * @param introspectionUri 内省uri
     * @return {@link Converter }<{@link String }, {@link RequestEntity }<{@link ? }>>
     * @since 2023-07-04 10:00:05
     */
    private Converter<String, RequestEntity<?>> defaultRequestEntityConverter(
            URI introspectionUri) {
        return (token) -> {
            HttpHeaders headers = requestHeaders();
            MultiValueMap<String, String> body = requestBody(token);
            return new RequestEntity<>(body, headers, HttpMethod.POST, introspectionUri);
        };
    }

    /**
     * 请求头
     *
     * @return {@link HttpHeaders }
     * @since 2023-07-04 10:00:05
     */
    private HttpHeaders requestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    /**
     * 请求体
     *
     * @param token 令牌
     * @return {@link MultiValueMap }<{@link String }, {@link String }>
     * @since 2023-07-04 10:00:06
     */
    private MultiValueMap<String, String> requestBody(String token) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);
        return body;
    }

    /**
     * 反省
     *
     * @param token 令牌
     * @return {@link OAuth2AuthenticatedPrincipal }
     * @since 2023-07-04 10:00:06
     */
    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
        RequestEntity<?> requestEntity = this.requestEntityConverter.convert(token);
        if (requestEntity == null) {
            throw new OAuth2IntrospectionException("requestEntityConverter returned a null entity");
        }
        ResponseEntity<Map<String, Object>> responseEntity = makeRequest(requestEntity);
        Map<String, Object> claims = adaptToNimbusResponse(responseEntity);
        return Mono.just(convertClaimsSet(claims));
    }

    /**
     * Sets the {@link Converter} used for converting the OAuth 2.0 access token to a
     * {@link RequestEntity} representation of the OAuth 2.0 token introspection request.
     *
     * @param requestEntityConverter the
     * @since 2023-07-04 10:00:06
     */
    public void setRequestEntityConverter(
            Converter<String, RequestEntity<?>> requestEntityConverter) {
        Assert.notNull(requestEntityConverter, "requestEntityConverter cannot be null");
        this.requestEntityConverter = requestEntityConverter;
    }

    /**
     * 使请求
     *
     * @param requestEntity 请求实体
     * @return {@link ResponseEntity }<{@link Map }<{@link String }, {@link Object }>>
     * @since 2023-07-04 10:00:06
     */
    private ResponseEntity<Map<String, Object>> makeRequest(RequestEntity<?> requestEntity) {
        try {
            return this.restOperations.exchange(requestEntity, STRING_OBJECT_MAP);
        } catch (Exception ex) {
            throw new OAuth2IntrospectionException(ex.getMessage(), ex);
        }
    }

    /**
     * 适应灵气反应
     *
     * @param responseEntity 响应实体
     * @return {@link Map }<{@link String }, {@link Object }>
     * @since 2023-07-04 10:00:06
     */
    private Map<String, Object> adaptToNimbusResponse(
            ResponseEntity<Map<String, Object>> responseEntity) {
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new OAuth2IntrospectionException(
                    "Introspection endpoint responded with " + responseEntity.getStatusCode());
        }
        Map<String, Object> claims = responseEntity.getBody();
        // relying solely on the authorization server to validate this token (not checking
        // 'exp', for example)
        if (claims == null) {
            return Collections.emptyMap();
        }

        boolean active =
                (boolean)
                        claims.compute(
                                OAuth2TokenIntrospectionClaimNames.ACTIVE,
                                (k, v) -> {
                                    if (v instanceof String) {
                                        return Boolean.parseBoolean((String) v);
                                    }
                                    if (v instanceof Boolean) {
                                        return v;
                                    }
                                    return false;
                                });
        if (!active) {
            this.logger.trace("Did not validate token since it is inactive");
            throw new BadOpaqueTokenException("Provided token isn't active");
        }
        return claims;
    }

    /**
     * 转换要求设置
     *
     * @param claims 索赔
     * @return {@link OAuth2AuthenticatedPrincipal }
     * @since 2023-07-04 10:00:06
     */
    private OAuth2AuthenticatedPrincipal convertClaimsSet(Map<String, Object> claims) {
        claims.computeIfPresent(
                OAuth2TokenIntrospectionClaimNames.AUD,
                (k, v) -> {
                    if (v instanceof String) {
                        return Collections.singletonList(v);
                    }
                    return v;
                });
        claims.computeIfPresent(
                OAuth2TokenIntrospectionClaimNames.CLIENT_ID, (k, v) -> v.toString());
        claims.computeIfPresent(
                OAuth2TokenIntrospectionClaimNames.EXP,
                (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        claims.computeIfPresent(
                OAuth2TokenIntrospectionClaimNames.IAT,
                (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        // RFC-7662 page 7 directs users to RFC-7519 for defining the values of these
        // issuer fields.
        // https://datatracker.ietf.org/doc/html/rfc7662#page-7
        //
        // RFC-7519 page 9 defines issuer fields as being 'case-sensitive' strings
        // containing
        // a 'StringOrURI', which is defined on page 5 as being any string, but strings
        // containing ':'
        // should be treated as valid URIs.
        // https://datatracker.ietf.org/doc/html/rfc7519#section-2
        //
        // It is not defined however as to whether-or-not normalized URIs should be
        // treated as the same literal
        // value. It only defines validation itself, so to avoid potential ambiguity or
        // unwanted side effects that
        // may be awkward to debug, we do not want to manipulate this value. Previous
        // versions of Spring Security
        // would *only* allow valid URLs, which is not what we wish to achieve here.
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.ISS, (k, v) -> v.toString());
        claims.computeIfPresent(
                OAuth2TokenIntrospectionClaimNames.NBF,
                (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.SCOPE, (k, v) -> v.toString());

        claims.computeIfPresent(
                BaseConstants.AUTHORITIES,
                (k, v) -> {
                    if (v instanceof ArrayList) {
                        @SuppressWarnings("unchecked")
                        List<String> values = (List<String>) v;
                        values.forEach(value -> authorities.add(new TtcGrantedAuthority(value)));
                    }
                    return v;
                });
        return new OAuth2IntrospectionAuthenticatedPrincipal(claims, authorities);
    }
}
