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
package com.taotao.cloud.uc.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 用户重置密码DTO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:21:15
 */
@Schema(name = "RestPasswordUserDTO", description = "用户重置密码DTO")
public class RestPasswordUserDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -5002412807608124376L;

	/**
	 * 手机号
	 */
	@Schema(description = "手机号", required = true)
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码不正确")
	private String phone;
	/**
	 * 原密码
	 */
	@Schema(description = "原密码", required = true)
	@NotBlank(message = "原密码不能为空")
	private String oldPassword;
	/**
	 * 新密码
	 */
	@Schema(description = "新密码", required = true)
	@NotBlank(message = "新密码不能为空")
	@Length(min = 6, max = 128, message = "密码长度不能小于6位")
	private String newPassword;

	public RestPasswordUserDTO() {
	}

	public RestPasswordUserDTO(String phone, String oldPassword, String newPassword) {
		this.phone = phone;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "RestPasswordUserDTO{" +
			"phone='" + phone + '\'' +
			", oldPassword='" + oldPassword + '\'' +
			", newPassword='" + newPassword + '\'' +
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
		RestPasswordUserDTO that = (RestPasswordUserDTO) o;
		return Objects.equals(phone, that.phone) && Objects.equals(oldPassword,
			that.oldPassword) && Objects.equals(newPassword, that.newPassword);
	}

	@Override
	public int hashCode() {
		return Objects.hash(phone, oldPassword, newPassword);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public static RestPasswordUserDTOBuilder builder() {
		return new RestPasswordUserDTOBuilder();
	}


	public static final class RestPasswordUserDTOBuilder {

		private String phone;
		private String oldPassword;
		private String newPassword;

		private RestPasswordUserDTOBuilder() {
		}

		public static RestPasswordUserDTOBuilder aRestPasswordUserDTO() {
			return new RestPasswordUserDTOBuilder();
		}

		public RestPasswordUserDTOBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public RestPasswordUserDTOBuilder oldPassword(String oldPassword) {
			this.oldPassword = oldPassword;
			return this;
		}

		public RestPasswordUserDTOBuilder newPassword(String newPassword) {
			this.newPassword = newPassword;
			return this;
		}

		public RestPasswordUserDTO build() {
			RestPasswordUserDTO restPasswordUserDTO = new RestPasswordUserDTO();
			restPasswordUserDTO.setPhone(phone);
			restPasswordUserDTO.setOldPassword(oldPassword);
			restPasswordUserDTO.setNewPassword(newPassword);
			return restPasswordUserDTO;
		}
	}
}
