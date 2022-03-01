package com.taotao.cloud.sys.biz.tools.security.configs.jwt;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private UserDetails principal;
	private String credentials;
	private String token;
	private Claims claims;

	public JwtAuthenticationToken(String token) {
		super(Collections.emptyList());
		this.token = token;
	}

	public JwtAuthenticationToken(UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.token = token;
	}

	@Override
	public void setDetails(Object details) {
		super.setDetails(details);
		this.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public String getToken() {
		return token;
	}

	public Claims getClaims() {
		if (this.claims != null){
			return this.claims;
		}
		if (StringUtils.isBlank(token)){
			return null;
		}
		this.claims = TokenService.parseToken(token);
		return this.claims;
	}
}
