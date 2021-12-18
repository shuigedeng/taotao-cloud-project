package com.taotao.cloud.member.biz.entity;

import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员(c端用户)表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Entity
@Table(name = "tt_member")
@org.hibernate.annotations.Table(appliesTo = "tt_member", comment = "会员(c端用户)表")
public class MemberBack extends JpaSuperEntity {

	/**
	 * 昵称
	 */
	@Column(name = "nickname", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '昵称'")
	private String nickname;

	/**
	 * 用户名
	 */
	@Column(name = "username", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '用户名'")
	private String username;

	/**
	 * 手机号
	 */
	@Column(name = "phone", unique = true, nullable = false, columnDefinition = "varchar(14) not null comment '手机号'")
	private String phone;

	/**
	 * 密码
	 */
	@Column(name = "password", updatable = true, nullable = false, columnDefinition = "varchar(255) not null comment '密码'")
	private String password;

	/**
	 * 头像
	 */
	@Column(name = "avatar", columnDefinition = "varchar(255) NOT NULL DEFAULT '' comment '头像'")
	private String avatar = "";

	/**
	 * @see UserSexEnum
	 */
	@Column(name = "gender", nullable = false, columnDefinition = "tinyint(1) unsigned not null default 0 comment '性别 1男 2女 0未知'")
	private byte gender = 0;

	/**
	 * 邮箱
	 */
	@Column(name = "email", unique = true, columnDefinition = "varchar(30) comment '邮箱'")
	private String email;

	/**
	 * @see UserLevelEnum
	 */
	@Column(name = "level", nullable = false, columnDefinition = "tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '用户等级 0:普通用户 1:vip'")
	private byte level = 0;

	@Column(name = "type", nullable = false, columnDefinition = "tinyint(1) unsigned not null default 1 comment '用户类型 1个人用户 2企业用户'")
	private byte type = 1;

	/**
	 * 创建ip
	 */
	@Column(name = "create_ip", columnDefinition = "varchar(12) DEFAULT '' COMMENT '创建ip'")
	private String createIp = "";

	/**
	 * 最后一次登陆时间
	 */
	@Column(name = "last_login_time", columnDefinition = "TIMESTAMP comment '最后一次登陆时间'")
	private LocalDateTime lastLoginTime = LocalDateTime.now();

	/**
	 * 最后一次登陆ip
	 */
	@Column(name = "last_login_ip", columnDefinition = "varchar(12) DEFAULT '' COMMENT '最后一次登陆ip'")
	private String lastLoginIp = "";

	/**
	 * 是否锁定 1-正常，2-锁定
	 */
	@Column(name = "is_lock", nullable = false, columnDefinition = "tinyint(1) unsigned NOT NULL DEFAULT 1 comment '是否锁定 1-正常，0-锁定'")
	private Byte isLock = 1;

	/**
	 * 状态 1:启用, 0:停用
	 */
	@Column(name = "status", nullable = false, columnDefinition = "tinyint(1) unsigned NOT NULL DEFAULT 1 comment '状态 1:启用, 0:停用'")
	private Byte status = 1;

	/**
	 * 登录次数
	 */
	@Column(name = "login_times", columnDefinition = " int(11) DEFAULT 0 COMMENT '登录次数'")
	private Integer loginTimes;

	/**
	 * 省code
	 */
	@Column(name = "province_code", columnDefinition = "varchar(32) COMMENT '省code'")
	private String provinceCode;

	/**
	 * 市code
	 */
	@Column(name = "city_code", columnDefinition = "varchar(32) COMMENT '市code'")
	private String cityCode;

	/**
	 * 区、县code
	 */
	@Column(name = "area_code", columnDefinition = "varchar(32) COMMENT '区、县code'")
	private String areaCode;

	public MemberBack() {
	}

	public MemberBack(String nickname, String username, String phone, String password,
		String avatar, byte gender, String email, byte level, byte type, String createIp,
		LocalDateTime lastLoginTime, String lastLoginIp, Byte isLock, Byte status,
		Integer loginTimes, String provinceCode, String cityCode, String areaCode) {
		this.nickname = nickname;
		this.username = username;
		this.phone = phone;
		this.password = password;
		this.avatar = avatar;
		this.gender = gender;
		this.email = email;
		this.level = level;
		this.type = type;
		this.createIp = createIp;
		this.lastLoginTime = lastLoginTime;
		this.lastLoginIp = lastLoginIp;
		this.isLock = isLock;
		this.status = status;
		this.loginTimes = loginTimes;
		this.provinceCode = provinceCode;
		this.cityCode = cityCode;
		this.areaCode = areaCode;
	}

	public MemberBack(Long id, Long createBy, Long lastModifiedBy, LocalDateTime createTime,
		LocalDateTime lastModifiedTime, int version, Boolean delFlag, String nickname,
		String username, String phone, String password, String avatar, byte gender,
		String email, byte level, byte type, String createIp, LocalDateTime lastLoginTime,
		String lastLoginIp, Byte isLock, Byte status, Integer loginTimes,
		String provinceCode, String cityCode, String areaCode) {
		super(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version, delFlag);
		this.nickname = nickname;
		this.username = username;
		this.phone = phone;
		this.password = password;
		this.avatar = avatar;
		this.gender = gender;
		this.email = email;
		this.level = level;
		this.type = type;
		this.createIp = createIp;
		this.lastLoginTime = lastLoginTime;
		this.lastLoginIp = lastLoginIp;
		this.isLock = isLock;
		this.status = status;
		this.loginTimes = loginTimes;
		this.provinceCode = provinceCode;
		this.cityCode = cityCode;
		this.areaCode = areaCode;
	}

	@Override
	public String toString() {
		return "Member{" +
			"nickname='" + nickname + '\'' +
			", username='" + username + '\'' +
			", phone='" + phone + '\'' +
			", password='" + password + '\'' +
			", avatar='" + avatar + '\'' +
			", gender=" + gender +
			", email='" + email + '\'' +
			", level=" + level +
			", type=" + type +
			", createIp='" + createIp + '\'' +
			", lastLoginTime=" + lastLoginTime +
			", lastLoginIp='" + lastLoginIp + '\'' +
			", isLock=" + isLock +
			", status=" + status +
			", loginTimes=" + loginTimes +
			", provinceCode='" + provinceCode + '\'' +
			", cityCode='" + cityCode + '\'' +
			", areaCode='" + areaCode + '\'' +
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
		MemberBack member = (MemberBack) o;
		return gender == member.gender && level == member.level && type == member.type
			&& Objects.equals(nickname, member.nickname) && Objects.equals(
			username, member.username) && Objects.equals(phone, member.phone)
			&& Objects.equals(password, member.password) && Objects.equals(
			avatar, member.avatar) && Objects.equals(email, member.email)
			&& Objects.equals(createIp, member.createIp) && Objects.equals(
			lastLoginTime, member.lastLoginTime) && Objects.equals(lastLoginIp,
			member.lastLoginIp) && Objects.equals(isLock, member.isLock)
			&& Objects.equals(status, member.status) && Objects.equals(
			loginTimes, member.loginTimes) && Objects.equals(provinceCode,
			member.provinceCode) && Objects.equals(cityCode, member.cityCode)
			&& Objects.equals(areaCode, member.areaCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), nickname, username, phone, password, avatar, gender,
			email, level, type, createIp, lastLoginTime, lastLoginIp, isLock, status, loginTimes,
			provinceCode, cityCode, areaCode);
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public byte getGender() {
		return gender;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Byte getIsLock() {
		return isLock;
	}

	public void setIsLock(Byte isLock) {
		this.isLock = isLock;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public static MemberBuilder builder() {
		return new MemberBuilder();
	}

	public static final class MemberBuilder {

		private String nickname;
		private String username;
		private String phone;
		private String password;
		private String avatar = "";
		private byte gender = 0;
		private String email;
		private byte level = 0;
		private byte type = 1;
		private String createIp = "";
		private LocalDateTime lastLoginTime = LocalDateTime.now();
		private String lastLoginIp = "";
		private Byte isLock = 1;
		private Byte status = 1;
		private Integer loginTimes;
		private String provinceCode;
		private String cityCode;
		private String areaCode;
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private MemberBuilder() {
		}

		public static MemberBuilder aMember() {
			return new MemberBuilder();
		}

		public MemberBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public MemberBuilder username(String username) {
			this.username = username;
			return this;
		}

		public MemberBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public MemberBuilder password(String password) {
			this.password = password;
			return this;
		}

		public MemberBuilder avatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public MemberBuilder gender(byte gender) {
			this.gender = gender;
			return this;
		}

		public MemberBuilder email(String email) {
			this.email = email;
			return this;
		}

		public MemberBuilder level(byte level) {
			this.level = level;
			return this;
		}

		public MemberBuilder type(byte type) {
			this.type = type;
			return this;
		}

		public MemberBuilder createIp(String createIp) {
			this.createIp = createIp;
			return this;
		}

		public MemberBuilder lastLoginTime(LocalDateTime lastLoginTime) {
			this.lastLoginTime = lastLoginTime;
			return this;
		}

		public MemberBuilder lastLoginIp(String lastLoginIp) {
			this.lastLoginIp = lastLoginIp;
			return this;
		}

		public MemberBuilder isLock(Byte isLock) {
			this.isLock = isLock;
			return this;
		}

		public MemberBuilder status(Byte status) {
			this.status = status;
			return this;
		}

		public MemberBuilder loginTimes(Integer loginTimes) {
			this.loginTimes = loginTimes;
			return this;
		}

		public MemberBuilder provinceCode(String provinceCode) {
			this.provinceCode = provinceCode;
			return this;
		}

		public MemberBuilder cityCode(String cityCode) {
			this.cityCode = cityCode;
			return this;
		}

		public MemberBuilder areaCode(String areaCode) {
			this.areaCode = areaCode;
			return this;
		}

		public MemberBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public MemberBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public MemberBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public MemberBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public MemberBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public MemberBuilder version(int version) {
			this.version = version;
			return this;
		}

		public MemberBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public MemberBack build() {
			MemberBack member = new MemberBack();
			member.setNickname(nickname);
			member.setUsername(username);
			member.setPhone(phone);
			member.setPassword(password);
			member.setAvatar(avatar);
			member.setGender(gender);
			member.setEmail(email);
			member.setLevel(level);
			member.setType(type);
			member.setCreateIp(createIp);
			member.setLastLoginTime(lastLoginTime);
			member.setLastLoginIp(lastLoginIp);
			member.setIsLock(isLock);
			member.setStatus(status);
			member.setLoginTimes(loginTimes);
			member.setProvinceCode(provinceCode);
			member.setCityCode(cityCode);
			member.setAreaCode(areaCode);
			member.setId(id);
			member.setCreateBy(createBy);
			member.setLastModifiedBy(lastModifiedBy);
			member.setCreateTime(createTime);
			member.setLastModifiedTime(lastModifiedTime);
			member.setVersion(version);
			member.setDelFlag(delFlag);
			return member;
		}
	}
}
