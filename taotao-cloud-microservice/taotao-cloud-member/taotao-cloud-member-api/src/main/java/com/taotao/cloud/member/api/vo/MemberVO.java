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
package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 会员信息VO
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/20 上午9:42
 */
@Schema(name = "MemberVO", description = "会员信息VO")
public class MemberVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "昵称")
	private String nickname;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "手机号")
	private String phone;

	@Schema(description = "密码")
	private String password;

	@Schema(description = "头像")
	private String avatar;

	@Schema(description = "性别")
	private Byte gender;

	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "等级")
	private Integer level;

	@Schema(description = "用户类型")
	private Integer type;

	@Schema(description = "创建ip")
	private String createIp;

	@Schema(description = "最后一次登陆时间")
	private LocalDateTime lastLoginTime;

	@Schema(description = "最后一次登陆ip")
	private String lastLoginIp;

	@Schema(description = "是否锁定")
	private Integer isLock;

	@Schema(description = "状态 1:启用, 0:停用")
	private Integer status;

	@Schema(description = "登录次数")
	private Integer loginTimes;

	@Schema(description = "省code")
	private String provinceCode;

	@Schema(description = "市code")
	private String cityCode;

	@Schema(description = "区、县code")
	private String areaCode;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;

	public MemberVO() {
	}

	public MemberVO(Long id, String nickname, String username, String phone, String password,
		String avatar, Byte gender, String email, Integer level, Integer type,
		String createIp, LocalDateTime lastLoginTime, String lastLoginIp, Integer isLock,
		Integer status, Integer loginTimes, String provinceCode, String cityCode,
		String areaCode, LocalDateTime createTime, LocalDateTime lastModifiedTime) {
		this.id = id;
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
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	@Override
	public String toString() {
		return "MemberVO{" +
			"id=" + id +
			", nickname='" + nickname + '\'' +
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
		MemberVO memberVO = (MemberVO) o;
		return Objects.equals(id, memberVO.id) && Objects.equals(nickname,
			memberVO.nickname) && Objects.equals(username, memberVO.username)
			&& Objects.equals(phone, memberVO.phone) && Objects.equals(password,
			memberVO.password) && Objects.equals(avatar, memberVO.avatar)
			&& Objects.equals(gender, memberVO.gender) && Objects.equals(email,
			memberVO.email) && Objects.equals(level, memberVO.level)
			&& Objects.equals(type, memberVO.type) && Objects.equals(createIp,
			memberVO.createIp) && Objects.equals(lastLoginTime, memberVO.lastLoginTime)
			&& Objects.equals(lastLoginIp, memberVO.lastLoginIp)
			&& Objects.equals(isLock, memberVO.isLock) && Objects.equals(status,
			memberVO.status) && Objects.equals(loginTimes, memberVO.loginTimes)
			&& Objects.equals(provinceCode, memberVO.provinceCode)
			&& Objects.equals(cityCode, memberVO.cityCode) && Objects.equals(
			areaCode, memberVO.areaCode) && Objects.equals(createTime,
			memberVO.createTime) && Objects.equals(lastModifiedTime,
			memberVO.lastModifiedTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nickname, username, phone, password, avatar, gender, email, level,
			type,
			createIp, lastLoginTime, lastLoginIp, isLock, status, loginTimes, provinceCode,
			cityCode, areaCode, createTime, lastModifiedTime);
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

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
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

	public Integer getIsLock() {
		return isLock;
	}

	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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

	public static MemberVOBuilder builder() {
		return new MemberVOBuilder();
	}

	public static final class MemberVOBuilder {

		private Long id;
		private String nickname;
		private String username;
		private String phone;
		private String password;
		private String avatar;
		private Byte gender;
		private String email;
		private Integer level;
		private Integer type;
		private String createIp;
		private LocalDateTime lastLoginTime;
		private String lastLoginIp;
		private Integer isLock;
		private Integer status;
		private Integer loginTimes;
		private String provinceCode;
		private String cityCode;
		private String areaCode;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;

		private MemberVOBuilder() {
		}

		public static MemberVOBuilder aMemberVO() {
			return new MemberVOBuilder();
		}

		public MemberVOBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public MemberVOBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public MemberVOBuilder username(String username) {
			this.username = username;
			return this;
		}

		public MemberVOBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public MemberVOBuilder password(String password) {
			this.password = password;
			return this;
		}

		public MemberVOBuilder avatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public MemberVOBuilder gender(Byte gender) {
			this.gender = gender;
			return this;
		}

		public MemberVOBuilder email(String email) {
			this.email = email;
			return this;
		}

		public MemberVOBuilder level(Integer level) {
			this.level = level;
			return this;
		}

		public MemberVOBuilder type(Integer type) {
			this.type = type;
			return this;
		}

		public MemberVOBuilder createIp(String createIp) {
			this.createIp = createIp;
			return this;
		}

		public MemberVOBuilder lastLoginTime(LocalDateTime lastLoginTime) {
			this.lastLoginTime = lastLoginTime;
			return this;
		}

		public MemberVOBuilder lastLoginIp(String lastLoginIp) {
			this.lastLoginIp = lastLoginIp;
			return this;
		}

		public MemberVOBuilder isLock(Integer isLock) {
			this.isLock = isLock;
			return this;
		}

		public MemberVOBuilder status(Integer status) {
			this.status = status;
			return this;
		}

		public MemberVOBuilder loginTimes(Integer loginTimes) {
			this.loginTimes = loginTimes;
			return this;
		}

		public MemberVOBuilder provinceCode(String provinceCode) {
			this.provinceCode = provinceCode;
			return this;
		}

		public MemberVOBuilder cityCode(String cityCode) {
			this.cityCode = cityCode;
			return this;
		}

		public MemberVOBuilder areaCode(String areaCode) {
			this.areaCode = areaCode;
			return this;
		}

		public MemberVOBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public MemberVOBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public MemberVO build() {
			MemberVO memberVO = new MemberVO();
			memberVO.setId(id);
			memberVO.setNickname(nickname);
			memberVO.setUsername(username);
			memberVO.setPhone(phone);
			memberVO.setPassword(password);
			memberVO.setAvatar(avatar);
			memberVO.setGender(gender);
			memberVO.setEmail(email);
			memberVO.setLevel(level);
			memberVO.setType(type);
			memberVO.setCreateIp(createIp);
			memberVO.setLastLoginTime(lastLoginTime);
			memberVO.setLastLoginIp(lastLoginIp);
			memberVO.setIsLock(isLock);
			memberVO.setStatus(status);
			memberVO.setLoginTimes(loginTimes);
			memberVO.setProvinceCode(provinceCode);
			memberVO.setCityCode(cityCode);
			memberVO.setAreaCode(areaCode);
			memberVO.setCreateTime(createTime);
			memberVO.setLastModifiedTime(lastModifiedTime);
			return memberVO;
		}
	}
}
