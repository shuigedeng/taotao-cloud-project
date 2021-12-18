/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

public class UserDto implements Serializable {


    private Long id;

    private String username;

    private String nickName;

    private String sex;

    private String avatar;

    private String email;

    private String phone;

    private Boolean enabled;

    @JsonIgnore
    private String password;

    private Timestamp lastPasswordResetTime;

    private Set<RoleSmallDto> roles;

    private JobSmallDto job;

    private DeptSmallDto dept;

    private Long deptId;

    private Timestamp createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getLastPasswordResetTime() {
		return lastPasswordResetTime;
	}

	public void setLastPasswordResetTime(Timestamp lastPasswordResetTime) {
		this.lastPasswordResetTime = lastPasswordResetTime;
	}

	public Set<RoleSmallDto> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleSmallDto> roles) {
		this.roles = roles;
	}

	public JobSmallDto getJob() {
		return job;
	}

	public void setJob(JobSmallDto job) {
		this.job = job;
	}

	public DeptSmallDto getDept() {
		return dept;
	}

	public void setDept(DeptSmallDto dept) {
		this.dept = dept;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
