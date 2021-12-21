package com.taotao.cloud.oauth2.biz.service;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sys.api.feign.IFeignUserService;
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
public class SysUserDetailsService implements UserDetailsService {

	@Autowired
	private IFeignUserService feignUserService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Result<SecurityUser> memberSecurityUser = feignUserService.getSysSecurityUser(username);
		//if (!memberSecurityUser.success() || memberSecurityUser.data() == null) {
		//	LogUtil.error("系统用户 [{}] not found.", username);
		//	throw new UsernameNotFoundException(String.format("系统用户 [%s] 不存在", username));
		//}
		//return memberSecurityUser.data();

		SecurityUser user = SecurityUser.builder()
			.account("admin")
			.userId(1L)
			.username("admin")
			.nickname("admin")
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
	}
}
