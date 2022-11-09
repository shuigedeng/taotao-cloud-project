package com.taotao.cloud.auth.biz.authentication.gestures.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultGesturesUserDetailsService implements GesturesUserDetailsService {

	@Override
	public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
		return null;
	}
}
