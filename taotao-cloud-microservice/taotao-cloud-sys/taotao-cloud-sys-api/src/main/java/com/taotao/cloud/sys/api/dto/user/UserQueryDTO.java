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
import java.io.Serializable;

/**
 * 用户查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:18:07
 */
@Schema(description = "用户查询对象")
public record UserQueryDTO(
	/**
	 * 用户昵称
	 */
	@Schema(description = "用户昵称")
	String nickname,
	/**
	 * 用户真实姓名
	 */
	@Schema(description = "用户真实姓名")
	String username,
	/**
	 * 电话
	 */
	@Schema(description = "电话")
	String phone,
	/**
	 * email
	 */
	@Schema(description = "email")
	String email,
	/**
	 * 用户类型 1前端用户 2商户用户 3后台管理用户
	 */
	@Schema(description = "用户类型 1前端用户 2商户用户 3后台管理用户")
//	@IntEnums(value = {1, 2, 3})
	Integer type,
	/**
	 * 性别 1男 2女 0未知
	 */
	@Schema(description = "性别 1男 2女 0未知")
//	@IntEnums(value = {0, 1, 2})
	Integer sex,
	/**
	 * 部门id
	 */
	@Schema(description = "部门id")
	Long deptId,
	/**
	 * 岗位id
	 */
	@Schema(description = "岗位id")
	Long jobId) implements Serializable {

	static final long serialVersionUID = -1972549738577159538L;


}
