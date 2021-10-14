
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

import com.taotao.cloud.web.mvc.constraints.IntEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 用户更新对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:18:07
 */
@Schema(name = "UserUpdateDTO", description = "用户更新对象")
public class UserUpdateDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1972549738577159538L;

	/**
	 * 用户昵称
	 */
	@Schema(description = "用户昵称", required = true)
	@NotBlank(message = "用户名不能超过为空")
	@Length(max = 20, message = "用户名不能超过20个字符")
	@Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "用户昵称限制格式错误：最多20字符，包含文字、字母和数字")
	private String nickname;
	/**
	 * 用户真实姓名
	 */
	@Schema(description = "用户真实姓名", required = true)
	@NotBlank(message = "用户真实姓名不能超过为空")
	@Length(max = 20, message = "用户真实姓名不能超过20个字符")
	@Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "用户真实姓名格式错误：最多20字符，包含文字、字母和数字")
	private String username;
	/**
	 * 用户类型 1前端用户 2商户用户 3后台管理用户
	 */
	@Schema(description = "用户类型 1前端用户 2商户用户 3后台管理用户", required = true)
	@NotNull(message = "用户类型不能为空")
	@IntEnums(value = {1, 2, 3})
	private Integer type;
	/**
	 * 性别 1男 2女 0未知
	 */
	@Schema(description = "性别 1男 2女 0未知", required = true)
	@NotNull(message = "用户性别不能为空")
	@IntEnums(value = {0, 1, 2})
	private Integer sex;
	/**
	 * 手机号
	 */
	@Schema(description = "手机号", required = true)
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码格式错误")
	private String phone;
	/**
	 * 邮箱
	 */
	@Schema(description = "邮箱")
	@Email(message = "邮箱格式错误")
	private String email;
	/**
	 * 部门ID
	 */
	@Schema(description = "部门ID")
	private Integer deptId;
	/**
	 * 岗位ID
	 */
	@Schema(description = "岗位ID")
	private Integer jobId;
	/**
	 * 头像
	 */
	@Schema(description = "头像")
	private String avatar;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
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

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserUpdateDTO() {
	}

	public UserUpdateDTO(String nickname, String username, Integer type, Integer sex, String phone,
		String email, Integer deptId, Integer jobId, String avatar) {
		this.nickname = nickname;
		this.username = username;
		this.type = type;
		this.sex = sex;
		this.phone = phone;
		this.email = email;
		this.deptId = deptId;
		this.jobId = jobId;
		this.avatar = avatar;
	}

}
