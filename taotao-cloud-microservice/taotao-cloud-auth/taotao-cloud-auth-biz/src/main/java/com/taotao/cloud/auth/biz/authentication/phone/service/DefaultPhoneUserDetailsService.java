package com.taotao.cloud.auth.biz.authentication.phone.service;

import com.taotao.cloud.common.enums.LoginTypeEnum;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import com.taotao.cloud.sys.api.feign.IFeignUserApi;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultPhoneUserDetailsService implements PhoneUserDetailsService {

	@Autowired
	private IFeignUserApi userApi;
	@Autowired
	private IFeignMemberApi memberApi;

	@Override
	public UserDetails loadUserByPhone(String phone, String type) throws UsernameNotFoundException {

		if (LoginTypeEnum.B_PC_PHONE.getType().equals(type)) {

		}

		if (LoginTypeEnum.C_PC_PHONE.getType().equals(type)) {

		}

		if (LoginTypeEnum.C_MIMI_PHONE.getType().equals(type)) {

		}

		if (LoginTypeEnum.C_APP_PHONE.getType().equals(type)) {

		}

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
