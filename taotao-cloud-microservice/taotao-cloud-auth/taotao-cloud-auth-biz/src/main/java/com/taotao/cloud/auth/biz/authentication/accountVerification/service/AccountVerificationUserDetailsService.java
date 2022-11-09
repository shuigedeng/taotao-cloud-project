package com.taotao.cloud.auth.biz.authentication.accountVerification.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AccountVerificationUserDetailsService {

	/**
	 * load user by username
	 *
	 * @param username username
	 * @return userDetails
	 * @throws UsernameNotFoundException not found user
	 */
	UserDetails loadUserByUsername(String username, String password, String type) throws UsernameNotFoundException;
}
