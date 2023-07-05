

package com.taotao.cloud.auth.biz.strategy.remote;

import com.taotao.cloud.auth.biz.strategy.AbstractStrategyUserDetailsService;
import com.taotao.cloud.security.springsecurity.core.domain.AccessPrincipal;
import com.taotao.cloud.security.springsecurity.core.domain.HerodotusGrantedAuthority;
import com.taotao.cloud.security.springsecurity.core.domain.HerodotusUser;
import com.taotao.cloud.sys.api.feign.IFeignUserApi;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Description: UserDetail远程调用服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/5/23 9:21
 */
public class HerodotusRemoteUserDetailsService extends AbstractStrategyUserDetailsService {

	private final IFeignUserApi userApi;

	public HerodotusRemoteUserDetailsService(IFeignUserApi userApi) {
		this.userApi = userApi;
	}

	@Override
	public HerodotusUser findUserDetailsByUsername(String userName) throws UsernameNotFoundException {
//		Result<SysUser> result = remoteUserDetailsService.findByUserName(userName);
//
//		SysUser sysUser = result.getData();
//		return this.convertSysUser(sysUser, userName);
//		return new HerodotusUser();

		Collection<HerodotusGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new HerodotusGrantedAuthority("manager.book.read"));
		authorities.add(new HerodotusGrantedAuthority("manager.book.write"));
		Set<String> roles = new HashSet<>();
		roles.add("ROLE_A1");
		roles.add("ROLE_A2");
		// admin/123456
		HerodotusUser user = new HerodotusUser("33e781c5-31e0-4ea4-8b02-1236bde9643", "admin",
			"{bcrypt}$2a$10$lvjys/FAHAVmgXM.U1LtOOJ./C5SstExZCZ0Z5N7SeGZAue0JFtXC",
			true, true, true, true,
			authorities, roles, "", "");
		return user;
	}

	@Override
	public HerodotusUser findUserDetailsBySocial(String source, AccessPrincipal accessPrincipal) {
//		Result<HerodotusUser> result = remoteSocialDetailsService.findUserDetailsBySocial(source, accessPrincipal);
//		return result.getData();
		return new HerodotusUser();
	}
}
