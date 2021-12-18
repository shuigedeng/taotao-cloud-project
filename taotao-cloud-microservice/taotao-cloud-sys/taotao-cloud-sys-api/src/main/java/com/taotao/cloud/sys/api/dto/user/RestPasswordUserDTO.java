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
package com.taotao.cloud.sys.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
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
@Schema(description = "用户重置密码DTO")
public record RestPasswordUserDTO(
	/**
	 * 手机号
	 */
	@Schema(description = "手机号", required = true)
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码不正确")
	String phone,
	/**
	 * 原密码
	 */
	@Schema(description = "原密码", required = true)
	@NotBlank(message = "原密码不能为空")
	String oldPassword,
	/**
	 * 新密码
	 */
	@Schema(description = "新密码", required = true)
	@NotBlank(message = "新密码不能为空")
	@Length(min = 6, max = 128, message = "密码长度不能小于6位")
	String newPassword) implements Serializable {

	@Serial
	private static final long serialVersionUID = -5002412807608124376L;

}
