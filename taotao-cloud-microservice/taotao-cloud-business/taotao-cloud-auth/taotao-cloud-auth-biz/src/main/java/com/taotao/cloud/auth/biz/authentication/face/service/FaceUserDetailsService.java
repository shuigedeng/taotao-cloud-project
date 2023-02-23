package com.taotao.cloud.auth.biz.authentication.face.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface FaceUserDetailsService {

	/**
	 * load user by imgBase64
	 *
	 * @param imgBase64 imgBase64
	 * @return userDetails
	 * @throws UsernameNotFoundException not found user
	 */
	UserDetails loadUserByImgBase64(String imgBase64) throws UsernameNotFoundException;
}
