package com.taotao.cloud.auth.biz.authentication.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public class AuthorityUtils {

	public static Set<GrantedAuthority> createAuthorityList(String... authorities) {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>(authorities.length);
		for (String authority : authorities) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority));
		}
		return grantedAuthorities;
	}
}
