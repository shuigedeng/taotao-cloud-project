package com.taotao.cloud.oauth2.biz.service;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.member.api.feign.IFeignMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * CloudUserDetailsService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-20 16:20:24
 */
public class MemberUserDetailsService implements UserDetailsService {

	@Autowired
	private IFeignMemberService feignMemberService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Result<SecurityUser> memberSecurityUser = feignMemberService.getMemberSecurityUser(
			username);
		if (!memberSecurityUser.success() || memberSecurityUser.data() == null) {
			LogUtil.error("会员用户 [{}] not found.", username);
			throw new UsernameNotFoundException(String.format("会员用户 [%s] 不存在", username));
		}
		return memberSecurityUser.data();

		//return CloudUserDetails.withId(account.getId())
		//	.username(account.getUsername())
		//	.password(account.getPassword())
		//	.roles(account.getRoles().toArray(String[]::new))
		//	.accountNonLocked(!account.accountLocked())
		//	.enabled(account.getEnabled())
		//	.accountNonExpired(true)
		//	.credentialsNonExpired(true)
		//	.build();

	}
}
