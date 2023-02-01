package com.taotao.cloud.auth.biz.service;

import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.sys.api.feign.IFeignUserApi;
import java.util.HashSet;
import java.util.Set;
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
	private IFeignUserApi userApi;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Result<SecurityUser> memberSecurityUser = feignUserService.getSysSecurityUser(username);
		//if (!memberSecurityUser.success() || memberSecurityUser == null) {
		//	LogUtil.error("系统用户 [{}] not found.", username);
		//	throw new UsernameNotFoundException(String.format("系统用户 [%s] 不存在", username));
		//}
		//return memberSecurityUser;

		Set<String> permissions = new HashSet<>();
		permissions.add("read");
		permissions.add("write");

		Set<String> roles = new HashSet<>();
		roles.add("admin");
		roles.add("super_admin");

		SecurityUser user = SecurityUser.builder()
			.account("admin")
			.userId(1L)
			.username("admin")
			.nickname("admin")
			.password("$2a$10$ofQ95D2nNs1JC.JiPaGo3O11.P7sP3TkcRyXBpyfskwBDJRAh0caG")
			.phone("15730445331")
			.mobile("15730445331")
			.email("981376578@qq.com")
			.sex(1)
			.status(1)
			.type(2)
			.permissions(permissions)
			.build();

		return user;
	}
}
