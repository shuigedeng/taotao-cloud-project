package com.taotao.cloud.uc.api.query.user;

import com.taotao.cloud.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * 用户分页查询query
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "UserPageQuery", description = "用户分页查询query")
public class UserPageQuery extends PageQuery {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "用户昵称")
	private String nickname;

	@Schema(description = "用户真实姓名")
	private String username;

	@Schema(description = "电话")
	private String phone;

	@Schema(description = "email")
	private String email;

	@Schema(description = "部门id")
	private Long deptId;

	@Schema(description = "岗位id")
	private Long jobId;

	@Override
	public String toString() {
		return "UserPageQuery{" +
			"nickname='" + nickname + '\'' +
			", username='" + username + '\'' +
			", phone='" + phone + '\'' +
			", email='" + email + '\'' +
			", deptId=" + deptId +
			", jobId=" + jobId +
			"} " + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		UserPageQuery that = (UserPageQuery) o;
		return Objects.equals(nickname, that.nickname) && Objects.equals(username,
			that.username) && Objects.equals(phone, that.phone)
			&& Objects.equals(email, that.email) && Objects.equals(deptId,
			that.deptId) && Objects.equals(jobId, that.jobId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), nickname, username, phone, email, deptId, jobId);
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

	public UserPageQuery() {
	}

	public UserPageQuery(String nickname, String username, String phone, String email,
		Long deptId, Long jobId) {
		this.nickname = nickname;
		this.username = username;
		this.phone = phone;
		this.email = email;
		this.deptId = deptId;
		this.jobId = jobId;
	}

	public UserPageQuery(Integer currentPage, Integer pageSize, String nickname,
		String username, String phone, String email, Long deptId, Long jobId) {
		super(currentPage, pageSize);
		this.nickname = nickname;
		this.username = username;
		this.phone = phone;
		this.email = email;
		this.deptId = deptId;
		this.jobId = jobId;
	}


	public static UserPageQueryBuilder builder() {
		return new UserPageQueryBuilder();
	}


	public static final class UserPageQueryBuilder {

		private Integer currentPage = 1;
		private Integer pageSize = 10;
		private String nickname;
		private String username;
		private String phone;
		private String email;
		private Long deptId;
		private Long jobId;

		private UserPageQueryBuilder() {
		}

		public static UserPageQueryBuilder anUserPageQuery() {
			return new UserPageQueryBuilder();
		}

		public UserPageQueryBuilder currentPage(Integer currentPage) {
			this.currentPage = currentPage;
			return this;
		}

		public UserPageQueryBuilder pageSize(Integer pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public UserPageQueryBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public UserPageQueryBuilder username(String username) {
			this.username = username;
			return this;
		}

		public UserPageQueryBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public UserPageQueryBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserPageQueryBuilder deptId(Long deptId) {
			this.deptId = deptId;
			return this;
		}

		public UserPageQueryBuilder jobId(Long jobId) {
			this.jobId = jobId;
			return this;
		}

		public UserPageQuery build() {
			UserPageQuery userPageQuery = new UserPageQuery();
			userPageQuery.setCurrentPage(currentPage);
			userPageQuery.setPageSize(pageSize);
			userPageQuery.setNickname(nickname);
			userPageQuery.setUsername(username);
			userPageQuery.setPhone(phone);
			userPageQuery.setEmail(email);
			userPageQuery.setDeptId(deptId);
			userPageQuery.setJobId(jobId);
			return userPageQuery;
		}
	}
}
