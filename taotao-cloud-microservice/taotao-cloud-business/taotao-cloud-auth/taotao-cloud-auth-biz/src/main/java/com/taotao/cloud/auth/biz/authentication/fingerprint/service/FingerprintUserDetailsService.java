package com.taotao.cloud.auth.biz.authentication.fingerprint.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface FingerprintUserDetailsService {

	/**
	 * load user by phone
	 *
	 * @param phone phone
	 * @return userDetails
	 * @throws UsernameNotFoundException not found user
	 */
	UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException;
}
