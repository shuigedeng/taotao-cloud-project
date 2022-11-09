package com.taotao.cloud.auth.biz.authentication.phone.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultPhoneUserDetailsService implements PhoneUserDetailsService {

	@Override
	public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
		return null;
	}
}
