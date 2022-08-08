package com.taotao.cloud.security.perm;

import java.util.Collection;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class VipAccessDecisionManager implements AccessDecisionManager {

	/**
	 * 决定当前用户是否有权限访问该请求
	 **/
	@Override
	public void decide(
		Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
		throws AccessDeniedException, InsufficientAuthenticationException {
		for (ConfigAttribute configAttribute : configAttributes) {            //将访问所需资源或用户拥有资源进行比对
			String needAuthority = configAttribute.getAttribute();
			if (needAuthority == null) {
				continue;
			}
			for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
				if (needAuthority.trim().equals(grantedAuthority.getAuthority())) {
					return;
				}
			}
		}
		throw new AccessDeniedException("抱歉，您没有访问权限");
	}

	@Override
	public boolean supports(ConfigAttribute configAttribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}
}
