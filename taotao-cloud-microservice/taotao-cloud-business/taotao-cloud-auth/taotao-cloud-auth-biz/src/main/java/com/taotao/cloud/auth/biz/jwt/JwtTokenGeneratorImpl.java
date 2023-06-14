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

package com.taotao.cloud.auth.biz.jwt;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.taotao.cloud.common.utils.context.ContextUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * jwt token generator
 *
 * @author n1
 * @since 2021 /3/27 13:33
 */
public class JwtTokenGeneratorImpl implements JwtTokenGenerator {

	@Override
	public OAuth2AccessTokenResponse tokenResponse(UserDetails userDetails) {
		JwsHeader jwsHeader = JwsHeader
			.with(SignatureAlgorithm.RS256)
			.type("JWT")
			.build();

		Instant issuedAt = Clock
			.system(ZoneId.of("Asia/Shanghai"))
			.instant();
		Set<String> scopes = userDetails
			.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toSet());

		Instant expiresAt = issuedAt.plusSeconds(5 * 60);
		JwtClaimsSet claimsSet = JwtClaimsSet
			.builder()
			.issuer("https://felord.cn")
			.subject(userDetails.getUsername())
			.expiresAt(expiresAt)
			.audience(Arrays.asList("client1", "client2"))
			.issuedAt(issuedAt)
			.claim("scope", scopes)
			.build();

		JWKSource jwkSource = ContextUtils.getBean(JWKSource.class, false);
		Jwt jwt = new NimbusJwtEncoder(jwkSource)
			.encode(JwtEncoderParameters.from(jwsHeader, claimsSet));

		return OAuth2AccessTokenResponse
			.withToken(jwt.getTokenValue())
			.tokenType(OAuth2AccessToken.TokenType.BEARER)
			.expiresIn(expiresAt.getEpochSecond())
			.scopes(scopes)
			.refreshToken(UUID.randomUUID().toString())
			.build();
	}
}
