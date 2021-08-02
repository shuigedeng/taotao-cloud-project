package com.taotao.cloud.log.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员登录日志表
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/13 09:46
 */
@Entity
@Table(name = "tt_member_login")
@org.hibernate.annotations.Table(appliesTo = "tt_member_login", comment = "会员登录日志表")
public class MemberLogin extends BaseEntity {

	private static final long serialVersionUID = 6887296988458221221L;

	/**
	 * 会员id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "bigint not null comment '会员id'")
	private Long memberId;

	/**
	 * 用户登录时间
	 */
	@Column(name = "login_time", nullable = false, columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户登录时间'")
	private LocalDateTime loginTime;

	/**
	 * 登陆ip
	 */
	@Column(name = "login_ip", columnDefinition = "varchar(12) not null COMMENT '登录IP'")
	private String loginIp;

	/**
	 * 登录状态
	 *
	 * @see LoginStatusEnum
	 */
	@Column(name = "login_status", nullable = false, columnDefinition = "int not null default 0 comment '登录状态 0-未成功 1-成功'")
	private Integer loginStatus = 0;

	@Override
	public String toString() {
		return "MemberLogin{" +
			"memberId=" + memberId +
			", loginTime=" + loginTime +
			", loginIp='" + loginIp + '\'' +
			", loginStatus=" + loginStatus +
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
		MemberLogin that = (MemberLogin) o;
		return Objects.equals(memberId, that.memberId) && Objects.equals(loginTime,
			that.loginTime) && Objects.equals(loginIp, that.loginIp)
			&& Objects.equals(loginStatus, that.loginStatus);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), memberId, loginTime, loginIp, loginStatus);
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Integer getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(Integer loginStatus) {
		this.loginStatus = loginStatus;
	}

	public MemberLogin() {
	}

	public MemberLogin(Long memberId, LocalDateTime loginTime, String loginIp,
		Integer loginStatus) {
		this.memberId = memberId;
		this.loginTime = loginTime;
		this.loginIp = loginIp;
		this.loginStatus = loginStatus;
	}

	public MemberLogin(Long id, Long createBy, Long lastModifiedBy, LocalDateTime createTime,
		LocalDateTime lastModifiedTime, int version, Boolean delFlag, Long memberId,
		LocalDateTime loginTime, String loginIp, Integer loginStatus) {
		super(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version, delFlag);
		this.memberId = memberId;
		this.loginTime = loginTime;
		this.loginIp = loginIp;
		this.loginStatus = loginStatus;
	}

	public static MemberLoginBuilder builder() {
		return new MemberLoginBuilder();
	}

	public static final class MemberLoginBuilder {

		private Long memberId;
		private LocalDateTime loginTime;
		private String loginIp;
		private Integer loginStatus = 0;
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private MemberLoginBuilder() {
		}

		public static MemberLoginBuilder aMemberLogin() {
			return new MemberLoginBuilder();
		}

		public MemberLoginBuilder memberId(Long memberId) {
			this.memberId = memberId;
			return this;
		}

		public MemberLoginBuilder loginTime(LocalDateTime loginTime) {
			this.loginTime = loginTime;
			return this;
		}

		public MemberLoginBuilder loginIp(String loginIp) {
			this.loginIp = loginIp;
			return this;
		}

		public MemberLoginBuilder loginStatus(Integer loginStatus) {
			this.loginStatus = loginStatus;
			return this;
		}

		public MemberLoginBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public MemberLoginBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public MemberLoginBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public MemberLoginBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public MemberLoginBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public MemberLoginBuilder version(int version) {
			this.version = version;
			return this;
		}

		public MemberLoginBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public MemberLogin build() {
			MemberLogin memberLogin = new MemberLogin();
			memberLogin.setMemberId(memberId);
			memberLogin.setLoginTime(loginTime);
			memberLogin.setLoginIp(loginIp);
			memberLogin.setLoginStatus(loginStatus);
			memberLogin.setId(id);
			memberLogin.setCreateBy(createBy);
			memberLogin.setLastModifiedBy(lastModifiedBy);
			memberLogin.setCreateTime(createTime);
			memberLogin.setLastModifiedTime(lastModifiedTime);
			memberLogin.setVersion(version);
			memberLogin.setDelFlag(delFlag);
			return memberLogin;
		}
	}
}
