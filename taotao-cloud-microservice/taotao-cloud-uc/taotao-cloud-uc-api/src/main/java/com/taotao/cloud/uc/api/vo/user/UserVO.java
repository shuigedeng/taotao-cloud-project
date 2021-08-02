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
 * @since 2020/5/14 10:44
 */
@Schema(name = "UserVO", description = "用户VO")
public class UserVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "昵称")
	private String nickname;

	@Schema(description = "真实用户名")
	private String username;

	@Schema(description = "手机号")
	private String phone;

	@Schema(description = "用户类型 1前端用户 2商户用户 3后台管理用户")
	private Integer type;

	@Schema(description = "性别 1男 2女 0未知")
	private Integer sex;

	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "部门ID")
	private Long deptId;

	@Schema(description = "岗位ID")
	private Long jobId;

	@Schema(description = "头像")
	private String avatar;

	@Schema(description = "是否锁定 1-正常，2-锁定")
	private Integer lockFlag;

	@Schema(description = "角色列表")
	private Set<String> roles;

	@Schema(description = "权限列表")
	private Set<String> permissions;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

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
