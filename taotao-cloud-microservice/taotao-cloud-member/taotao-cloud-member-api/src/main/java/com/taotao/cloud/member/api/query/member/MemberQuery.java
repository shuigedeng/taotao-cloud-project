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
package com.taotao.cloud.member.api.query.member;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

/**
 * 会员query
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/30 08:49
 */
@Schema(name = "MemberQuery", description = "会员query")
public class MemberQuery implements Serializable {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "用户昵称")
	private String nickname;

	@Schema(description = "用户名称")
	private String username;

	@Schema(description = "手机号码")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码格式错误")
	private String phone;

	@Schema(description = "邮箱")
	@Email(message = "邮箱格式错误")
	private String email;


	public MemberQuery() {
	}

	public MemberQuery(String nickname, String username, String phone, String email) {
		this.nickname = nickname;
		this.username = username;
		this.phone = phone;
		this.email = email;
	}

	@Override
	public String toString() {
		return "MemberQuery{" +
			"nickname='" + nickname + '\'' +
			", username='" + username + '\'' +
			", phone='" + phone + '\'' +
			", email='" + email + '\'' +
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
		MemberQuery that = (MemberQuery) o;
		return Objects.equals(nickname, that.nickname) && Objects.equals(username,
			that.username) && Objects.equals(phone, that.phone)
			&& Objects.equals(email, that.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nickname, username, phone, email);
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

	public static MemberQueryBuilder builder() {
		return new MemberQueryBuilder();
	}

	public static final class MemberQueryBuilder {

		private String nickname;
		private String username;
		private String phone;
		private String email;

		private MemberQueryBuilder() {
		}

		public static MemberQueryBuilder aMemberQuery() {
			return new MemberQueryBuilder();
		}

		public MemberQueryBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public MemberQueryBuilder username(String username) {
			this.username = username;
			return this;
		}

		public MemberQueryBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public MemberQueryBuilder email(String email) {
			this.email = email;
			return this;
		}

		public MemberQuery build() {
			MemberQuery memberQuery = new MemberQuery();
			memberQuery.setNickname(nickname);
			memberQuery.setUsername(username);
			memberQuery.setPhone(phone);
			memberQuery.setEmail(email);
			return memberQuery;
		}
	}
}
