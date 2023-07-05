
package com.taotao.cloud.auth.biz.strategy.local;

import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.handler.SocialAuthenticationHandler;
import com.taotao.cloud.auth.biz.strategy.AbstractStrategyUserDetailsService;
import com.taotao.cloud.auth.biz.strategy.user.SysUser;
import com.taotao.cloud.security.springsecurity.core.domain.AccessPrincipal;
import com.taotao.cloud.security.springsecurity.core.domain.HerodotusUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>Description: UserDetail本地直联服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/5/23 9:20
 */
public class HerodotusLocalUserDetailsService extends AbstractStrategyUserDetailsService {

	private final SysUserService sysUserService;
	private final SocialAuthenticationHandler socialAuthenticationHandler;

	public HerodotusLocalUserDetailsService(SysUserService sysUserService, SocialAuthenticationHandler socialAuthenticationHandler) {
		this.sysUserService = sysUserService;
		this.socialAuthenticationHandler = socialAuthenticationHandler;
	}

	@Override
	public HerodotusUser findUserDetailsByUsername(String userName) throws UsernameNotFoundException {
		SysUser sysUser = sysUserService.findByUserName(userName);
		return this.convertSysUser(sysUser, userName);
	}

	@Override
	public HerodotusUser findUserDetailsBySocial(String source, AccessPrincipal accessPrincipal) {
		return socialAuthenticationHandler.authentication(source, accessPrincipal);
	}
}
