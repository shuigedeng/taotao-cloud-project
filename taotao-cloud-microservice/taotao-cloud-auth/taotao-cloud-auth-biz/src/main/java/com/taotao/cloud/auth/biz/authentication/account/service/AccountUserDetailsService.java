package com.taotao.cloud.auth.biz.authentication.account.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AccountUserDetailsService {

	/**
	 * load user by phone
	 *
	 * @param phone phone
	 * @return userDetails
	 * @throws UsernameNotFoundException not found user
	 */
	UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException;
}
