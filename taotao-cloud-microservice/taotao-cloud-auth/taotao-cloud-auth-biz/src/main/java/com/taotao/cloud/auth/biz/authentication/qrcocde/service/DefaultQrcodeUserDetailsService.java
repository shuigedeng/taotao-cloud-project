package com.taotao.cloud.auth.biz.authentication.qrcocde.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultQrcodeUserDetailsService implements QrcodeUserDetailsService {
	@Override
	public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
		return null;
	}
}
