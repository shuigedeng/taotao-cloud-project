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
package com.taotao.cloud.log.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/20 上午9:42
 */
@Schema(name = "MemberLoginVO", description = "会员登录日志信息VO")
public class MemberLoginVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "机器人名称")
	private String name;

	@Schema(description = "会员id")
	private Long memberId;

	@Schema(description = "用户登录时间")
	private LocalDateTime loginTime;

	@Schema(description = "登陆ip")
	private String loginIp;

	@Schema(description = "登录状态")
	private Integer loginStatus;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;

	public MemberLoginVO() {
	}

	@Override
	public String toString() {
		return "MemberLoginVO{" +
			"id=" + id +
			", name='" + name + '\'' +
			", memberId=" + memberId +
			", loginTime=" + loginTime +
			", loginIp='" + loginIp + '\'' +
			", loginStatus=" + loginStatus +
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
		MemberLoginVO that = (MemberLoginVO) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name)
			&& Objects.equals(memberId, that.memberId) && Objects.equals(loginTime,
			that.loginTime) && Objects.equals(loginIp, that.loginIp)
			&& Objects.equals(loginStatus, that.loginStatus) && Objects.equals(
			createTime, that.createTime) && Objects.equals(lastModifiedTime,
			that.lastModifiedTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, memberId, loginTime, loginIp, loginStatus, createTime,
			lastModifiedTime);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public MemberLoginVO(Long id, String name, Long memberId, LocalDateTime loginTime,
		String loginIp, Integer loginStatus, LocalDateTime createTime,
		LocalDateTime lastModifiedTime) {
		this.id = id;
		this.name = name;
		this.memberId = memberId;
		this.loginTime = loginTime;
		this.loginIp = loginIp;
		this.loginStatus = loginStatus;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	public static MemberLoginVOBuilder builder() {
		return new MemberLoginVOBuilder();
	}

	public static final class MemberLoginVOBuilder {

		private Long id;
		private String name;
		private Long memberId;
		private LocalDateTime loginTime;
		private String loginIp;
		private Integer loginStatus;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;

		private MemberLoginVOBuilder() {
		}

		public static MemberLoginVOBuilder aMemberLoginVO() {
			return new MemberLoginVOBuilder();
		}

		public MemberLoginVOBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public MemberLoginVOBuilder name(String name) {
			this.name = name;
			return this;
		}

		public MemberLoginVOBuilder memberId(Long memberId) {
			this.memberId = memberId;
			return this;
		}

		public MemberLoginVOBuilder loginTime(LocalDateTime loginTime) {
			this.loginTime = loginTime;
			return this;
		}

		public MemberLoginVOBuilder loginIp(String loginIp) {
			this.loginIp = loginIp;
			return this;
		}

		public MemberLoginVOBuilder loginStatus(Integer loginStatus) {
			this.loginStatus = loginStatus;
			return this;
		}

		public MemberLoginVOBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public MemberLoginVOBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public MemberLoginVO build() {
			MemberLoginVO memberLoginVO = new MemberLoginVO();
			memberLoginVO.setId(id);
			memberLoginVO.setName(name);
			memberLoginVO.setMemberId(memberId);
			memberLoginVO.setLoginTime(loginTime);
			memberLoginVO.setLoginIp(loginIp);
			memberLoginVO.setLoginStatus(loginStatus);
			memberLoginVO.setCreateTime(createTime);
			memberLoginVO.setLastModifiedTime(lastModifiedTime);
			return memberLoginVO;
		}
	}
}
