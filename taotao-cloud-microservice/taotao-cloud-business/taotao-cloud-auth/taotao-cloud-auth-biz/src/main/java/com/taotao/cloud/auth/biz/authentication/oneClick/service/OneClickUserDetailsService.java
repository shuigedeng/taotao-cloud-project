package com.taotao.cloud.auth.biz.authentication.oneClick.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface OneClickUserDetailsService {

	/**
	 * load user by phone
	 *
	 * @param phone phone
	 * @return userDetails
	 * @throws UsernameNotFoundException not found user
	 */
	UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException;
}
