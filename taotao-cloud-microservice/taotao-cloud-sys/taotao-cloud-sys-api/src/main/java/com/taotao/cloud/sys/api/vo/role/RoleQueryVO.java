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
package com.taotao.cloud.sys.api.vo.role;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:23:58
 */
@Schema( description = "角色查询对象")
public record RoleQueryVO(
	/**
	 * id
	 */
	@Schema(description = "id")
	Long id,
	/**
	 * 角色名称
	 */
	@Schema(description = "角色名称")
	String name,
	/**
	 * 角色code
	 */
	@Schema(description = "角色code")
	String code,
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	String remark,
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	LocalDateTime createTime,
	/**
	 * 最后修改时间
	 */
	@Schema(description = "最后修改时间")
	LocalDateTime lastModifiedTime) implements Serializable {

	@Serial
	static final long serialVersionUID = 5126530068827085130L;


}
