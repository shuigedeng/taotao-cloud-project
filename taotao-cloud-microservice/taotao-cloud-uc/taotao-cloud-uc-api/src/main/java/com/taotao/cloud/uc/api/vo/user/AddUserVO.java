package com.taotao.cloud.uc.api.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * 用户注册VO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(description = "用户注册VO")
public class AddUserVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "真实用户名")
	private String username;

	@Schema(description = "手机号")
	private String phone;

	@Schema(description = "密码")
	private String password;

	public AddUserVO() {
	}

	public AddUserVO(String username, String phone, String password) {
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
		AddUserVO addUserVO = (AddUserVO) o;
		return Objects.equals(username, addUserVO.username) && Objects.equals(phone,
			addUserVO.phone) && Objects.equals(password, addUserVO.password);
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

		public AddUserVO build() {
			AddUserVO addUserVO = new AddUserVO();
			addUserVO.setUsername(username);
			addUserVO.setPhone(phone);
			addUserVO.setPassword(password);
			return addUserVO;
		}
	}
}
