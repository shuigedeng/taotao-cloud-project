package com.taotao.cloud.auth.biz.authentication.qrcocde.service;

import com.taotao.cloud.common.model.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DefaultQrcodeUserDetailsService implements QrcodeUserDetailsService {
	@Override
	public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
		return SecurityUser.builder()
			.account("admin")
			.userId(1L)
			.username("admin")
			.nickname("admin")
			.password(
				"$2a$10$ofQ95D2nNs1JC.JiPaGo3O11.P7sP3TkcRyXBpyfskwBDJRAh0caG")
			.phone("15730445331")
			.mobile("15730445331")
			.email("981376578@qq.com")
			.sex(1)
			.status(1)
			.type(2)
			.permissions(Set.of("xxx", "sldfl"))
			.build();
	}
}
