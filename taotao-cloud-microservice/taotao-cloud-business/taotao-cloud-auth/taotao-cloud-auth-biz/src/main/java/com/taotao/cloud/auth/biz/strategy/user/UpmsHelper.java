

package com.taotao.cloud.auth.biz.strategy.user;


import com.taotao.cloud.data.jpa.tenant.DataItemStatus;
import com.taotao.cloud.security.springsecurity.core.domain.HerodotusGrantedAuthority;
import com.taotao.cloud.security.springsecurity.core.domain.HerodotusUser;
import com.taotao.cloud.security.springsecurity.core.utils.SecurityUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Description: 实体转换帮助类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/18 11:38
 */
public class UpmsHelper {

	public static HerodotusUser convertSysUserToHerodotusUser(SysUser sysUser) {
		Set<HerodotusGrantedAuthority> authorities = new HashSet<>();
		Set<String> roles = new HashSet<>();
		for (SysRole sysRole : sysUser.getRoles()) {
			roles.add(sysRole.getRoleCode());
			authorities.add(new HerodotusGrantedAuthority(SecurityUtils.wellFormRolePrefix(sysRole.getRoleCode())));
			Set<SysPermission> sysPermissions = sysRole.getPermissions();
			if (CollectionUtils.isNotEmpty(sysPermissions)) {
				sysPermissions.forEach(sysAuthority -> authorities.add(new HerodotusGrantedAuthority((sysAuthority.getPermissionCode()))));
			}
		}

		String employeeId = ObjectUtils.isNotEmpty(sysUser.getEmployee()) ? sysUser.getEmployee().getEmployeeId() : null;

		return new HerodotusUser(sysUser.getUserId(), sysUser.getUserName(), sysUser.getPassword(),
			isEnabled(sysUser),
			isAccountNonExpired(sysUser),
			isCredentialsNonExpired(sysUser),
			isNonLocked(sysUser),
			authorities, roles, employeeId, sysUser.getAvatar());
	}

	private static boolean isEnabled(SysUser sysUser) {
		return sysUser.getStatus() != DataItemStatus.FORBIDDEN;
	}

	private static boolean isNonLocked(SysUser sysUser) {
		return !(sysUser.getStatus() == DataItemStatus.LOCKING);
	}

	private static boolean isNonExpired(LocalDateTime localDateTime) {
		if (ObjectUtils.isEmpty(localDateTime)) {
			return true;
		} else {
			return localDateTime.isAfter(LocalDateTime.now());
		}
	}

	private static boolean isAccountNonExpired(SysUser sysUser) {
		if (sysUser.getStatus() == DataItemStatus.EXPIRED) {
			return false;
		}

		return isNonExpired(sysUser.getAccountExpireAt());
	}

	private static boolean isCredentialsNonExpired(SysUser sysUser) {
		return isNonExpired(sysUser.getCredentialsExpireAt());
	}
}
