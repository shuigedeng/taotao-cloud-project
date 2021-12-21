package com.taotao.cloud.oauth2.biz.jwt;

import com.taotao.cloud.common.model.SecurityUser;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.util.CollectionUtils;


public class JwtCustomizerServiceImpl implements JwtCustomizer {

	@Override
	public void customizeToken(JwtEncodingContext context) {

		AbstractAuthenticationToken token = null;

		Authentication authenticataion = SecurityContextHolder.getContext().getAuthentication();

		if (authenticataion instanceof OAuth2ClientAuthenticationToken) {
			token = (OAuth2ClientAuthenticationToken) authenticataion;
		}

		if (token != null) {
			if (token.isAuthenticated() && OAuth2TokenType.ACCESS_TOKEN.equals(
				context.getTokenType())) {

				Authentication authentication = context.getPrincipal();

				if (authentication != null) {
					if (authentication instanceof UsernamePasswordAuthenticationToken) {
						SecurityUser principal = (SecurityUser) authentication.getPrincipal();
						Long userId = principal.getUserId();

						Set<String> authorities = principal
							.getAuthorities()
							.stream()
							.map(GrantedAuthority::getAuthority)
							.collect(Collectors.toSet());

						Map<String, Object> userAttributes = new HashMap<>();
						userAttributes.put("userId", userId);

						JwtClaimsSet.Builder jwtClaimSetBuilder = context.getClaims();
						jwtClaimSetBuilder.claim(OAuth2ParameterNames.SCOPE, authorities);
						jwtClaimSetBuilder.claim("USER_ID", principal.getUserId());
						principal.eraseCredentials();
						jwtClaimSetBuilder.claim("DETAILS", principal);
						jwtClaimSetBuilder.claims(claims ->
							claims.putAll(userAttributes)
						);
					}

					if (authentication instanceof OAuth2ClientAuthenticationToken) {
						OAuth2ClientAuthenticationToken OAuth2ClientAuthenticationToken = (OAuth2ClientAuthenticationToken) authentication;
						Map<String, Object> additionalParameters = OAuth2ClientAuthenticationToken.getAdditionalParameters();

						// customize the token according to your need for this kind of authentication
						if (!CollectionUtils.isEmpty(additionalParameters)) {

						}
					}
				}
			}
		}
	}
}
