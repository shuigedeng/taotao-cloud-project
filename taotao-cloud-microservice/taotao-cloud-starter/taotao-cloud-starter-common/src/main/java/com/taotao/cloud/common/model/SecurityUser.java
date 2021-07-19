/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.model;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户手机号和账号密码 身份权限认证类 登陆身份认证
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/29 17:52
 */
@Data
public class SecurityUser implements UserDetails, Serializable {

	private static final long serialVersionUID = -3685249101751401211L;

	private static final String ROLE_PREFIX = "ROLE_";

	private Long userId;
	private String username;
	private String password;
	private String deptId;
	private String jobId;
	private String email;
	private String phone;
	private String avatar;
	private String lockFlag;
	private String delFlag;
	private String nickname;
	private Integer sex;
	private Integer type;

	private Set<String> permissions;
	private Set<String> roles;

	public SecurityUser() {

	}

	/**
	 * @param userId      用户Id
	 * @param username    用户名称
	 * @param password    密码
	 * @param permissions 权限
	 * @param roles       权限
	 */
	public SecurityUser(Long userId,
		String username,
		String password,
		Set<String> permissions,
		Set<String> roles) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.permissions = permissions;
		this.roles = roles;
	}

	/***
	 * 权限重写
	 */
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		if (!CollUtil.isEmpty(roles)) {
			roles.parallelStream()
				.forEach(role -> authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role)));
		}
		if (!CollUtil.isEmpty(permissions)) {
			permissions.parallelStream()
				.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
		}
		return authorities;
	}

	/**
	 * 账户是否未过期,过期无法验证
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 指定用户是否解锁,锁定的用户无法进行身份验证
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 是否可用 ,禁用的用户不能身份验证
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}
