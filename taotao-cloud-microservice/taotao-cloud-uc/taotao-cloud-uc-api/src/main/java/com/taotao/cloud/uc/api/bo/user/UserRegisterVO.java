package com.taotao.cloud.uc.api.bo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 用户注册VO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "UserRegisterVO", description = "用户注册VO")
public class UserRegisterVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "真实用户名")
	private String username;

	@Schema(description = "手机号")
	private String phone;

	@Schema(description = "密码")
	private String password;

	public UserRegisterVO() {
	}

	public UserRegisterVO(String username, String phone, String password) {
		this.username = username;
		this.phone = phone;
		this.password = password;
	}

	@Override
	public String toString() {
		return "AddUserVO{" +
			"username='" + username + '\'' +
			", phone='" + phone + '\'' +
			", password='" + password + '\'' +
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
		UserRegisterVO userRegisterVO = (UserRegisterVO) o;
		return Objects.equals(username, userRegisterVO.username) && Objects.equals(phone,
			userRegisterVO.phone) && Objects.equals(password, userRegisterVO.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, phone, password);
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

	public static AddUserVOBuilder builder() {
		return new AddUserVOBuilder();
	}

	public static final class AddUserVOBuilder {

		private String username;
		private String phone;
		private String password;

		private AddUserVOBuilder() {
		}

		public static AddUserVOBuilder anAddUserVO() {
			return new AddUserVOBuilder();
		}

		public AddUserVOBuilder username(String username) {
			this.username = username;
			return this;
		}

		public AddUserVOBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public AddUserVOBuilder password(String password) {
			this.password = password;
			return this;
		}

		public UserRegisterVO build() {
			UserRegisterVO userRegisterVO = new UserRegisterVO();
			userRegisterVO.setUsername(username);
			userRegisterVO.setPhone(phone);
			userRegisterVO.setPassword(password);
			return userRegisterVO;
		}
	}
}
