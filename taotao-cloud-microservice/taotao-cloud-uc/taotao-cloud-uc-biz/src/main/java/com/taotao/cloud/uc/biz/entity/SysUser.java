package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.common.enums.SexTypeEnum;
import com.taotao.cloud.data.jpa.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 后台管理用户表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Entity
@Table(name = "tt_sys_user")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_user", comment = "后台管理用户表")
public class SysUser extends BaseEntity {

	/**
	 * 昵称
	 */
	@Column(name = "nickname", nullable = false, columnDefinition = "varchar(255) not null comment '昵称'")
	private String nickname;

	/**
	 * 真实用户名
	 */
	@Column(name = "username", nullable = false, columnDefinition = "varchar(255) not null comment '真实用户名'")
	private String username;

	/**
	 * 手机号
	 */
	@Column(name = "phone", unique = true, nullable = false, columnDefinition = "varchar(11) not null comment '手机号'")
	private String phone;

	/**
	 * 密码
	 */
	@Column(name = "password", nullable = false, columnDefinition = "varchar(255) not null comment '密码'")
	private String password;

	@Column(name = "type", nullable = false, columnDefinition = "int not null default 3 comment '用户类型 1前端用户 2商户用户 3后台管理用户'")
	private int type = 3;

	/**
	 * @see SexTypeEnum
	 */
	@Column(name = "sex", nullable = false, columnDefinition = "int not null default 0 comment '性别 1男 2女 0未知'")
	private int sex = 0;

	/**
	 * 邮箱
	 */
	@Column(name = "email", columnDefinition = "varchar(50) not null comment '邮箱'")
	private String email;

	/**
	 * 部门ID
	 */
	@Column(name = "dept_id", columnDefinition = "bigint comment '部门ID'")
	private Long deptId;

	/**
	 * 岗位ID
	 */
	@Column(name = "job_id", columnDefinition = "bigint comment '岗位ID'")
	private Long jobId;

	/**
	 * 头像
	 */
	@Column(name = "avatar", columnDefinition = "varchar(255) comment '头像'")
	private String avatar;

	/**
	 * 是否锁定 0-正常，1-锁定o
	 */
	@Column(name = "is_lock", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否锁定 0-正常，1-锁定'")
	private Boolean isLock = false;

	/**
	 * 租户id
	 */
	@Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
	private String tenantId;

	@Override
	public String toString() {
		return "SysUser{" +
			"nickname='" + nickname + '\'' +
			", username='" + username + '\'' +
			", phone='" + phone + '\'' +
			", password='" + password + '\'' +
			", type=" + type +
			", sex=" + sex +
			", email='" + email + '\'' +
			", deptId=" + deptId +
			", jobId=" + jobId +
			", avatar='" + avatar + '\'' +
			", isLock=" + isLock +
			", tenantId='" + tenantId + '\'' +
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
		SysUser sysUser = (SysUser) o;
		return type == sysUser.type && sex == sysUser.sex && Objects.equals(nickname,
			sysUser.nickname) && Objects.equals(username, sysUser.username)
			&& Objects.equals(phone, sysUser.phone) && Objects.equals(password,
			sysUser.password) && Objects.equals(email, sysUser.email)
			&& Objects.equals(deptId, sysUser.deptId) && Objects.equals(jobId,
			sysUser.jobId) && Objects.equals(avatar, sysUser.avatar)
			&& Objects.equals(isLock, sysUser.isLock) && Objects.equals(
			tenantId, sysUser.tenantId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), nickname, username, phone, password, type, sex, email,
			deptId, jobId, avatar, isLock, tenantId);
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
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

	public Boolean getLock() {
		return isLock;
	}

	public void setLock(Boolean lock) {
		isLock = lock;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public SysUser() {
	}

	public SysUser(String nickname, String username, String phone, String password, int type,
		int sex,
		String email, Long deptId, Long jobId, String avatar, Boolean isLock,
		String tenantId) {
		this.nickname = nickname;
		this.username = username;
		this.phone = phone;
		this.password = password;
		this.type = type;
		this.sex = sex;
		this.email = email;
		this.deptId = deptId;
		this.jobId = jobId;
		this.avatar = avatar;
		this.isLock = isLock;
		this.tenantId = tenantId;
	}

	public SysUser(Long id, Long createBy, Long lastModifiedBy, LocalDateTime createTime,
		LocalDateTime lastModifiedTime, int version, Boolean delFlag, String nickname,
		String username, String phone, String password, int type, int sex, String email,
		Long deptId, Long jobId, String avatar, Boolean isLock, String tenantId) {
		super(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version, delFlag);
		this.nickname = nickname;
		this.username = username;
		this.phone = phone;
		this.password = password;
		this.type = type;
		this.sex = sex;
		this.email = email;
		this.deptId = deptId;
		this.jobId = jobId;
		this.avatar = avatar;
		this.isLock = isLock;
		this.tenantId = tenantId;
	}

	public static SysUserBuilder builder() {
		return new SysUserBuilder();
	}

	public static final class SysUserBuilder {

		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;
		private String nickname;
		private String username;
		private String phone;
		private String password;
		private int type = 3;
		private int sex = 0;
		private String email;
		private Long deptId;
		private Long jobId;
		private String avatar;
		private Boolean isLock = false;
		private String tenantId;

		private SysUserBuilder() {
		}

		public static SysUserBuilder aSysUser() {
			return new SysUserBuilder();
		}

		public SysUserBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysUserBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public SysUserBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public SysUserBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public SysUserBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public SysUserBuilder version(int version) {
			this.version = version;
			return this;
		}

		public SysUserBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public SysUserBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public SysUserBuilder username(String username) {
			this.username = username;
			return this;
		}

		public SysUserBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public SysUserBuilder password(String password) {
			this.password = password;
			return this;
		}

		public SysUserBuilder type(int type) {
			this.type = type;
			return this;
		}

		public SysUserBuilder sex(int sex) {
			this.sex = sex;
			return this;
		}

		public SysUserBuilder email(String email) {
			this.email = email;
			return this;
		}

		public SysUserBuilder deptId(Long deptId) {
			this.deptId = deptId;
			return this;
		}

		public SysUserBuilder jobId(Long jobId) {
			this.jobId = jobId;
			return this;
		}

		public SysUserBuilder avatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public SysUserBuilder isLock(Boolean isLock) {
			this.isLock = isLock;
			return this;
		}

		public SysUserBuilder tenantId(String tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public SysUser build() {
			SysUser sysUser = new SysUser();
			sysUser.setId(id);
			sysUser.setCreateBy(createBy);
			sysUser.setLastModifiedBy(lastModifiedBy);
			sysUser.setCreateTime(createTime);
			sysUser.setLastModifiedTime(lastModifiedTime);
			sysUser.setVersion(version);
			sysUser.setDelFlag(delFlag);
			sysUser.phone = this.phone;
			sysUser.type = this.type;
			sysUser.deptId = this.deptId;
			sysUser.tenantId = this.tenantId;
			sysUser.jobId = this.jobId;
			sysUser.sex = this.sex;
			sysUser.nickname = this.nickname;
			sysUser.email = this.email;
			sysUser.isLock = this.isLock;
			sysUser.username = this.username;
			sysUser.avatar = this.avatar;
			sysUser.password = this.password;
			return sysUser;
		}
	}
}
