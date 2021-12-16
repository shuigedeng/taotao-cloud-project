/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author hupeng
 * @date 2018-11-23
 */
public class JwtUser implements UserDetails {

    private final Long id;

    private final String username;

    private final String nickName;

    private final String sex;

    @JsonIgnore
    private final String password;

    private final String avatar;

    private final String email;

    private final String phone;

    private final String dept;

    private final String job;

    @JsonIgnore
    private final Collection<SimpleGrantedAuthority> authorities;

    private final boolean enabled;

    private Timestamp createTime;

    @JsonIgnore
    private final Date lastPasswordResetDate;

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

	public JwtUser(Long id, String username, String nickName, String sex, String password,
		String avatar, String email, String phone, String dept, String job,
		Collection<SimpleGrantedAuthority> authorities, boolean enabled,
		Date lastPasswordResetDate) {
		this.id = id;
		this.username = username;
		this.nickName = nickName;
		this.sex = sex;
		this.password = password;
		this.avatar = avatar;
		this.email = email;
		this.phone = phone;
		this.dept = dept;
		this.job = job;
		this.authorities = authorities;
		this.enabled = enabled;
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

	@Override
	public String getUsername() {
		return null;
	}

	@Override
    public boolean isEnabled() {
        return enabled;
    }

    public Collection getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
