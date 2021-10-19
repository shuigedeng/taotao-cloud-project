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
package com.taotao.cloud.uc.api.bo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:19:37
 */
@Schema(name = "UserQueryVO", description = "用户查询对象")
public class UserBO implements Serializable {

	@Serial
	private static final long serialVersionUID = 5126530068827085130L;

	/**
	 * id
	 */
	@Schema(description = "id")
	private Long id;
	/**
	 * 昵称
	 */
	@Schema(description = "昵称")
	private String nickname;
	/**
	 * 真实用户名
	 */
	@Schema(description = "真实用户名")
	private String username;
	/**
	 * 手机号
	 */
	@Schema(description = "手机号")
	private String phone;
	/**
	 * 用户类型 1前端用户 2商户用户 3后台管理用户
	 */
	@Schema(description = "用户类型 1前端用户 2商户用户 3后台管理用户")
	private Integer type;
	/**
	 * 性别 1男 2女 0未知
	 */
	@Schema(description = "性别 1男 2女 0未知")
	private Integer sex;
	/**
	 * 邮箱
	 */
	@Schema(description = "邮箱")
	private String email;
	/**
	 * 部门ID
	 */
	@Schema(description = "部门ID")
	private Long deptId;
	/**
	 * 岗位ID
	 */
	@Schema(description = "岗位ID")
	private Long jobId;
	/**
	 * 头像
	 */
	@Schema(description = "头像")
	private String avatar;
	/**
	 * 是否锁定 1-正常，2-锁定
	 */
	@Schema(description = "是否锁定 1-正常，2-锁定")
	private Integer lockFlag;
	/**
	 * 角色列表
	 */
	@Schema(description = "角色列表")
	private Set<String> roles;
	/**
	 * 权限列表
	 */
	@Schema(description = "权限列表")
	private Set<String> permissions;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 最后修改时间
	 */
	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;

	public UserBO() {
	}

	public UserBO(Long id, String nickname, String username, String phone, Integer type,
		Integer sex, String email, Long deptId, Long jobId, String avatar,
		Integer lockFlag, Set<String> roles, Set<String> permissions,
		LocalDateTime createTime, LocalDateTime lastModifiedTime) {
		this.id = id;
		this.nickname = nickname;
		this.username = username;
		this.phone = phone;
		this.type = type;
		this.sex = sex;
		this.email = email;
		this.deptId = deptId;
		this.jobId = jobId;
		this.avatar = avatar;
		this.lockFlag = lockFlag;
		this.roles = roles;
		this.permissions = permissions;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(Integer lockFlag) {
		this.lockFlag = lockFlag;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
}
