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
 * 用户查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:18:07
 */
@Schema(name = "UserQueryDTO", description = "用户查询对象")
public class UserQueryDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1972549738577159538L;

	/**
	 * 用户昵称
	 */
	@Schema(description = "用户昵称")
	private String nickname;
	/**
	 * 用户真实姓名
	 */
	@Schema(description = "用户真实姓名")
	private String username;
	/**
	 * 电话
	 */
	@Schema(description = "电话")
	private String phone;
	/**
	 * email
	 */
	@Schema(description = "email")
	private String email;
	/**
	 * 用户类型 1前端用户 2商户用户 3后台管理用户
	 */
	@Schema(description = "用户类型 1前端用户 2商户用户 3后台管理用户")
//	@IntEnums(value = {1, 2, 3})
	private Integer type;
	/**
	 * 性别 1男 2女 0未知
	 */
	@Schema(description = "性别 1男 2女 0未知")
//	@IntEnums(value = {0, 1, 2})
	private Integer sex;
	/**
	 * 部门id
	 */
	@Schema(description = "部门id")
	private Long deptId;
	/**
	 * 岗位id
	 */
	@Schema(description = "岗位id")
	private Long jobId;

	public UserQueryDTO() {

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

}
