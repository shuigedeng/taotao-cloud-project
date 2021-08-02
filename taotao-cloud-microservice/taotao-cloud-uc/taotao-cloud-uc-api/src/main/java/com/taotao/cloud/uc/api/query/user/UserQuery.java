package com.taotao.cloud.uc.api.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * 用户查询query
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "UserQuery", description = "用户查询query")
public class UserQuery implements Serializable {

	private static final long serialVersionUID = -6200931899296559445L;

	@Schema(description = "用户昵称")
	private String nickname;

	@Schema(description = "用户真实姓名")
	private String username;

	@Schema(description = "电话")
	private String phone;

	@Schema(description = "email")
	private String email;

	@Schema(description = "用户类型 1前端用户 2商户用户 3后台管理用户")
//	@IntEnums(value = {1, 2, 3})
	private Integer type;

	@Schema(description = "性别 1男 2女 0未知")
//	@IntEnums(value = {0, 1, 2})
	private Integer sex;

	@Schema(description = "部门id")
	private Long deptId;

	@Schema(description = "岗位id")
	private Long jobId;

	public UserQuery() {
	}

	public UserQuery(String nickname, String username, String phone, String email,
		Integer type, Integer sex, Long deptId, Long jobId) {
		this.nickname = nickname;
		this.username = username;
		this.phone = phone;
		this.email = email;
		this.type = type;
		this.sex = sex;
		this.deptId = deptId;
		this.jobId = jobId;
	}

	@Override
	public String toString() {
		return "UserQuery{" +
			"nickname='" + nickname + '\'' +
			", username='" + username + '\'' +
			", phone='" + phone + '\'' +
			", email='" + email + '\'' +
			", type=" + type +
			", sex=" + sex +
			", deptId=" + deptId +
			", jobId=" + jobId +
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
		UserQuery userQuery = (UserQuery) o;
		return Objects.equals(nickname, userQuery.nickname) && Objects.equals(
			username, userQuery.username) && Objects.equals(phone, userQuery.phone)
			&& Objects.equals(email, userQuery.email) && Objects.equals(type,
			userQuery.type) && Objects.equals(sex, userQuery.sex)
			&& Objects.equals(deptId, userQuery.deptId) && Objects.equals(jobId,
			userQuery.jobId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nickname, username, phone, email, type, sex, deptId, jobId);
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public static UserQueryBuilder builder() {
		return new UserQueryBuilder();
	}

	public static final class UserQueryBuilder {

		private String nickname;
		private String username;
		private String phone;
		private String email;
		//	@IntEnums(value = {1, 2, 3})
		private Integer type;
		//	@IntEnums(value = {0, 1, 2})
		private Integer sex;
		private Long deptId;
		private Long jobId;

		private UserQueryBuilder() {
		}

		public static UserQueryBuilder anUserQuery() {
			return new UserQueryBuilder();
		}

		public UserQueryBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public UserQueryBuilder username(String username) {
			this.username = username;
			return this;
		}

		public UserQueryBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public UserQueryBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserQueryBuilder type(Integer type) {
			this.type = type;
			return this;
		}

		public UserQueryBuilder sex(Integer sex) {
			this.sex = sex;
			return this;
		}

		public UserQueryBuilder deptId(Long deptId) {
			this.deptId = deptId;
			return this;
		}

		public UserQueryBuilder jobId(Long jobId) {
			this.jobId = jobId;
			return this;
		}

		public UserQuery build() {
			UserQuery userQuery = new UserQuery();
			userQuery.setNickname(nickname);
			userQuery.setUsername(username);
			userQuery.setPhone(phone);
			userQuery.setEmail(email);
			userQuery.setType(type);
			userQuery.setSex(sex);
			userQuery.setDeptId(deptId);
			userQuery.setJobId(jobId);
			return userQuery;
		}
	}
}
