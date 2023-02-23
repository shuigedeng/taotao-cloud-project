package com.taotao.cloud.auth.biz.authentication.account.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AccountUserDetailsService {

	/**
	 * load user by phone
	 *
	 * @param username username
	 * @return userDetails
	 * @throws UsernameNotFoundException not found user
	 */
	UserDetails loadUserByUsername(String username, String password, String type)
		throws UsernameNotFoundException;
}
