package com.taotao.cloud.security.access;

import cn.hutool.core.collection.CollectionUtil;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public class CustomFilterInvocationSecurityMetadataSource implements
	FilterInvocationSecurityMetadataSource {

	private final UrlSecurityPermsLoad urlPermsLoad;
	private final FilterInvocationSecurityMetadataSource superMetadataSource;

	public CustomFilterInvocationSecurityMetadataSource(UrlSecurityPermsLoad urlPermsLoad,
		FilterInvocationSecurityMetadataSource superMetadataSource) {
		this.urlPermsLoad = urlPermsLoad;
		this.superMetadataSource = superMetadataSource;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
		throws IllegalArgumentException {
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		String matchRoles = urlPermsLoad.findMatchRoles(requestUrl);
		if (StringUtils.isBlank(matchRoles)) {
			// 如果路径没有角色配置,则默认是可以被访问的, 只要登录了就行
			matchRoles = "authc";
		}
		final List<ConfigAttribute> list = SecurityConfig.createList(matchRoles);
		return CollectionUtil.isEmpty(list) ? superMetadataSource.getAttributes(object) : list;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
}
