package com.taotao.cloud.uc.api.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

/**
 * SecurityUserQuery
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "SecurityUserQuery", description = "用户查询query")
public class SecurityUserQuery implements Serializable {

	private static final long serialVersionUID = -6200931899296559445L;

	@Schema(description = "用户昵称")
	private String nickname;

	@Schema(description = "用户真实姓名")
	private String username;

	@Schema(description = "电话")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码格式错误")
	private String phone;

	@Schema(description = "email")
	@Email(message = "邮箱格式错误")
	private String email;


	@Override
	public String toString() {
		return "SecurityUserQuery{" +
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
		SecurityUserQuery that = (SecurityUserQuery) o;
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

	public SecurityUserQuery() {
	}

	public SecurityUserQuery(String nickname, String username, String phone, String email) {
		this.nickname = nickname;
		this.username = username;
		this.phone = phone;
		this.email = email;
	}

	public static SecurityUserQueryBuilder builder() {
		return new SecurityUserQueryBuilder();
	}


	public static final class SecurityUserQueryBuilder {

		private String nickname;
		private String username;
		private String phone;
		private String email;

		private SecurityUserQueryBuilder() {
		}

		public static SecurityUserQueryBuilder aSecurityUserQuery() {
			return new SecurityUserQueryBuilder();
		}

		public SecurityUserQueryBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public SecurityUserQueryBuilder username(String username) {
			this.username = username;
			return this;
		}

		public SecurityUserQueryBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public SecurityUserQueryBuilder email(String email) {
			this.email = email;
			return this;
		}

		public SecurityUserQuery build() {
			SecurityUserQuery securityUserQuery = new SecurityUserQuery();
			securityUserQuery.setNickname(nickname);
			securityUserQuery.setUsername(username);
			securityUserQuery.setPhone(phone);
			securityUserQuery.setEmail(email);
			return securityUserQuery;
		}
	}
}
