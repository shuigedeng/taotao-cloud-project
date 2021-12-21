package com.taotao.cloud.oauth2.biz.service;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.model.SecurityUser.SecurityUserBuilder;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.member.api.feign.IFeignMemberService;
import java.util.HashSet;
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
		//Result<SecurityUser> memberSecurityUser = feignMemberService.getMemberSecurityUser(
		//	username);
		//if (!memberSecurityUser.success() || memberSecurityUser.data() == null) {
		//	LogUtil.error("会员用户 [{}] not found.", username);
		//	throw new UsernameNotFoundException(String.format("会员用户 [%s] 不存在", username));
		//}

		SecurityUser user = SecurityUser.builder()
			.account("taotao")
			.userId(1L)
			.username("taotao")
			.nickname("taotao")
			.password("$2a$10$ofQ95D2nNs1JC.JiPaGo3O11.P7sP3TkcRyXBpyfskwBDJRAh0caG")
			.phone("15730445331")
			.mobile("15730445331")
			.deptId("1")
			.jobId("1")
			.email("981376578@qq.com")
			.sex(1)
			.status(1)
			.type(2)
			.permissions(new HashSet<>())
			.roles(new HashSet<>())
			.build();

		return user;
		//return memberSecurityUser.data();

	}
}
