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
package com.taotao.cloud.uc.api.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * 用户VO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:19:37
 */
@Schema(name = "UserVO", description = "用户VO")
public class UserVO implements Serializable {

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

	public UserVO() {
	}

	public UserVO(Long id, String nickname, String username, String phone, Integer type,
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

	@Override
	public String toString() {
		return "UserVO{" +
			"id=" + id +
			", nickname='" + nickname + '\'' +
			", username='" + username + '\'' +
			", phone='" + phone + '\'' +
			", type=" + type +
			", sex=" + sex +
			", email='" + email + '\'' +
			", deptId=" + deptId +
			", jobId=" + jobId +
			", avatar='" + avatar + '\'' +
			", lockFlag=" + lockFlag +
			", roles=" + roles +
			", permissions=" + permissions +
			", createTime=" + createTime +
			", lastModifiedTime=" + lastModifiedTime +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserVO userVO = (UserVO) o;
		return Objects.equals(id, userVO.id) && Objects.equals(nickname,
			userVO.nickname) && Objects.equals(username, userVO.username)
			&& Objects.equals(phone, userVO.phone) && Objects.equals(type,
			userVO.type) && Objects.equals(sex, userVO.sex) && Objects.equals(
			email, userVO.email) && Objects.equals(deptId, userVO.deptId)
			&& Objects.equals(jobId, userVO.jobId) && Objects.equals(avatar,
			userVO.avatar) && Objects.equals(lockFlag, userVO.lockFlag)
			&& Objects.equals(roles, userVO.roles) && Objects.equals(
			permissions, userVO.permissions) && Objects.equals(createTime,
			userVO.createTime) && Objects.equals(lastModifiedTime,
			userVO.lastModifiedTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nickname, username, phone, type, sex, email, deptId, jobId, avatar,
			lockFlag, roles, permissions, createTime, lastModifiedTime);
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

	public static UserVOBuilder builder() {
		return new UserVOBuilder();
	}

	public static final class UserVOBuilder {

		private Long id;
		private String nickname;
		private String username;
		private String phone;
		private Integer type;
		private Integer sex;
		private String email;
		private Long deptId;
		private Long jobId;
		private String avatar;
		private Integer lockFlag;
		private Set<String> roles;
		private Set<String> permissions;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;

		private UserVOBuilder() {
		}

		public static UserVOBuilder anUserVO() {
			return new UserVOBuilder();
		}

		public UserVOBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public UserVOBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public UserVOBuilder username(String username) {
			this.username = username;
			return this;
		}

		public UserVOBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public UserVOBuilder type(Integer type) {
			this.type = type;
			return this;
		}

		public UserVOBuilder sex(Integer sex) {
			this.sex = sex;
			return this;
		}

		public UserVOBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserVOBuilder deptId(Long deptId) {
			this.deptId = deptId;
			return this;
		}

		public UserVOBuilder jobId(Long jobId) {
			this.jobId = jobId;
			return this;
		}

		public UserVOBuilder avatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public UserVOBuilder lockFlag(Integer lockFlag) {
			this.lockFlag = lockFlag;
			return this;
		}

		public UserVOBuilder roles(Set<String> roles) {
			this.roles = roles;
			return this;
		}

		public UserVOBuilder permissions(Set<String> permissions) {
			this.permissions = permissions;
			return this;
		}

		public UserVOBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public UserVOBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public UserVO build() {
			UserVO userVO = new UserVO();
			userVO.setId(id);
			userVO.setNickname(nickname);
			userVO.setUsername(username);
			userVO.setPhone(phone);
			userVO.setType(type);
			userVO.setSex(sex);
			userVO.setEmail(email);
			userVO.setDeptId(deptId);
			userVO.setJobId(jobId);
			userVO.setAvatar(avatar);
			userVO.setLockFlag(lockFlag);
			userVO.setRoles(roles);
			userVO.setPermissions(permissions);
			userVO.setCreateTime(createTime);
			userVO.setLastModifiedTime(lastModifiedTime);
			return userVO;
		}
	}
}
