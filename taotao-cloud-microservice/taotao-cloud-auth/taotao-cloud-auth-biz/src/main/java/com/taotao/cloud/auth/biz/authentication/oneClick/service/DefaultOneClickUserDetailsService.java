package com.taotao.cloud.auth.biz.authentication.oneClick.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultOneClickUserDetailsService implements OneClickUserDetailsService {

	@Override
	public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
		return null;
	}
}
