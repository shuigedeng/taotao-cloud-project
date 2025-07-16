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

package com.taotao.cloud.auth.biz.authentication.token;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;

/**
 * jwt token generator
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:25:46
 */
public class JwtTokenGeneratorImpl implements JwtTokenGenerator {

    /**
     * jwk来源
     */
    private final JWKSource<SecurityContext> jwkSource;

    /**
     * jwt令牌发电机实现类
     *
     * @param jwkSource jwk来源
     * @return
     * @since 2023-07-10 17:25:47
     */
    public JwtTokenGeneratorImpl(JWKSource<SecurityContext> jwkSource) {
        this.jwkSource = jwkSource;
    }

    /**
     * 令牌响应
     *
     * @param userDetails 用户详细信息
     * @return {@link OAuth2AccessTokenResponse }
     * @since 2023-07-10 17:25:47
     */
    @Override
    public OAuth2AccessTokenResponse tokenResponse(UserDetails userDetails) {
        // 使用HS256算法签名，PRIVATE_KEY为签名密钥 //生成签名密钥
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).type("JWT").build();

        Instant issuedAt = Clock.system(ZoneId.of("Asia/Shanghai")).instant();

        Set<String> scopes =
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());

        Instant expiresAt = issuedAt.plusSeconds(5 * 60 * 60);
        JwtClaimsSet claimsSet =
                JwtClaimsSet.builder()
                        // JWT的签发主体
                        .issuer("https://blog.taotaocloud.top/")
                        // JWT的所有者
                        .subject(userDetails.getUsername())
                        // JWT的过期时间
                        .expiresAt(expiresAt)
                        // JWT的接收对象
                        .audience(Arrays.asList("client1", "client2"))
                        // JWT的签发时间
                        .issuedAt(issuedAt)
                        // 自定义有效载荷部分
                        .claim("scope", scopes)
                        .build();

        Jwt jwt =
                new NimbusJwtEncoder(jwkSource)
                        .encode(JwtEncoderParameters.from(jwsHeader, claimsSet));

        return OAuth2AccessTokenResponse.withToken(jwt.getTokenValue())
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(expiresAt.getEpochSecond())
                .scopes(scopes)
                .refreshToken(UUID.randomUUID().toString())
                .build();
    }

    /**
     * 社交令牌响应
     *
     * @param oAuth2User o auth2用户
     * @return {@link OAuth2AccessTokenResponse }
     * @since 2023-07-10 17:25:47
     */
    @Override
    public OAuth2AccessTokenResponse socialTokenResponse(OAuth2User oAuth2User) {
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).type("JWT").build();

        Instant issuedAt = Clock.system(ZoneId.of("Asia/Shanghai")).instant();

        Set<String> scopes =
                oAuth2User.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());

        Instant expiresAt = issuedAt.plusSeconds(5 * 60 * 60);
        JwtClaimsSet claimsSet =
                JwtClaimsSet.builder()
                        .issuer("https://blog.taotaocloud.top/")
                        .subject(oAuth2User.getName())
                        .expiresAt(expiresAt)
                        .audience(Arrays.asList("client1", "client2"))
                        .issuedAt(issuedAt)
                        .claim("scope", scopes)
                        .build();

        Jwt jwt =
                new NimbusJwtEncoder(jwkSource)
                        .encode(JwtEncoderParameters.from(jwsHeader, claimsSet));

        return OAuth2AccessTokenResponse.withToken(jwt.getTokenValue())
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(expiresAt.getEpochSecond())
                .scopes(scopes)
                .refreshToken(UUID.randomUUID().toString())
                .build();
    }
}
