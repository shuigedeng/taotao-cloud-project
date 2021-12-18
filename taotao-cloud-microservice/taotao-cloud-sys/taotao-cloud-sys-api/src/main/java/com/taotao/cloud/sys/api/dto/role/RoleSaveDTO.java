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
package com.taotao.cloud.sys.api.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 角色添加对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:25:01
 */
@Schema(description = "角色添加对象")
public record RoleSaveDTO(
	/**
	 * 角色名称
	 */
	@Schema(description = "角色名称", required = true)
	@NotBlank(message = "角色名称不能超过为空")
	@Length(max = 20, message = "角色名称不能超过20个字符")
	String name,

	/**
	 * 角色标识
	 */
	@Schema(description = "角色标识", required = true)
	@NotBlank(message = "角色标识不能超过为空")
	@Length(max = 20, message = "角色标识不能超过20个字符")
	@Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "角色标识格式错误：最多20字符，只能包含字母或者下划线")
	String code,

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	String remark) implements Serializable {

	static final long serialVersionUID = -1972549738577159538L;


}
