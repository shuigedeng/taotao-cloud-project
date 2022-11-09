package com.taotao.cloud.auth.biz.authentication.account.service;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.enums.LoginTypeEnum;
import com.taotao.cloud.member.api.feign.fallback.FeignMemberApiFallback;
import com.taotao.cloud.sys.api.feign.IFeignUserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultAccountUserDetailsService implements AccountUserDetailsService {

	@Autowired
	private IFeignUserApi feignUserApi;
	@Autowired
	private FeignMemberApiFallback fe;

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

		return null;
	}
}
