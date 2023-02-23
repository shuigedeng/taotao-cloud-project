package com.taotao.cloud.auth.biz.authentication.account.service;

import cn.hutool.core.util.StrUtil;
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
public class DefaultAccountUserDetailsService implements AccountUserDetailsService {

	@Autowired
	private IFeignUserApi userApi;
	@Autowired
	private IFeignMemberApi memberApi;

	@Override
	public UserDetails loadUserByUsername(String username, String password, String type)
		throws UsernameNotFoundException {
		// 校验密码
		//TODO 此处省略对UserDetails 的可用性 是否过期  是否锁定 是否失效的检验  建议根据实际情况添加  或者在 UserDetailsService 的实现中处理

		if (StrUtil.isEmpty(type) || StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
			throw new IllegalArgumentException("参数错误");
		}

		if (LoginTypeEnum.B_PC_ACCOUNT.getType().equals(type)) {

		}

		if (LoginTypeEnum.C_PC_ACCOUNT.getType().equals(type)) {

		}

		if (LoginTypeEnum.C_APP_ACCOUNT.getType().equals(type)) {

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
