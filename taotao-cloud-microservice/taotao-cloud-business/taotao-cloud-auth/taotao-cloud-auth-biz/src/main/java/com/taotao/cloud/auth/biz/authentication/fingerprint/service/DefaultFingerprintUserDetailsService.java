package com.taotao.cloud.auth.biz.authentication.fingerprint.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultFingerprintUserDetailsService implements FingerprintUserDetailsService {

	@Override
	public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
		return null;
	}
}
