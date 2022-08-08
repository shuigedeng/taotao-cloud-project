package com.taotao.cloud.security.perm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

public class VipSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
	private Set<PermRoleEntity> permRoleEntitySet;
	private final FilterInvocationSecurityMetadataSource superMetadataSource;
	private final VipSecurityOauthService vipSecurityOauthService;

	public VipSecurityMetadataSource(FilterInvocationSecurityMetadataSource superMetadataSource,
		VipSecurityOauthService vipSecurityOauthService) {
		this.superMetadataSource = superMetadataSource;
		this.vipSecurityOauthService = vipSecurityOauthService;
	}

	private void loadPerms() {
		permRoleEntitySet = vipSecurityOauthService.loadPerms();
	}

	/**
	 * 返回能访问该请求的所有角色集合
	 **/
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
		throws IllegalArgumentException {
		loadPerms();

		FilterInvocation fi = (FilterInvocation) object;
		String accessUri = fi.getRequestUrl();
		for (PermRoleEntity permRoleEntity : permRoleEntitySet) {
			if (ANT_PATH_MATCHER.match(permRoleEntity.getAccessUri(), accessUri)) {
				return permRoleEntity.getConfigAttributeList();
			}
		}

		return superMetadataSource.getAttributes(object);
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		loadPerms();
		Set<ConfigAttribute> attributeSet = new HashSet<>();
		permRoleEntitySet.stream().map(PermRoleEntity::getConfigAttributeList)
			.forEach(attributeSet::addAll);
		return attributeSet;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
}
